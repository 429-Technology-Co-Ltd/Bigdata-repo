package qm.qiaomeng.flink

import org.elasticsearch.search.SearchHit
import qm.qiaomeng.es.ESUtils

import scala.collection.mutable.ArrayBuffer

/**
 * @ClassName: SearchUtils
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2020/12/26 16:40
 */
object SearchUtils {

  /**
   * 精准比价逻辑
   *
   * @param text 搜索关键字
   * @return 结果数组
   */
  def searchLowPrice(text: Any, price: Double): Array[String] = {
    val results = new ArrayBuffer[String]()

    // 搜索三个平台的同一件商品
    val platforms: Array[String] = Array("淘宝", "京东", "唯品会", "拼多多")

    for (platform <- platforms) {
      // lintodo : 在此处修改搜索算法
      val hits: Array[SearchHit] = ESUtils.boolQuery("goods", platform, "item_name", price, text, 1).getHits
      if (hits.nonEmpty) {
        results.append(hits(0).getSourceAsString)
      }
    }
    results.toArray
  }
}
