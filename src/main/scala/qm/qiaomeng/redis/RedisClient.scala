package qm.qiaomeng.redis

import java.time.Duration

import io.lettuce.core.RedisURI
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.cluster.{ClusterClientOptions, ClusterTopologyRefreshOptions, RedisClusterClient}

import scala.collection.JavaConverters._
import scala.collection.immutable

/**
 * @ClassName: RedisUtils
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/1/12 9:51
 */
object RedisClient {

  //ip和端口
  val ipAndPorts: Array[String] = Array(
    "8.135.22.177:6379",
    "8.135.53.75:6379",
    "8.135.58.206:6379",
    "8.135.22.177:6380",
    "8.135.53.75:6380",
    "8.135.58.206:6380"
  )

  // 创建redis集群连接
  def getRedisClient: StatefulRedisClusterConnection[String, String] = {
    val uris: immutable.Seq[RedisURI] = ipAndPorts.map(host => {
      val ip = host.split(":")(0)
      val port = host.split(":")(1)
      RedisURI.builder.withHost(ip).withPort(port.toInt).build()
    }).toList

    val client = RedisClusterClient.create(uris.asJava)
    val options = ClusterTopologyRefreshOptions.builder
      .enablePeriodicRefresh(Duration.ofMinutes(10))
      .enableAllAdaptiveRefreshTriggers()
      .build
    client.setOptions(ClusterClientOptions
      .builder
      .autoReconnect(true)
      .pingBeforeActivateConnection(true)
      .topologyRefreshOptions(options)
      .build)
    client.connect()
  }
}
