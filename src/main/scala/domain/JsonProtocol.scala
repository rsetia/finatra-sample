package domain

import spray.json.DefaultJsonProtocol

object JsonProtocol extends DefaultJsonProtocol {
  implicit def userJsonFormat = jsonFormat(User, "id", "username")
  implicit def tweetJsonFormat = jsonFormat(Tweet, "id", "text")
  implicit def userTweetsJsonFormat = jsonFormat(UserAndTweets, "user", "tweets")
}
