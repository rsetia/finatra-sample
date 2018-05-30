import caches.{UsersCache, RedisUsersCache}
import com.google.inject.Provides
import com.twitter.finagle.{Http, Redis, Service}
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.http.{Controller, HttpServer}
import com.twitter.inject.TwitterModule
import domain.UserAndTweets
import javax.inject.Inject
import lib.Config
import services.{TweetsService, UsersService}
import spray.json._

class MainController @Inject()(
                              usersService : UsersService,
                              tweetsService: TweetsService,
                              ) extends Controller {

  get("/:id") { request: Request =>
    import domain.JsonProtocol._

    val id = request.params("id").toInt
    for {
      user <- usersService.getUser(id)
      tweets <- tweetsService.getLatestTweets(id)
    } yield {
      val res = Response()
      if(user.isDefined) {
        val userAndTweets = UserAndTweets(user.get, tweets)
        res.contentString_=(userAndTweets.toJson.toString())
        res
      } else {
        res.status_=(Status.NotFound)
        res
      }
    }
  }
}

object DependenciesModule extends TwitterModule {
  private val usersClient = Http.client.newService(Config.Services.usersServiceDest)
  private val tweetsClient = Http.client.newService(Config.Services.tweetsServiceDest)


  private val redisClient = Redis.newRichClient(Config.Caches.usersCacheDest)
  private val usersCache = new RedisUsersCache(redisClient)

  @Provides
  def providesUserService: UsersService = { new UsersService(usersCache, usersClient) }

  @Provides
  def providesTweetsService: TweetsService = { new TweetsService(tweetsClient) }
}

object FinatraServerMain extends FinatraServer

class FinatraServer extends HttpServer {
  override val modules = Seq(DependenciesModule)

  override def configureHttp(router: HttpRouter): Unit = {
    router.
      add[MainController]
  }
}