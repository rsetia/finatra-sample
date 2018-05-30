package services

import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future
import domain.Tweet
import javax.inject.Inject
import spray.json.JsonParser

class TweetsService @Inject() (client: Service[Request, Response]) {
  import domain.JsonProtocol._

  def getLatestTweets(userId: Int): Future[Seq[Tweet]] = {
    val req = Request(s"/tweets/latest/$userId")

    client(req).map(x => {
      if (x.status == Status.Ok) {
        JsonParser(x.contentString).convertTo[Seq[Tweet]]
      } else {
        Seq()
      }
    })
  }
}

