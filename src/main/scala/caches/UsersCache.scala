package caches

import com.twitter.util.Future
import domain.User

trait UsersCache {
  def getUser(id: String): Future[Option[User]]
  def setUser(user: User): Future[Long]
}