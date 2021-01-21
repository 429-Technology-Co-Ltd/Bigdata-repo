package qm.qiaomeng.redis

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection

/**
 * @ClassName: RedisUtils
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/1/12 15:39
 */
object RedisUtils {

  /**
   * 往redis中写入一条数据并设置过期时间
   * @param key 存入的key
   * @param value 存入的值
   * @param milliseconds 过期的毫秒值
   * @return 返回存入信息
   */
  def writeToRedis(key: String, value: String, milliseconds: Long): String = {
    val client: StatefulRedisClusterConnection[String, String] = RedisClient.getRedisClient

    // 同步存入redis
    val reply = client.sync.psetex(key, milliseconds, value)
    reply
  }
}
