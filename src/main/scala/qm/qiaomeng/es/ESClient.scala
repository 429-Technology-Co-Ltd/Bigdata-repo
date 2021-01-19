package qm.qiaomeng.es

import java.io.IOException

import org.apache.http.message.BasicHeader
import org.apache.http.{Header, HttpHost}
import org.elasticsearch.client.sniff.Sniffer
import org.elasticsearch.client.{RestClient, RestClientBuilder, RestHighLevelClient}

import scala.collection.mutable.ArrayBuffer


/**
 * @ClassName: ESClient
 * @Description: 连接ES客户端
 * @Create by: LinYang
 * @Date: 2020/12/17 10:34
 */
object ESClient {

  //主机和端口
  val hostAndPortStr = "qmbigdata01:9200,qmbigdata02:9200,qmbigdata03:9200"

  private var highLevelClient: RestHighLevelClient = _
  private var sniffer: Sniffer = _

  //  构建客户端
  def esClient(): RestClientBuilder = {
    val hostsAndPorts: Array[String] = hostAndPortStr.split(",")

    val defaultHeader: ArrayBuffer[Header] = ArrayBuffer(new BasicHeader("", ""))
    val httpHosts: ArrayBuffer[HttpHost] = new ArrayBuffer[HttpHost]()

    if (0 != hostsAndPorts.length) {
      for (elem <- hostsAndPorts) {
        val hostAndPort: Array[String] = elem.split(":")
        val host = hostAndPort(0)
        val port = hostAndPort(1).trim.toInt
        val httpHost = new HttpHost(host, port)
        httpHosts.+=(httpHost)
      }
    }

    val restClientBuilder = RestClient.builder(httpHosts.toArray: _*)
      //.setDefaultHeaders(defaultHeader.toArray)
      .setFailureListener(new RestClient.FailureListener() {
        def onFailure(node: Nothing): Unit = {
          super.onFailure(node)
        }
      })
    //      .setRequestConfigCallback((requestConfigBuilder: RequestConfig.Builder) => requestConfigBuilder.setSocketTimeout(10000))
    restClientBuilder
  }

  /**
   * 获取客户端
   *
   * @return 返回ES官方推荐的客户端
   */
  def getClient: RestHighLevelClient = {
    highLevelClient = new RestHighLevelClient(esClient())
    sniffer = Sniffer.builder(highLevelClient.getLowLevelClient)
      .setSniffAfterFailureDelayMillis(10000)
      .build()
    highLevelClient
  }

  /**
   * 关闭客户端连接
   */
  def closeClient(): Unit = {
    if (null != highLevelClient) {
      try {
        sniffer.close()
        highLevelClient.close()
      } catch {
        case e: IOException => e.printStackTrace()
      }
    }
  }
}
