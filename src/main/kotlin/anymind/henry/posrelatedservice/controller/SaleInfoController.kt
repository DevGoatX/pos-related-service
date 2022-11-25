package anymind.henry.posrelatedservice.controller

import anymind.henry.posrelatedservice.extension.ErrorList
import anymind.henry.posrelatedservice.model.SaleInfoPostRequest
import anymind.henry.posrelatedservice.paymentmethod.*
import anymind.henry.posrelatedservice.persistance.model.SaleInfo
import anymind.henry.posrelatedservice.service.SaleInfoService
import anymind.henry.posrelatedservice.unittests.validation.Messages
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult
import java.lang.Exception

const val SUCCESS_KEY = "success"
const val ERROR_LIST_KEY = "errList"
const val FINAL_PRICE_KEY = "final_price"
const val POINTS_KEY = "points"
const val RESULT_KEY = "result"
const val DATETIME_REGEXP_STRING = "^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:Z|[+-][01]\\d:[0-5]\\d)\$"

@Controller
class SaleInfoController {
    @Autowired
    private lateinit var saleInfoService: SaleInfoService

    @PostMapping("/save-sale")
    @ResponseBody
    fun saveSaleInfo(@RequestBody body: SaleInfoPostRequest): DeferredResult<ResponseEntity<*>> {

        val result = DeferredResult<ResponseEntity<*>>()
        val errList = ErrorList()

        // check basic validation
        var error = ""

        if (body.customer_id.isEmpty()) {
            error = Messages.NO_CUSTOMER_ID
        }

        if (error.isEmpty() && body.price == 0f) {
            error = Messages.INVALID_PRICE
        }

        var paymentMethod: PaymentMethod? = null

        if (error.isEmpty()) {
            if (body.payment_method.isEmpty()) {
                error = Messages.NO_PAYMENT_METHOD
            } else {
                when (body.payment_method) {
                    PaymentMethod.AMEX -> paymentMethod = Amex()
                    PaymentMethod.BANK_TRANSFER -> paymentMethod = BankTransfer()
                    PaymentMethod.CASH -> paymentMethod = Cash()
                    PaymentMethod.CASH_ON_DELIVERY -> paymentMethod = CashOnDelivery()
                    PaymentMethod.CHEQUE -> paymentMethod = Cheque()
                    PaymentMethod.GRAB_PAY -> paymentMethod = GrabPay()
                    PaymentMethod.JCB -> paymentMethod = Jcb()
                    PaymentMethod.LINE_PAY -> paymentMethod = LinePay()
                    PaymentMethod.MASTERCARD -> paymentMethod = Mastercard()
                    PaymentMethod.PAYPAY -> paymentMethod = PayPay()
                    PaymentMethod.POINTS, PaymentMethod.VISA -> paymentMethod = Visa()
                    else -> error = Messages.INVALID_PAYMENT_METHOD
                }
            }
        }

        if (error.isEmpty()) {
            if (body.datetime.isEmpty())
                error = Messages.NO_DATE_TIME
            else if (!DATETIME_REGEXP_STRING.toRegex().matches(body.datetime))
                error = Messages.INVALID_DATE_TIME
        }

        val resBody = HashMap<String, Any>()
        resBody[SUCCESS_KEY] = false

        if (error.isNotEmpty()) {
            errList.add(error)
            resBody[ERROR_LIST_KEY] = errList
            result.setErrorResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody))
            return result
        }

        try {
            // check payment validation
            val errList = paymentMethod!!.checkValidation(body.price_modifier, body.additional_item)
            if (errList.isNotEmpty()) {
                resBody[ERROR_LIST_KEY] = errList
                result.setErrorResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody))
                return result
            }

            // save sale information
            val saleInfo = SaleInfo()
            saleInfo.customer_id = body.customer_id
            saleInfo.payment_method = body.payment_method
            saleInfo.sales = body.price * body.price_modifier
            saleInfo.points = body.price * paymentMethod.pointsRate
            saleInfo.datetime = body.datetime
            if (!body.additional_item.isNullOrEmpty())
                saleInfo.additional_item = JSONObject(body.additional_item).toString()

            saleInfoService.save(saleInfo)

        }  catch (e: Exception) {
            errList.add(e.message ?: "Error is occurred")
            resBody[ERROR_LIST_KEY] = errList
            result.setErrorResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody))
            return result
        }

        resBody[SUCCESS_KEY] = true

        val calcBody = HashMap<String, Any>()
        calcBody[FINAL_PRICE_KEY] = body.price * body.price_modifier
        calcBody[POINTS_KEY] = body.price * paymentMethod.pointsRate

        resBody[RESULT_KEY] = calcBody
        result.setResult(ResponseEntity.ok(resBody))
        return result
    }

    @GetMapping("/all-sales")
    @ResponseBody
    fun getAllSales(): DeferredResult<ResponseEntity<*>> {
        val result = DeferredResult<ResponseEntity<*>>()

        val resBody = HashMap<String, Any>()
        resBody[SUCCESS_KEY] = false
        try {
            val salesList = saleInfoService.findAll()
            resBody[RESULT_KEY] = salesList

        } catch (e: Exception) {
            val errList = ErrorList()
            errList.add(e.message ?: "Error is occurred")
            resBody[ERROR_LIST_KEY] = errList
            result.setErrorResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody))
            return result
        }

        resBody[SUCCESS_KEY] = true
        result.setResult(ResponseEntity.ok(resBody))
        return result
    }

    @GetMapping("/sales-by-customer")
    @ResponseBody
    fun getSalesByCustomerId(@RequestParam(value = "id") id: String): DeferredResult<ResponseEntity<*>> {
        val result = DeferredResult<ResponseEntity<*>>()

        val resBody = HashMap<String, Any>()
        resBody[SUCCESS_KEY] = false
        try {
            val salesList = saleInfoService.findByCustomerId(id)
            resBody[RESULT_KEY] = salesList

        } catch (e: Exception) {
            val errList = ErrorList()
            errList.add(e.message ?: "Error is occurred")
            resBody[ERROR_LIST_KEY] = errList
            result.setErrorResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody))
            return result
        }

        resBody[SUCCESS_KEY] = true
        result.setResult(ResponseEntity.ok(resBody))
        return result
    }

    @GetMapping("/sales-by-time-range")
    @ResponseBody
    fun getSaleInfoByTimeRange(@RequestParam(value = "start_time") start_time: String, @RequestParam(value = "end_time") end_time: String): DeferredResult<ResponseEntity<*>> {
        val result = DeferredResult<ResponseEntity<*>>()

        val resBody = HashMap<String, Any>()
        resBody[SUCCESS_KEY] = false
        try {
            val salesList = saleInfoService.findAllByDatetimeBetween(start_time, end_time)
            resBody[RESULT_KEY] = salesList

        } catch (e: Exception) {
            val errList = ErrorList()
            errList.add(e.message ?: "Error is occurred")
            resBody[ERROR_LIST_KEY] = errList
            result.setErrorResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resBody))
            return result
        }

        resBody[SUCCESS_KEY] = true
        result.setResult(ResponseEntity.ok(resBody))
        return result
    }

}