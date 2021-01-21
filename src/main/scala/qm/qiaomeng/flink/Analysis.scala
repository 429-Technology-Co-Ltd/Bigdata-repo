package qm.qiaomeng.flink

import java.util

import qm.qiaomeng.jackson.Jackson
import qm.qiaomeng.redis.RedisUtils

import scala.collection.mutable.ArrayBuffer

/**
 * @ClassName: Analysis
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2020/12/24 12:33
 */
object Analysis {
  /**
   * 对接过来的数据进行处理，返回多个平台比价
   *
   * @param x 要处理的数据，json字符串 
   * @return 返回搜索结果
   */
  def analysis(x: String): returnValue = {

    //切分字符串
    //一开始选择“-”做分割符，但是数据中会出现“-” 所以选择了一个比较长的分割符
    val goodsInfo = x.split("#QM#MQ#")
    //要处理的商品信息
    val head = goodsInfo(0)
    val tail = goodsInfo(1)

    //用jackson处理json字符串
    val headMap = new util.HashMap[String, Any]()
    val tailMap = new util.HashMap[String, Any]()
    val resultMap = new util.HashMap[String, Any]()
    Jackson.autoParseJson(head, headMap)
    Jackson.autoParseJson(tail, tailMap)

    //判空处理
    if (headMap.isEmpty) {
      // lintodo : 没有头部字段处理

      return returnValue("", "头部为空！", 0.0,"")
    }
    if (tailMap.isEmpty) {
      // lintodo : 没有尾部字段处理

      return returnValue("", "尾部为空！", 0.0,"")
    }

    val userId = headMap.getOrDefault("userid", "").toString
    if (userId.isEmpty) {
      // lintodo : 用户id为空处理
      return returnValue("", "没有用户id", 0.0,"")
    }

    val title = tailMap.getOrDefault("title", "").toString

    //json对象为空，转成json对象失败，接收的json字符串格式不对
    if (title.isEmpty) {
      // lintodo : 没有搜索字段处理

      return returnValue("", "没有title字段", 0.0,"")
    }

    val price = priceComparison(title,userId)

    price
  }

  // lintodo : 做比价和搜索的区分

  /**
   * 比价函数
   * @param title 搜索关键字
   * @param userId 用户的id
   * @return 最低价
   */
  private def priceComparison(title: String,userId:String): returnValue = {
    val resultMap = new util.HashMap[String, Any]()

    val items = new ArrayBuffer[returnValue]()
    //商品数据
    val results: Array[String] = SearchUtils.searchLowPrice(title)
    for (result <- results) {
      Jackson.autoParseJson(result, resultMap)

      // 转换失败
      if (resultMap.isEmpty) {
        // lintodo : 结果为空处理
        return returnValue("", "结果为空！", 0.0, "")
      }

      val itemInfo = resultMap.getOrDefault("item_information", "").toString.replaceAll("\\\\", "")
      val price = java.lang.Double.valueOf(resultMap.getOrDefault("price", 0.0).toString)
      val source = resultMap.getOrDefault("source", "").toString
      val title = resultMap.getOrDefault("item_name", "").toString

      items.append(returnValue(title,source,price,itemInfo))
    }

    //取最低价
    val lowPrice = items.minBy(_.price)

    //存入redis
    RedisUtils.writeToRedis(userId, lowPrice.itemInfo, 2000L)
    lowPrice
  }

  // 返回值
  case class returnValue(
                  title:String,
                  source:String,
                  price:Double,
                  itemInfo:String
                  )
}
