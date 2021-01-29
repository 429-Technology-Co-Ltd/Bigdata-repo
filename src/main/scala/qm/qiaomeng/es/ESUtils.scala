package qm.qiaomeng.es

import com.alibaba.fastjson.JSONObject
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.{IndexRequest, IndexResponse}
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.indices.GetIndexRequest
import org.elasticsearch.client.{RequestOptions, RestHighLevelClient}
import org.elasticsearch.common.unit.{Fuzziness, TimeValue}
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder
import org.elasticsearch.search.sort.SortOrder
import org.elasticsearch.search.{SearchHit, SearchHits}

import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * @ClassName: ESUtil
 * @Description: ES的工具类，实现ES的增删查改
 * @Create by: LinYang
 * @Date: 2020/12/17 15:27
 */

object ESUtils {


  private val bulkRequest: BulkRequest = new BulkRequest
  private val sourceBuilder = new SearchSourceBuilder
  private var searchRequest: SearchRequest = _
  private val client: RestHighLevelClient = ESClient.getClient

  //增
  /**
   * 添加数据到ES，指定索引名和id
   *
   * @param indexName 索引名称
   * @param indexId   索引id
   * @param content   要添加的内容，格式为json
   */
  def add(indexName: String,
          indexId: String,
          content: java.util.Map[String, AnyRef]): Unit = {
    val indexRequest = new IndexRequest(indexName)
    indexRequest.id(indexId)
    indexRequest.source(new JSONObject(content).toString, XContentType.JSON)
    var response: IndexResponse = null
    try {
      response = client.index(indexRequest, RequestOptions.DEFAULT)

    } catch {
      case e: IOException => e.printStackTrace()
    }
    val index = response.getIndex
    val id = response.getId

    println("添加成功：" + index + id)
  }

  //删
  /**
   * 删除索引
   *
   * @param indexName 要删除的索引名字
   * @return 是否删除成功
   */
  def delete(indexName: String): Boolean = {
    var flag = false
    val deleteRequest = new DeleteRequest(indexName)
    try {
      if (!indexExists(indexName)) {
        println("索引不存在!")
      }
      client.delete(deleteRequest, RequestOptions.DEFAULT)
      flag = !indexExists(indexName)
    } catch {
      case e: IOException => e.printStackTrace()
    }
    flag
  }

  //改


  //查
  /**
   * 查询指定的索引是否存在
   *
   * @param indexName 索引名字
   * @return 返回是否存在
   */
  def indexExists(indexName: String): Boolean = {
    var exits: Boolean = false
    try {
      val request: GetIndexRequest = new GetIndexRequest(indexName)
      exits = client.indices.exists(request, RequestOptions.DEFAULT)
    } catch {
      case e: Exception => e.printStackTrace()
    }
    exits
  }

  //获取一个文档

  def get(indexName: String): Unit = {
    val getRequest = new GetRequest(indexName)
    try {
      val getResponse = client.get(getRequest, RequestOptions.DEFAULT)
      if (getResponse.isExists) {
        val index = getResponse.getIndex
        val id = getResponse.getId
        val version = getResponse.getVersion
        val source = getResponse.getSourceAsString
        val sourceMap = getResponse.getSourceAsMap
        val sourceBytes = getResponse.getSourceAsBytes

        println(index + id + version)
        println(source)
        println(sourceMap)
        println(sourceBytes)
      } else {
        println("没有查询到结果")
      }
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }

  /**
   * 无条件查询指定索引
   *
   * @param indexName 指定的索引名
   * @param size      取出多少条数据
   * @return 返回长度为size的搜索结果数组
   */
  def searchAll(indexName: String, size: Int): Array[SearchHit] = {

    if (!indexExists(indexName)) {
      return null
    }

    searchRequest = new SearchRequest(indexName)
    //构建查询
    val queryBuilder = QueryBuilders.matchAllQuery()
    //查询
    sourceBuilder
      .query(queryBuilder)
      .timeout(new TimeValue(60, TimeUnit.SECONDS))
      .size(size)
    searchRequest.source(sourceBuilder)
    //搜索
    val response = client.search(searchRequest, RequestOptions.DEFAULT)
    response.getHits.getHits
  }

  /**
   * 根据字段精准匹配 term
   *
   * @param indexName 要查询的索引
   * @param fieldName 字段名
   * @param text      匹配的值
   * @param size      取出的条数
   * @return 返回搜索结果,长度为size的结果数组
   */
  def termMatching(indexName: String, fieldName: String, text: AnyRef, size: Int): Array[SearchHit] = {
    if (!indexExists(indexName)) {
      return null
    }
    searchRequest = new SearchRequest(indexName)
    val termQueryBuilder = QueryBuilders.termQuery(fieldName, text)
    sourceBuilder.query(termQueryBuilder)
    searchRequest.source(sourceBuilder)
    val response = client.search(searchRequest, RequestOptions.DEFAULT)
    response.getHits.getHits
  }

  /**
   * 根据指定索引字段模糊匹配
   *
   * @param indexName 索引名字
   * @param fieldName 字段名字
   * @param text      匹配文本
   * @param size      取出的长度
   * @param sortField 排序字段
   * @param sortOrder 排序方式
   *                  升序:SortOrder.ASC,
   *                  降序:SortOrder.DESC
   * @return 返回size长度的结果数组
   */
  def fuzzyMatching(indexName: String, fieldName: String, text: AnyRef, size: Int, sortField: String, sortOrder: SortOrder): Array[SearchHit] = {
    if (!indexExists(indexName)) {
      return null
    }
    searchRequest = new SearchRequest(indexName)
    val matchQueryBuilder = QueryBuilders.matchQuery(fieldName, text)
      .fuzziness(Fuzziness.AUTO)
      .prefixLength(3)
      .maxExpansions(10)
    sourceBuilder.query(matchQueryBuilder)
      .timeout(new TimeValue(60, TimeUnit.SECONDS))
      .size(size)
    searchRequest.source(sourceBuilder)
    val response = client.search(searchRequest, RequestOptions.DEFAULT)
    response.getHits.getHits
  }


  /**
   * 根据指定的多字段进行模糊匹配
   *
   * @param size       取出的长度
   * @param indexName  索引名字
   * @param text       匹配的文本
   * @param fieldNames 字段名
   * @return 返回结果的数组
   */
  def multiFuzzyMatching(size: Int, indexName: String, text: Any, fieldNames: String*): SearchHits = {
    // 多字段匹配
    if (!indexExists(indexName)) {
      return null
    }
    searchRequest = new SearchRequest(indexName)
    val multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(text, fieldNames: _*)
      .fuzziness(Fuzziness.AUTO)
      .maxExpansions(10)
      .prefixLength(3)

    sourceBuilder.query(multiMatchQueryBuilder).size(size)
    searchRequest.source(sourceBuilder)
    val response = client.search(searchRequest, RequestOptions.DEFAULT)

    response.getHits
  }

  //复合查询
  def boolQuery(indexName: String,
                source: String,
                name: String,
                price: Double,
                value: Any,
                size: Int): SearchHits = {
    searchRequest = new SearchRequest(indexName)
    val boolQueryBuilder = QueryBuilders.boolQuery
      //精准查询source字段的值，filedName写‘source’，source写‘淘宝’、‘拼多多’……
      .must(QueryBuilders.termQuery("source", source))
      //
      .must(QueryBuilders.rangeQuery("price").gte(price * 0.8))
      //匹配item_name字段的值
      .should(QueryBuilders.matchQuery(name, value))

    sourceBuilder.query(boolQueryBuilder).size(size)
    searchRequest.source(sourceBuilder)

    val highlightBuilder = new HighlightBuilder

    highlightBuilder.field("item_name", 10)
      .preTags("<")
      .postTags(">")

    sourceBuilder.highlighter(highlightBuilder)

    val response = client.search(searchRequest, RequestOptions.DEFAULT)

    response.getHits

  }
}
