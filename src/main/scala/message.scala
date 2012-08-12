package zerotick.ib.message

import com.ib.client
import zerotick.ib.orderstatus.OrderStatusValue

abstract class IncomingMessage

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
case class OrderStatus(orderId: Int, status: OrderStatusValue, filled: Int, remaining: Int,
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
