package qm.qiaomeng.MD5

import java.io.{BufferedReader, InputStreamReader}
import java.security.MessageDigest
import java.util.Base64

/**
 * @ClassName: MD5
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/1/15 13:49
 */
object MD5 {

  def encode(line: String): String = {
    //md5加密
    val md5: MessageDigest = MessageDigest.getInstance("MD5")
    md5.update(line.getBytes("UTF8"))
    val md5Bytes: Array[Byte] = md5.digest
    //sha1加密
    val sha1 = MessageDigest.getInstance("SHA-1")
    sha1.update(md5Bytes)
    val sha1Bytes = sha1.digest
    //Base64编码
    val encoder = Base64.getEncoder
    encoder.encodeToString(sha1Bytes)
  }

  def decode(line: String): String ={
    //Base64解码
    val decoder = Base64.getDecoder
    decoder.decode(line).mkString
    //sha1解密
    //MD5解密

  }

  def main(args: Array[String]): Unit = {
    val reader = new BufferedReader(new InputStreamReader(System.in))
    while (true) {
      print("请输入要加密的字符串:")
      val line = reader.readLine()
      //加密
      val md5Str: String = encode(line)
      println(md5Str)
      println(decode(md5Str))
    }
  }
}
