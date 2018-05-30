package caches

import com.twitter.finagle.redis.Client
import com.twitter.finagle.redis.util.{BufToString, StringToBuf}
import com.twitter.util.Future
import domain.User
import spray.json.JsonParser
import spray.json._

class RedisUsersCache(redisClient: Client) extends UsersCache {

  private def userIdToKeys(id: String) = {
    Tuple2((id.toInt / 1000).toString, id)
  }

  def getUser(id: String): Future[Option[User]] = {
    import domain.JsonProtocol._

    val x = userIdToKeys(id)
    val key = StringToBuf(x._1)
    val field = StringToBuf(x._2)

    redisClient.hGet(key, field).map(x => {
      if(x.isDefined) {
        Some(JsonParser(BufToString(x.get)).convertTo[User])
      } else {
        None
      }
    })
  }

  def setUser(user: User): Future[Long] = {
    import domain.JsonProtocol._

    val x = userIdToKeys(user.id.toString)
    val key = StringToBuf(x._1)
    val field = StringToBuf(x._2)
    val value = StringToBuf(user.toJson.toString())

    redisClient.hSet(key, field, value).map(x => x.toLong)
  }

}
