package qm.qiaomeng.kafka

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig

/**
 * @ClassName: KafkaConsumer
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/1/13 14:54
 */
object KafkaConsumer {
  def getConsumer: FlinkKafkaConsumer[String] = {
    val properties = new Properties()
    // 对接kafka
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "8.135.22.177:9092")
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "goods-group")
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")

    val topic = "app"
    new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), properties)
  }
}
