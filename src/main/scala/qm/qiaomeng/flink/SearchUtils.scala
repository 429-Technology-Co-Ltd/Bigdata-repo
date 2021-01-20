package qm.qiaomeng.flink

import org.elasticsearch.search.SearchHit

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
  def search(text: Any): Array[String] = {
    val result = new ArrayBuffer[String]()

    // 搜索三个平台的同一件商品
    // lintodo : 指定ES只搜索一件商品，加一个size参数
    // lintodo : 在此处修改搜索算法
    val tb_hits: Array[SearchHit] = ESUtil.boolQuery("goods", "source", "淘宝", "item_name", text, 1).getHits
    val jd_hits: Array[SearchHit] = ESUtil.boolQuery("goods", "source", "京东", "item_name", text, 1).getHits
    val wph_hits: Array[SearchHit] = ESUtil.boolQuery("goods", "source", "唯品会", "item_name", text, 1).getHits

    // 得到每一个平台的商品信息
    val taobao = tb_hits(0).getSourceAsString
    val jingdong = jd_hits(0).getSourceAsString
    val weipinghui = wph_hits(0).getSourceAsString

    result += taobao
    result += jingdong
    result += weipinghui

    result.toArray
  }

}
