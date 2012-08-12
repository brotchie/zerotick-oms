package zerotick.ib

import java.io.ByteArrayInputStream

import com.ib.client

import akka.actor._
import akka.zeromq._

/** An actor that decodes ZeroMQ delivered
  * IB API messages and forwards them to a recipient
  * actor.
  *
  * @constructor creates a new listener with target recipient.
  * @param recipient the actor to receive IB API messages.
  */
class TWSListener(recipient: ActorRef) extends Actor {
  val clientSocket = new client.EClientSocket(new EWrapper(recipient))
  val reader = new EReader(clientSocket)

  def receive = {
    case message: ZMQMessage => reader.processMsg(message.payload(0))
  }
}


class EReader( socket: client.EClientSocket
             , stream: io.ReplaceableDataInputStream = new io.ReplaceableDataInputStream())
             extends client.EReader(socket, stream) {

  def processMsg(bytes: Array[Byte]) = {
    stream.replace(new ByteArrayInputStream(bytes))
    val msgId: Int = readInt()
    super.processMsg(msgId)
  }

}


