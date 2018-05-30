package services

import caches.UsersCache
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future
import domain.User
import javax.inject.Inject
import spray.json.JsonParser

class UsersService @Inject() (usersCache: UsersCache, client: Service[Request, Response]) {
  import domain.JsonProtocol._

  def getUser(id: Int): Future[Option[User]] = {
    usersCache.getUser(id.toString).flatMap(userOpt => {
      if(userOpt.isDefined) {
        Future { userOpt }
      } else {
        val req = Request(s"/users/$id")
        for {
          res <- client(req)
        } yield {
          if (res.status == Status.NotFound) {
            None
          } else {
            val user = JsonParser(res.contentString).convertTo[User]
            usersCache.setUser(user)
            Some(user)
          }
        }
      }
    })
  }
}
