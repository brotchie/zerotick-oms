package zerotick.ib.orderstatus

object OrderStatusValue {
  /** Implicit conversion from String to OrderStatusValue. */
  implicit def string2status(s: String): OrderStatusValue = s match {
    case "PendingSubmit" => PendingSubmit()
    case "PendingCancel" => PendingCancel()
    case "PreSubmitted"  => PreSubmitted()
    case "Submitted"     => Submitted()
    case "Cancelled"     => Cancelled()
    case "Filled"        => Filled()
    case "Inactive"      => Inactive()
    case status          => Unknown(status)
  }
}

/** Base class for all order status values. */
abstract class OrderStatusValue

/** Indicates that you have transmitted the order, but have
  * not yet received confirmation that it has been accepted
  * by the order destination.
  */
case class PendingSubmit() extends OrderStatusValue

/** Indicates that you have sent a request to cancel the order
  * but have not yet received cancel confirmation from the order
  * destination. At this point, your order is not confirmed
  * canceled. You may still receive an execution while your
  * cancellation request is pending. PendingSubmit and PendingCancel
  * order statuses are not sent by the system and should be
  * explicitly set by the API developer when an order is canceled.
  */
case class PendingCancel() extends OrderStatusValue

/** Indicates that a simulated order type has been accepted by
  * the system and that this order has yet to be elected. The
  * order is held in the system until the election criteria are
  * met. At that time the order is transmitted to the order
  * destination as specified.
  */
case class PreSubmitted() extends OrderStatusValue

/** Indicates that your order has been accepted at the
  * order destination and is working.
  */
case class Submitted() extends OrderStatusValue

/** Indicates that the balance of your order has been confirmed
  * canceled by the system. This could occur unexpectedly when
  * the destination has rejected your order.
  */
case class Cancelled() extends OrderStatusValue

/** Indicates that he order has been completely filled.
  */
case class Filled() extends OrderStatusValue

/** Indicates that the order has been accepted by the system
  * (simulated orders) or an exchange (native orders) but that
  * currently the order is inactive due to system, exchange
  * or other issues.
  */
case class Inactive() extends OrderStatusValue

/** An unrecognized unhandled order status. */
case class Unknown(status: String) extends OrderStatusValue
