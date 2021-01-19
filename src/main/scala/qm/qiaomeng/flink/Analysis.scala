package qm.qiaomeng.flink

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
  def analysis(x: String): ((String, String, String), Double) = {

    //切分字符串
    val goodsInfo = x.split("-")
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
    if (headMap.isEmpty ) {
      return (("", "头部为空！", ""), 0.0)
    }
    if (tailMap.isEmpty) {
      return (("", "尾部为空！", ""), 0.0)
    }

    var key = headMap.getOrDefault("key", null)
    if (key == null) {
      return (("", "没有用户id！", ""), 0.0)
    }

    val title = tailMap.getOrDefault("title", null)

    //json对象为空，转成json对象失败，接收的json字符串格式不对
    if (title == null) {
      return (("", "没有标题字段！", ""), 0.0)
    }

    //搜索工具搜索
    val result = SearchUtils.search(title)
    var itemStr: String = null

    val itemArray: ArrayBuffer[((String, String, String), Double)] = new ArrayBuffer[((String, String, String), Double)]()
    // 对结果进行处理
    // 处理结果，只返回结果中的商品信息字段，商品信息为json串，里面有很多转义的“\”,需要替换掉
    for (elem <- result) {
      // 换成jackson
      Jackson.autoParseJson(elem, resultMap)

      // 转换失败
      if (resultMap.isEmpty) {
        return (("", "结果为空！", ""), 0.0)
      }

      // 取出值
      val itemInfo = resultMap.getOrDefault("item_information", null).toString
      itemStr = itemInfo.replaceAll("\\\\", "")
      val price = java.lang.Double.valueOf(resultMap.getOrDefault("price", 0.0).toString)
      val source = resultMap.getOrDefault("source", null).toString
      val title = resultMap.getOrDefault("item_name", null).toString
      itemArray.append(((source, title, itemStr), price))
    }
    //取最低价
    val lowPrice: ((String, String, String), Double) = itemArray.minBy(_._2)

    // lintodo : 存入redis
    //   RedisUtils.writeToRedis(key, lowPrice._1._3)

    lowPrice
  }
}
