import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http
import com.twitter.util.{Await, Future}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.http.service.RoutingService

object Server extends App {
	def usersService: Service[Request, Response] = Http.client.newService("localhost:8081")

	val routingService = RoutingService.byPath { case "/users/1" => usersService }
	
	val server = Http.serve(":8080", routingService)
	Await.ready(server)
}
