/*   ____  __  __ ___                                           *\
**  / _//\|  \/  / __|   zerotick-oms - The Minimalist Order    **
** | (//) | |\/| \__ \                  Management System       **
**  \//__/|_|  |_|___/   Copyright (c) 2012, James Brotchie     **
\*                       http://zerotick.org/                   */

import akka.actor._

object OMS {
  trait State
  case class Unsubmitted() extends State
  case class Submitted() extends State
  case class Completed() extends State
  case class Cancelled() extends State

  trait Action
  case class Submit() extends Action
  case class Complete() extends Action
  case class Cancel() extends Action

  case class Data()
}


class OMS extends Actor with FSM[OMS.State, OMS.Data] {
  import OMS._

  startWith(Unsubmitted(), Data())

  when(Unsubmitted()) {
    case Event(Submit(), _) => goto(Submitted())
  }

  when(Submitted()) {
    case Event(Complete(), _) => goto(Completed())
    case Event(Cancel(), _) => goto(Cancelled())
  }

  when(Completed()) {
    case _ => {println("Completed"); stay()}
  }

  when(Cancelled()) {
    case _ => {println("Cancelled"); stay()}
  }

  initialize
}
