package anymind.henry.posrelatedservice.controller

import anymind.henry.posrelatedservice.extension.ErrorList
import anymind.henry.posrelatedservice.model.MutationResponse
import anymind.henry.posrelatedservice.model.SaleInfoGraphQLRequest
import anymind.henry.posrelatedservice.paymentmethod.*
import anymind.henry.posrelatedservice.persistance.model.SaleInfo
import anymind.henry.posrelatedservice.service.SaleInfoService
import anymind.henry.posrelatedservice.unittests.validation.Messages
import com.google.gson.GsonBuilder
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.lang.Exception

const val ERROR_SEPERATOR = ". "

@Controller
class GraphQLController {
    @Autowired
    private lateinit var saleInfoService: SaleInfoService

    @QueryMapping
    fun allSales(): List<SaleInfo> {
        val result = saleInfoService.findAll()
        return result
    }

    @QueryMapping
    fun salesByTimeRange(@Argument start_time: String, @Argument end_time: String): List<SaleInfo> {
        val result = saleInfoService.findAllByDatetimeBetween(start_time, end_time)
        return result
    }

    @MutationMapping
    fun saveSaleInfo(@Argument input: SaleInfoGraphQLRequest): MutationResponse {
        val rt = MutationResponse()
        val errList = ErrorList()

        val GSON_MAPPER = GsonBuilder().serializeNulls().create()
        var additionalItem: Map<String, Any>? = null
        if (input.additional_item != null) {
            additionalItem = GSON_MAPPER.fromJson(GSON_MAPPER.toJson(input.additional_item!!), Map::class.java) as Map<String, Any>
        }

        // check basic validation
        var error = ""

        if (input.customer_id.isEmpty()) {
            error = Messages.NO_CUSTOMER_ID
        }

        if (error.isEmpty() && input.price == 0f) {
            error = Messages.INVALID_PRICE
        }

        var paymentMethod: PaymentMethod? = null

        if (error.isEmpty()) {
            if (input.payment_method.isEmpty()) {
                error = Messages.NO_PAYMENT_METHOD
            } else {
                when (input.payment_method) {
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
            if (input.datetime.isEmpty())
                error = Messages.NO_DATE_TIME
            else if (!DATETIME_REGEXP_STRING.toRegex().matches(input.datetime))
                error = Messages.INVALID_DATE_TIME
        }

        if (error.isNotEmpty()) {
            errList.add(error)
            rt.errors = errList.joinToString(separator = ERROR_SEPERATOR);
            return rt
        }

        try {
            // check payment validation
            val errList = paymentMethod!!.checkValidation(input.price_modifier, additionalItem)
            if (errList.isNotEmpty()) {
                rt.errors = errList.joinToString(separator = ERROR_SEPERATOR);
                return rt
            }

            // save sale information
            val saleInfo = SaleInfo()
            saleInfo.customer_id = input.customer_id
            saleInfo.payment_method = input.payment_method
            saleInfo.sales = input.price * input.price_modifier
            saleInfo.points = input.price * paymentMethod.pointsRate
            saleInfo.datetime = input.datetime
            if (!additionalItem.isNullOrEmpty())
                saleInfo.additional_item = JSONObject(additionalItem).toString()

            saleInfoService.save(saleInfo)

        }  catch (e: Exception) {
            errList.add(e.message ?: "Error is occurred")
            rt.errors = errList.joinToString(separator = ERROR_SEPERATOR);
            return rt
        }

        rt.final_price = input.price * input.price_modifier
        rt.points = input.price * paymentMethod.pointsRate
        return rt
    }
}