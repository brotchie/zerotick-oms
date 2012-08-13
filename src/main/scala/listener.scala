/*   ____  __  __ ___                                           *\
**  / _//\|  \/  / __|   zerotick-oms - The Minimalist Order    **
** | (//) | |\/| \__ \                  Management System       **
**  \//__/|_|  |_|___/   Copyright (c) 2012, James Brotchie     **
\*                       http://zerotick.org/                   */

package zerotick.ib

import java.io.ByteArrayInputStream

import com.ib.client

import akka.actor._
import akka.zeromq._

object TWSListener {
  sealed trait State
  /** Listener is idle. Not IB API messages will be forwarded. */
  case object Idle extends State
  /** Listener has a valid recipient and is forwarding IB API messages. */
  case object Forwarding extends State

  sealed trait Data
  /** Listener is uninitialized. */
  case object Uninitialized extends Data
  /** Listener decodes ZeroMQ messages with the EReader object. */
  case class Reader(reader: EReader) extends Data

  /** Sets the recipient actor who will receive decoded IB API messages. */
  case class SetRecipient(recipient: ActorRef)
  /** Removes any existing recipient, reutrning listener state to idle. */
  case object RemoveRecipient 
}

/** An actor that decodes ZeroMQ delivered
  * IB API messages and forwards them to a recipient
  * actor.
  */
class TWSListener() extends Actor 
                    with FSM[TWSListener.State, TWSListener.Data]{

  import TWSListener._

  startWith(Idle, Uninitialized)

  when(Idle) {
    case Event(SetRecipient(recipient), Uninitialized) => {
      val wrapper       = new EWrapper(recipient)
      val clientSocket  = new client.EClientSocket(wrapper)
      val reader        = new EReader(clientSocket)

      goto(Forwarding) using Reader(reader)
    }

    // These events are ignored when in Idle state.
    case Event( RemoveRecipient
              | Connecting
              | IBMessage(_)
              , _) => stay
  }

  when(Forwarding) {
    case Event(IBMessage(msg) , Reader(reader)) => { reader.processMsg(msg); stay }
    case Event(RemoveRecipient, _)              => goto(Idle) using Uninitialized
    case Event(Connecting     , _)              => stay
  }

  initialize
}

/** Extractor for IB API ZeroMQ messages. */
object IBMessage {
  def apply(payload: Array[Byte]) = ZMQMessage(payload)

  /** Extracts the first frame from a ZMQMessage as Array[Byte]. */
  def unapply(msg: ZMQMessage): Option[Array[Byte]] =
    if (msg.frames.length == 1) Some(msg.payload(0)) else None
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


