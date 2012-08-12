package zerotick.ib

import com.ib.client

import java.io.{ ByteArrayInputStream
               , InputStream
               , DataInputStream
               , FilterInputStream }

import akka.actor._
import akka.zeromq._

package util {
  class ReplaceableDataInputStream() extends DataInputStream(null) {
    var instream: InputStream = null
    def replace(stream: InputStream) { in = stream }
  }
}

package message {
  abstract class IncomingMessage()

  case class TickPrice(tickerId: Int, field: Int, price: Double, canAutoExecute: Int) extends IncomingMessage
  case class TickSize(tickerId: Int, field: Int, size: Int) extends IncomingMessage
  case class TickOptionComputation(tickerId: Int, field: Int, impliedVol: Double,
              delta: Double, optPrice: Double, pvDividend: Double,
              gamma: Double, vega: Double, theta: Double, undPrice: Double) extends IncomingMessage
  case class TickGeneric(tickerId: Int, tickType: Int, value: Double) extends IncomingMessage
  case class TickString(tickerId: Int, tickType: Int, value: String) extends IncomingMessage
  case class TickEFP(tickerId: Int, tickType: Int, basisPoInt: Double,
              formattedBasisPoints: String, impliedFuture: Double, holdDays: Int,
              futureExpiry: String, dividendImpact: Double, dividendsToExpiry: Double) extends IncomingMessage
  case class OrderStatus(orderId: Int, status: String, filled: Int, remaining: Int,
              avgFillPrice: Double, permId: Int, parentId: Int, lastFillPrice: Double,
              clientId: Int, whyHeld: String) extends IncomingMessage
  case class OpenOrder(orderId: Int, contract: client.Contract, order: client.Order, orderState: client.OrderState) extends IncomingMessage
  case class OpenOrderEnd() extends IncomingMessage
  case class UpdateAccountValue(key: String, value: String, currency: String, accountName: String) extends IncomingMessage
  case class UpdatePortfolio(contract: client.Contract, position: Int, marketPrice: Double, marketValue: Double,
              averageCost: Double, unrealizedPNL: Double, realizedPNL: Double, accountName: String) extends IncomingMessage
  case class UpdateAccountTime(timeStamp: String) extends IncomingMessage
  case class AccountDownloadEnd(accountName: String) extends IncomingMessage
  case class NextValidId(orderId: Int) extends IncomingMessage
  case class ContractDetails(reqId: Int, details: client.ContractDetails) extends IncomingMessage
  case class BondContractDetails(reqId: Int, contractDetails: client.ContractDetails) extends IncomingMessage
  case class ContractDetailsEnd(reqId: Int) extends IncomingMessage
  case class ExecDetails(reqId: Int, contract: client.Contract, execution: client.Execution) extends IncomingMessage
  case class ExecDetailsEnd(reqId: Int) extends IncomingMessage
  case class UpdateMktDepth(tickerId: Int, position: Int, operation: Int, side: Int, price: Double, size: Int) extends IncomingMessage
  case class UpdateMktDepthL2(tickerId: Int, position: Int, marketMaker: String, operation: Int,
              side: Int, price: Double, size: Int) extends IncomingMessage
  case class UpdateNewsBulletin(msgId: Int, msgType: Int, message: String, origExchange: String) extends IncomingMessage
  case class ManagedAccounts(accountsList: String) extends IncomingMessage
  case class ReceiveFA(faDataType: Int, xml: String) extends IncomingMessage
  case class HistoricalData(reqId: Int, date: String, open: Double, high: Double, low: Double,
                        close: Double, volume: Int, count: Int, WAP: Double, hasGaps: Boolean) extends IncomingMessage
  case class ScannerParameters(xml: String) extends IncomingMessage
  case class ScannerData(reqId: Int, rank: Int, contractDetails: client.ContractDetails, distance: String,
              benchmark: String, projection: String, legsStr: String) extends IncomingMessage
  case class ScannerDataEnd(reqId: Int) extends IncomingMessage
  case class RealtimeBar(reqId: Int, time: Long, open: Double, high: Double, low: Double, close: Double, volume: Long, wap: Double, count:
        Int) extends IncomingMessage
  case class CurrentTime(time: Long) extends IncomingMessage
  case class FundamentalData(reqId: Int, data: String) extends IncomingMessage
  case class DeltaNeutralValidation(reqId: Int, underComp: client.UnderComp) extends IncomingMessage
  case class TickSnapshotEnd(reqId: Int) extends IncomingMessage
  case class MarketDataType(reqId: Int, marketDataType: Int) extends IncomingMessage
  case class CommissionReport(commissionReport: client.CommissionReport) extends IncomingMessage
}

import message._

class EWrapper(recipient: ActorRef) extends client.EWrapper {
    def tickPrice(tickerId: Int, field: Int, price: Double, canAutoExecute: Int)
      {recipient ! TickPrice(tickerId, field, price, canAutoExecute)}

    def tickSize(tickerId: Int, field: Int, size: Int)
      {recipient ! TickSize(tickerId, field, size)}

    def tickOptionComputation(tickerId: Int, field: Int, impliedVol: Double,
    		delta: Double, optPrice: Double, pvDividend: Double,
    		gamma: Double, vega: Double, theta: Double, undPrice: Double)
      {recipient ! TickOptionComputation(tickerId, field, impliedVol, delta, optPrice, pvDividend,
                             gamma, vega, theta, undPrice)}

	def tickGeneric(tickerId: Int, tickType: Int, value: Double)
      {recipient ! TickGeneric(tickerId, tickType, value)}

	def tickString(tickerId: Int, tickType: Int, value: String)
      {recipient ! TickString(tickerId, tickType, value)}

	def tickEFP(tickerId: Int, tickType: Int, basisPoint: Double,
			formattedBasisPoints: String, impliedFuture: Double, holdDays: Int,
			futureExpiry: String, dividendImpact: Double, dividendsToExpiry: Double)
      {recipient ! TickEFP(tickerId, tickType, basisPoint, formattedBasisPoints, impliedFuture,
               holdDays, futureExpiry, dividendImpact, dividendsToExpiry)}

    def orderStatus(orderId: Int, status: String, filled: Int, remaining: Int,
            avgFillPrice: Double, permId: Int, parentId: Int, lastFillPrice: Double,
            clientId: Int, whyHeld: String)
     {recipient ! OrderStatus(orderId, status, filled, remaining, avgFillPrice, permId, parentId,
                  lastFillPrice, clientId, whyHeld)}

    def openOrder(orderId: Int, contract: client.Contract, order: client.Order, orderState: client.OrderState)
      {recipient ! OpenOrder(orderId, contract, order, orderState)}

    def openOrderEnd()
      {recipient ! OpenOrderEnd()}

    def updateAccountValue(key: String, value: String, currency: String, accountName: String)
      {recipient ! UpdateAccountValue(key, value, currency, accountName)}

    def updatePortfolio(contract: client.Contract, position: Int, marketPrice: Double, marketValue: Double,
            averageCost: Double, unrealizedPNL: Double, realizedPNL: Double, accountName: String)
      {recipient ! UpdatePortfolio(contract, position, marketPrice, marketValue, averageCost,
                       unrealizedPNL, realizedPNL, accountName)}

    def updateAccountTime(timeStamp: String)
      {recipient ! UpdateAccountTime(timeStamp)}

    def accountDownloadEnd(accountName: String)
      {recipient ! AccountDownloadEnd(accountName)}

    def nextValidId(orderId: Int)
      {recipient ! NextValidId(orderId)}

    def contractDetails(reqId: Int, details: client.ContractDetails)
      {recipient ! ContractDetails(reqId, details)}

    def bondContractDetails(reqId: Int, contractDetails: client.ContractDetails)
      {recipient ! BondContractDetails(reqId, contractDetails)}

    def contractDetailsEnd(reqId: Int)
      {recipient ! ContractDetailsEnd(reqId)}

    def execDetails(reqId: Int, contract: client.Contract, execution: client.Execution)
      {recipient ! ExecDetails(reqId, contract, execution)}

    def execDetailsEnd(reqId: Int)
      {recipient ! ExecDetailsEnd(reqId)}

    def updateMktDepth(tickerId: Int, position: Int, operation: Int, side: Int, price: Double, size: Int)
      {recipient ! UpdateMktDepth(tickerId, position, operation, side, price, size)}

    def updateMktDepthL2(tickerId: Int, position: Int, marketMaker: String, operation: Int,
    		side: Int, price: Double, size: Int)
      {recipient ! UpdateMktDepthL2(tickerId, position, marketMaker, operation, side, price, size)}

    def updateNewsBulletin(msgId: Int, msgType: Int, message: String, origExchange: String)
      {recipient ! UpdateNewsBulletin(msgId, msgType, message, origExchange)}

    def managedAccounts(accountsList: String)
      {recipient ! ManagedAccounts(accountsList)}

    def receiveFA(faDataType: Int, xml: String)
      {recipient ! ReceiveFA(faDataType, xml)}

    def historicalData(reqId: Int, date: String, open: Double, high: Double, low: Double,
                      close: Double, volume: Int, count: Int, WAP: Double, hasGaps: Boolean)
      {recipient ! HistoricalData(reqId, date, open, high, low, close, volume, count, WAP, hasGaps)}

    def scannerParameters(xml: String)
      {recipient ! ScannerParameters(xml)}

    def scannerData(reqId: Int, rank: Int, contractDetails: client.ContractDetails, distance: String,
    		benchmark: String, projection: String, legsStr: String)
      {recipient ! ScannerData(reqId, rank, contractDetails, distance, benchmark, projection, legsStr)}

    def scannerDataEnd(reqId: Int)
      {recipient ! ScannerDataEnd(reqId)}

    def realtimeBar(reqId: Int, time: Long, open: Double, high: Double, low: Double, close: Double, volume: Long, wap: Double, count:
      Int)
      {recipient ! RealtimeBar(reqId, time, open, high, low, close, volume, wap, count)}

    def currentTime(time: Long)
      {recipient ! CurrentTime(time)}

    def fundamentalData(reqId: Int, data: String)
      {recipient ! FundamentalData(reqId, data)}

    def deltaNeutralValidation(reqId: Int, underComp: client.UnderComp)
      {recipient ! DeltaNeutralValidation(reqId, underComp)}

    def tickSnapshotEnd(reqId: Int)
      {recipient ! TickSnapshotEnd(reqId)}

    def marketDataType(reqId: Int, marketDataType: Int)
      {recipient ! MarketDataType(reqId, marketDataType)}

    def commissionReport(commissionReport: client.CommissionReport)
      {recipient ! CommissionReport(commissionReport)}

    /* AnyWrapper Implementation, not used. */
    def error(e: Exception){}
    def error(str: String){}
    def error(id: Int, errorCode: Int, errorMsg: String){}
    def connectionClosed(){}

}

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
             , stream: util.ReplaceableDataInputStream = new util.ReplaceableDataInputStream())
             extends client.EReader(socket, stream) {

  def processMsg(bytes: Array[Byte]) = {
    stream.replace(new ByteArrayInputStream(bytes))
    val msgId: Int = readInt()
    super.processMsg(msgId)
  }

}


