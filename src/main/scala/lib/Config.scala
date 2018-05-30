package lib

import com.typesafe.config.ConfigFactory

object Config {
  val conf = ConfigFactory.load()

  object Services {
    def usersServiceDest: String = {
      conf.getString("services.users-service-dest")
    }

    def tweetsServiceDest: String = {
      conf.getString("services.tweets-service-dest")
    }
  }

  object Caches {
    def usersCacheDest: String = {
      conf.getString("caches.users-cache-dest")
    }
  }
}
