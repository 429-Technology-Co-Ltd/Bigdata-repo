package qm.qiaomeng.flink

import qm.qiaomeng.jackson.{Jackson, ParseJson}
import qm.qiaomeng.redis.RedisUtils

import java.util
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
  def analysis(x: String): Unit = {

    //切分字符串
    //一开始选择“-”做分割符，但是数据中会出现“-” 所以选择了一个比较长的分割符
    val goodsInfo = x.split("#QM#MQ#")
    //要处理的商品信息
    val head = goodsInfo(0)
    val tail = goodsInfo(1)

    //用jackson处理json字符串
    val headMap = new util.HashMap[String, Any]()
    val tailMap = new util.HashMap[String, Any]()
    Jackson.autoParseJson(head, headMap)
    Jackson.autoParseJson(tail, tailMap)
    //判空处理
    if (headMap.isEmpty) {
      // 没有头部字段
      return
    }
    if (tailMap.isEmpty) {
      // 没有尾部字段
      return
    }

    val userId = headMap.getOrDefault("userid", "").toString
    if (userId.isEmpty) {
      // 用户id为空
      return
    }
    val title = tailMap.getOrDefault("title", "").toString
    //json对象为空，转成json对象失败，接收的json字符串格式不对
    if (title.isEmpty) {
      // 没有搜索字段
      return
    }

    // 增加价格比价参考
    val price = java.lang.Double.valueOf(tailMap.getOrDefault("price", 0.0).toString)
    priceComparison(title, price, userId)
  }

  /**
   * 比价函数
   *
   * @param title  搜索关键字
   * @param userId 用户的id
   * @return 最低价
   */
  private def priceComparison(title: String, price: Double, userId: String): Unit = {
    val resultMap = new util.HashMap[String, Any]()

    val items = new ArrayBuffer[(String, String, Double, String)]()
    //商品数据
    val results: Array[String] = SearchUtils.searchLowPrice(title, price)
    for (result <- results) {
      Jackson.autoParseJson(result, resultMap)

      // 结果为空
      if (resultMap.isEmpty) {
        RedisUtils.writeToRedis(userId, "", 2000L)
        return
      }

      val itemInfo = resultMap.getOrDefault("item_information", "").toString
      val price = java.lang.Double.valueOf(resultMap.getOrDefault("price", 0.0).toString)
      val source = resultMap.getOrDefault("source", "").toString
      val title = resultMap.getOrDefault("item_name", "").toString

      items.append((title, source, price, itemInfo))
    }


    //调试

    if (items.nonEmpty) {
      //取最低价
      val lowPrice = items.filter(_._2.nonEmpty).minBy(_._3)

      //判断平台
      lowPrice._2 match {
        case "淘宝" =>
          val item = ParseJson.parseTBJson(lowPrice._4)
          val itemStr = Jackson.bean2String(item)
          RedisUtils.writeToRedis(userId, itemStr, 2000L)
        case "拼多多" =>
          val item = ParseJson.parsePDDJson(lowPrice._4)
          val itemStr = Jackson.bean2String(item)
          RedisUtils.writeToRedis(userId, itemStr, 2000L)
        case "京东" =>
          val item = ParseJson.parseJDJson(lowPrice._4)
          val itemStr = Jackson.bean2String(item)
          RedisUtils.writeToRedis(userId, itemStr, 2000L)
        case "唯品会" =>
          val item = ParseJson.parseWPHJson(lowPrice._4)
          val itemStr = Jackson.bean2String(item)
          RedisUtils.writeToRedis(userId, itemStr, 2000L)
        case _ => RedisUtils.writeToRedis(userId, "", 2000L)
      }
      //      RedisUtils.writeToRedis(userId, Jackson.bean2String(lowPrice), 2000L)
    }
  }
}
