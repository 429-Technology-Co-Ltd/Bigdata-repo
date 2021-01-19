package qm.qiaomeng.flink

import java.net.InetSocketAddress
import java.util

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisClusterConfig
import qm.qiaomeng.kafka.KafkaConsumer


/**
 * @ClassName: Flink
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/1/5 9:54
 */
object FlinkEntry {
  def entry(): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //对接kafka
    val consumer: FlinkKafkaConsumer[String] = KafkaConsumer.getConsumer

    //从头开始消费
    consumer.setStartFromEarliest()

    val ds: DataStream[String] = env.addSource(consumer)

    // 处理入口
    ds.map(x => {
      // 数据处理流程
      val results = Analysis.analysis(x)

      // 拼接字符串，进入后续流程 供测试使用
      results
    })
      .map(x => (x._1._1,x._1._2, x._2))
      .executeAndCollect()
      .foreach(println)

    //对接redis
    val addressSet: java.util.Set[InetSocketAddress] = new util.HashSet[InetSocketAddress]()
    addressSet.add(new InetSocketAddress("8.135.22.177", 6379))
    addressSet.add(new InetSocketAddress("8.135.53.75", 6379))
    addressSet.add(new InetSocketAddress("8.135.58.206", 6379))
    addressSet.add(new InetSocketAddress("8.135.22.177", 6380))
    addressSet.add(new InetSocketAddress("8.135.53.75", 6380))
    addressSet.add(new InetSocketAddress("8.135.58.206", 6380))

    val redisConfig = new FlinkJedisClusterConfig.Builder()
      .setNodes(addressSet)
      .setMinIdle(5)
      .setMaxIdle(10)
      .build

    //    ds.addSink(new RedisSink[String](redisConfig, GoodsRedisMapper))
    //
    //    env.execute("Kafka-Flink-Redis")
  }
}
