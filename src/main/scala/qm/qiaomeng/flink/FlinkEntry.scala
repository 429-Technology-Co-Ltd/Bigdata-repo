package qm.qiaomeng.flink

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
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
    // consumer.setStartFromEarliest()
    //添加源并设置并行度
    val ds: DataStream[String] = env.addSource(consumer)

    // 处理入口
    ds.map(x => {
      // 数据处理流程
      Analysis.analysis(x)

      x
    })
    env.execute("PriceComparison")
  }
}
