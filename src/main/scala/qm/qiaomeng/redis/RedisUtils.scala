package qm.qiaomeng.redis

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection

/**
 * @ClassName: RedisUtils
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/1/12 15:39
 */
object RedisUtils {
  def writeToRedis(key: String, value: String): String = {
    val client: StatefulRedisClusterConnection[String, String] = RedisClient.getRedisClient

    // 同步存入redis
    val reply = client.sync.set(key, value)
    reply
  }
}
