/*   ____  __  __ ___                                           *\
**  / _//\|  \/  / __|   zerotick-oms - The Minimalist Order    **
** | (//) | |\/| \__ \                  Management System       **
**  \//__/|_|  |_|___/   Copyright (c) 2012, James Brotchie     **
\*                       http://zerotick.org/                   */

package zerotick

import akka.actor._
import akka.zeromq._

import zerotick.ib.message._
import zerotick.ib._

class MessagePrinter extends Actor {

  /* As an example we're only interested in
   * tick prices and tick sizes. */
  def receive = {
    case TickPrice(id, field, price, _) =>
      printf("Tick %d %d %f\n", id, field, price)
    case TickSize(id, field, size) =>
      printf("Size %d %d %d\n", id, field, size)
  }
}

object Main extends App {
  val system = ActorSystem("Main")
  val endpoint:String = "ipc:///var/tmp/ibtws/broadcast"

  // Initially send IB API messages to an actor that
  // prints them to stdout.
  val printer = system.actorOf(Props[MessagePrinter])

  // The TWSListener actor accepts ZeroMQ messages containing
  // IB API messages, parses them, then sends the parsed
  // messages to the given actor.
  val listener = system.actorOf(Props(new TWSListener(printer)))

  // Connect to the ib-zmq proxy broadcast endpoint.
  val subsocket = ZeroMQExtension(system).newSocket( SocketType.Sub
                                                   , Listener(listener)
                                                   , Connect(endpoint)
                                                   , SubscribeAll )
}
