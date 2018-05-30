package caches

import com.twitter.util.Future
import domain.User

class MockUsersCache(userOpt: Option[User]) extends UsersCache {

  def getUser(id: String): Future[Option[User]] = {
    Future{ userOpt }
  }

  def setUser(user: User): Future[Long] = {
    Future { 1 }
  }
}
