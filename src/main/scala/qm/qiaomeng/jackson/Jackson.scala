package qm.qiaomeng.jackson

import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.util
import scala.util.{Failure, Success, Try}

/**
 * @ClassName: Jackson
 * @Description: TODO
 * @Create by: LinYoung
 * @Date: 2021/1/15 19:25
 */
object Jackson {
  private val mapper = new ObjectMapper

  mapper.registerModule(DefaultScalaModule)

  /**
   *
   * @param value
   * @return
   */
  def bean2String(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  /**
   * 解析一级key
   *
   * @param line       要解析的json字符串
   * @param resultsMap 结果map
   * @return 返回结果
   */
  def autoParseJsonFirst(line: String): util.HashMap[String, Any] = {
    val map: util.HashMap[String, Any] = new util.HashMap[String, Any]()
    Try {
      val node = mapper.readTree(line)
      val keys = node.fieldNames
      while (keys.hasNext) {
        val key = keys.next
        map.put(key, node.get(key))
      }
    } match {
      case Success(value) => map
      case Failure(exception) => map
    }
  }

  /**
   * 自动解析json串，递归处理json套json的字符串
   *
   * @param line       要处理的json字符串
   * @param resultsMap 放返回结果的map
   * @return 返回的结果
   */
  def autoParseJson(line: String, resultsMap: util.HashMap[String, Any]): util.HashMap[String, Any] = {
    Try {
      //json树
      val jsonNode = mapper.readTree(line)
      //获取节点名称
      val keys = jsonNode.fieldNames

      //循环读取节点信息
      while (keys.hasNext) {
        //取出节点的key
        val key = keys.next

        //取出子节点
        val tmpNode = jsonNode.get(key)

        //判断子节点
        if (tmpNode.isObject) {
          //json对象
          autoParseJson(tmpNode.toString, resultsMap)
        } else if (tmpNode.isArray) {
          //json数组
          resultsMap.put(key, tmpNode)
        } else {
          //其他类型
          if (tmpNode.isTextual) {
            resultsMap.put(key, tmpNode.asText)
          } else {
            resultsMap.put(key, tmpNode)
          }
        }
      }
    } match {

      //发生异常的地方在readTree函数，如果发生异常，resultsMap
      case Success(value) => resultsMap //返回结果
      //      case Failure(exception) => new util.HashMap[String, Any] //返回空map
      case Failure(exception) => resultsMap
    }
  }

  /**
   *
   * @param line
   * @return
   */
  def string2Array(line: String): Array[AnyRef] = {
    JSON.parseArray(line).toArray
  }
}
