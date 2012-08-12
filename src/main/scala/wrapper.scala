package zerotick.ib

import com.ib.client
import akka.actor.ActorRef

import zerotick.ib.message._

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
