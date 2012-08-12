package zerotick

import akka.actor._
import akka.zeromq._

import zerotick.ib.message._
import zerotick.ib._

class MessagePrinter extends Actor {
  def receive = {
    case message: IncomingMessage => println(message)
  }
}

object Main extends App {
  val system = ActorSystem("Main")

  val printer = system.actorOf(Props[MessagePrinter])
  val listener = system.actorOf(Props(new TWSListener(printer)))
  val subsocket = ZeroMQExtension(system).newSocket(SocketType.Sub, Listener(listener), Connect("ipc:///var/tmp/ibtws/broadcast"),
    SubscribeAll)
}
