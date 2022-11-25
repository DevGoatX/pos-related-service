package anymind.henry.posrelatedservice.integrationtests

import anymind.henry.posrelatedservice.PosRelatedServiceApplication
import anymind.henry.posrelatedservice.model.SaleInfoGetResponse
import anymind.henry.posrelatedservice.model.SaleInfoPostRequest
import anymind.henry.posrelatedservice.model.SalePostResponse
import anymind.henry.posrelatedservice.paymentmethod.*
import anymind.henry.posrelatedservice.persistance.dao.SaleInfoRepository
import anymind.henry.posrelatedservice.persistance.model.SaleInfo
import com.graphql.spring.boot.test.GraphQLTest
import com.graphql.spring.boot.test.GraphQLTestTemplate
import graphql.Assert.assertNotNull
import graphql.Assert.assertNull
import junit.framework.Assert.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest(
    classes = arrayOf(PosRelatedServiceApplication::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(
    locations = arrayOf("classpath:application-integrationtest.properties")
)
class AppRestIntegrationTest {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var saleInfoRepository: SaleInfoRepository

    val validAdditonalItemForCard = HashMap<String, Any>()
    val validAdditonalItemForCashOnDelivery1 = HashMap<String, Any>()
    val validAdditonalItemForCashOnDelivery2 = HashMap<String, Any>()
    val validAdditonalItemForBankTransfer = HashMap<String, Any>()
    val validAdditonalItemForCheque = HashMap<String, Any>()

    val invalidAdditonalItem1 = HashMap<String, Any>()
    val invalidAdditonalItem2 = HashMap<String, Any>()
    val invalidAdditonalItemForCashOnDelivery = HashMap<String, Any>()
    val invalidAdditonalItemForCard1 = HashMap<String, Any>()
    val invalidAdditonalItemForCard2 = HashMap<String, Any>()
    val invalidAdditonalItemForCard3 = HashMap<String, Any>()

    val validReqForCash = SaleInfoPostRequest()
    val validReqForCashOnDelivery1 = SaleInfoPostRequest()
    val validReqForCashOnDelivery2 = SaleInfoPostRequest()
    val validReqForMastercard = SaleInfoPostRequest()
    val validReqForBankTransfer = SaleInfoPostRequest()
    val validReqForCheque = SaleInfoPostRequest()

    @BeforeAll
    fun prepareSalePostReqeust() {
        validAdditonalItemForCard[LAST_4_KEY] = "1234"

        validAdditonalItemForCashOnDelivery1[COURIER_SERVICE_KEY] = "YAMATO"

        validAdditonalItemForCashOnDelivery2[COURIER_SERVICE_KEY] = "SAGAWA"

        validAdditonalItemForBankTransfer[BANK_KEY] = "USA BANK"
        validAdditonalItemForBankTransfer[ACCOUNT_NUMBER_KEY] = "112222"

        validAdditonalItemForCheque[BANK_KEY] = "USA BANK"
        validAdditonalItemForCheque[CHEQUE_NUMBER_KEY] = "112222"

        invalidAdditonalItem2["xxx"] = "SAGAWA"
        invalidAdditonalItem2[ACCOUNT_NUMBER_KEY] = "112222"
        invalidAdditonalItem2[CHEQUE_NUMBER_KEY] = "112222"

        invalidAdditonalItemForCashOnDelivery[COURIER_SERVICE_KEY] = "YAMA"

        invalidAdditonalItemForCard1[LAST_4_KEY] = "123"
        invalidAdditonalItemForCard2[LAST_4_KEY] = "12345"
        invalidAdditonalItemForCard3[LAST_4_KEY] = "123a"

        validReqForCash.customer_id = "12345"
        validReqForCash.payment_method = PaymentMethod.CASH
        validReqForCash.price = 120f
        validReqForCash.price_modifier = 0.99f
        validReqForCash.datetime = "2022-10-20T00:00:00Z"

        validReqForCashOnDelivery1.customer_id = "12345"
        validReqForCashOnDelivery1.payment_method = PaymentMethod.CASH_ON_DELIVERY
        validReqForCashOnDelivery1.price = 120f
        validReqForCashOnDelivery1.price_modifier = 1.01f
        validReqForCashOnDelivery1.datetime = "2022-10-21T00:00:00Z"
        validReqForCashOnDelivery1.additional_item = validAdditonalItemForCashOnDelivery1

        validReqForCashOnDelivery2.customer_id = "12345"
        validReqForCashOnDelivery2.payment_method = PaymentMethod.CASH_ON_DELIVERY
        validReqForCashOnDelivery2.price = 120f
        validReqForCashOnDelivery2.price_modifier = 1f
        validReqForCashOnDelivery2.datetime = "2022-10-22T00:00:00Z"
        validReqForCashOnDelivery2.additional_item = validAdditonalItemForCashOnDelivery2

        validReqForMastercard.customer_id = "12345"
        validReqForMastercard.payment_method = PaymentMethod.MASTERCARD
        validReqForMastercard.price = 120f
        validReqForMastercard.price_modifier = 1f
        validReqForMastercard.datetime = "2022-10-23T00:00:00Z"
        validReqForMastercard.additional_item = validAdditonalItemForCard

        validReqForBankTransfer.customer_id = "12345"
        validReqForBankTransfer.payment_method = PaymentMethod.BANK_TRANSFER
        validReqForBankTransfer.price = 120f
        validReqForBankTransfer.price_modifier = 1f
        validReqForBankTransfer.datetime = "2022-10-24T00:00:00Z"
        validReqForBankTransfer.additional_item = validAdditonalItemForBankTransfer

        validReqForCheque.customer_id = "12345"
        validReqForCheque.payment_method = PaymentMethod.CHEQUE
        validReqForCheque.price = 120f
        validReqForCheque.price_modifier = 1f
        validReqForCheque.datetime = "2022-10-25T00:00:00Z"
        validReqForCheque.additional_item = validAdditonalItemForCheque
    }

    @Test
    fun whenGetCalled_thenShouldNotFound() {
        val result = restTemplate.getForEntity("/sales", SaleInfoGetResponse::class.java)

        assertNotNull(result)
        assertEquals(HttpStatus.NOT_FOUND, result?.statusCode)
    }

    @Test
    fun whenGetCalled_thenShouldBadRequest() {
        var result = restTemplate.getForEntity("/sales-by-customer", SaleInfoGetResponse::class.java)

        assertNotNull(result)
        assertEquals(HttpStatus.BAD_REQUEST, result?.statusCode)

        result = restTemplate.getForEntity("/sales-by-time-range", SaleInfoGetResponse::class.java)

        assertNotNull(result)
        assertEquals(HttpStatus.BAD_REQUEST, result?.statusCode)
    }

    @Test
    fun whenSaveSalesAndGetCalled_thenShouldReturnAllSales() {
        saleInfoRepository.deleteAll()

        var saleCount = 0
        var postRet = restTemplate.postForEntity("/save-sale", validReqForCash, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.OK, postRet?.statusCode)
        assertNull((postRet.body as SalePostResponse).errList)
        assertEquals((postRet.body as SalePostResponse).success, true)
        assertEquals((postRet.body as SalePostResponse).result?.final_price, validReqForCash.price * validReqForCash.price_modifier)
        assertEquals((postRet.body as SalePostResponse).result?.points, validReqForCash.price * Cash().pointsRate)
        saleCount++

        postRet = restTemplate.postForEntity("/save-sale", validReqForCashOnDelivery1, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.OK, postRet?.statusCode)
        assertNull((postRet.body as SalePostResponse).errList)
        assertEquals((postRet.body as SalePostResponse).success, true)
        assertEquals((postRet.body as SalePostResponse).result?.final_price, validReqForCashOnDelivery1.price * validReqForCashOnDelivery1.price_modifier)
        assertEquals((postRet.body as SalePostResponse).result?.points, validReqForCashOnDelivery1.price * CashOnDelivery().pointsRate)
        saleCount++

        postRet = restTemplate.postForEntity("/save-sale", validReqForCashOnDelivery2, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.OK, postRet?.statusCode)
        assertNull((postRet.body as SalePostResponse).errList)
        assertEquals((postRet.body as SalePostResponse).success, true)
        assertEquals((postRet.body as SalePostResponse).result?.final_price, validReqForCashOnDelivery2.price * validReqForCashOnDelivery2.price_modifier)
        assertEquals((postRet.body as SalePostResponse).result?.points, validReqForCashOnDelivery2.price * CashOnDelivery().pointsRate)
        saleCount++

        postRet = restTemplate.postForEntity("/save-sale", validReqForMastercard, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.OK, postRet?.statusCode)
        assertNull((postRet.body as SalePostResponse).errList)
        assertEquals((postRet.body as SalePostResponse).success, true)
        assertEquals((postRet.body as SalePostResponse).result?.final_price, validReqForMastercard.price * validReqForMastercard.price_modifier)
        assertEquals((postRet.body as SalePostResponse).result?.points, validReqForMastercard.price * Mastercard().pointsRate)
        saleCount++

        postRet = restTemplate.postForEntity("/save-sale", validReqForBankTransfer, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.OK, postRet?.statusCode)
        assertNull((postRet.body as SalePostResponse).errList)
        assertEquals((postRet.body as SalePostResponse).success, true)
        assertEquals((postRet.body as SalePostResponse).result?.final_price, validReqForBankTransfer.price * validReqForBankTransfer.price_modifier)
        assertEquals((postRet.body as SalePostResponse).result?.points, validReqForBankTransfer.price * BankTransfer().pointsRate)
        saleCount++

        postRet = restTemplate.postForEntity("/save-sale", validReqForCheque, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.OK, postRet?.statusCode)
        assertNull((postRet.body as SalePostResponse).errList)
        assertEquals((postRet.body as SalePostResponse).success, true)
        assertEquals((postRet.body as SalePostResponse).result?.final_price, validReqForCheque.price * validReqForCheque.price_modifier)
        assertEquals((postRet.body as SalePostResponse).result?.points, validReqForCheque.price * Cheque().pointsRate)
        saleCount++

        var result = restTemplate.getForEntity("/all-sales", SaleInfoGetResponse::class.java)

        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertNull((result.body as SaleInfoGetResponse).errList)
        assertEquals((result.body as SaleInfoGetResponse).success, true)
        assertEquals((result.body as SaleInfoGetResponse).result?.size, saleCount)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(0).customer_id, validReqForCash.customer_id)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(0).payment_method, validReqForCash.payment_method)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(0).points, validReqForCash.price * Cash().pointsRate)

        assertEquals((result.body as SaleInfoGetResponse).result!!.get(1).customer_id, validReqForCashOnDelivery1.customer_id)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(1).payment_method, validReqForCashOnDelivery1.payment_method)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(1).points, validReqForCashOnDelivery1.price * CashOnDelivery().pointsRate)

        assertEquals((result.body as SaleInfoGetResponse).result!!.get(2).customer_id, validReqForCashOnDelivery2.customer_id)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(2).payment_method, validReqForCashOnDelivery2.payment_method)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(2).points, validReqForCashOnDelivery2.price * CashOnDelivery().pointsRate)

        assertEquals((result.body as SaleInfoGetResponse).result!!.get(3).customer_id, validReqForMastercard.customer_id)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(3).payment_method, validReqForMastercard.payment_method)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(3).points, validReqForMastercard.price * Mastercard().pointsRate)

        assertEquals((result.body as SaleInfoGetResponse).result!!.get(4).customer_id, validReqForBankTransfer.customer_id)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(4).payment_method, validReqForBankTransfer.payment_method)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(4).points, validReqForBankTransfer.price * BankTransfer().pointsRate)

        assertEquals((result.body as SaleInfoGetResponse).result!!.get(5).customer_id, validReqForCheque.customer_id)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(5).payment_method, validReqForCheque.payment_method)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(5).points, validReqForCheque.price * Cheque().pointsRate)


        result = restTemplate.getForEntity("/sales-by-time-range?start_time=2022-10-24T00:00:00Z&end_time=2022-10-26T00:00:00Z", SaleInfoGetResponse::class.java)
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertNull((result.body as SaleInfoGetResponse).errList)
        assertEquals((result.body as SaleInfoGetResponse).success, true)
        assertEquals((result.body as SaleInfoGetResponse).result?.size, 2)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(0).payment_method, PaymentMethod.BANK_TRANSFER)
        assertEquals((result.body as SaleInfoGetResponse).result!!.get(1).payment_method, PaymentMethod.CHEQUE)
    }

    @Test
    fun whenInvalidSaveSalesCalled_thenShouldReturnError() {
        saleInfoRepository.deleteAll()

        var invalidReq = SaleInfoPostRequest()
        var postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)

        invalidReq.customer_id = "11111"
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)

        invalidReq.payment_method = PaymentMethod.CASH
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)

        invalidReq.price = 100f
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)

        invalidReq.datetime = "2022-10-24T00:00:00Z"
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.OK, postRet?.statusCode)
        assertNull((postRet?.body as SalePostResponse).errList)

        invalidReq.price_modifier = 0.8f
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)
        assertEquals((postRet?.body as SalePostResponse).errList!!.size, 1)
        assertEquals((postRet?.body as SalePostResponse).errList!![0], NOT_IN_POSSIBLE_RANGE)

        invalidReq.price_modifier = 1f
        invalidReq.payment_method = PaymentMethod.MASTERCARD
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)
        assertEquals((postRet?.body as SalePostResponse).errList!!.size, 1)
        assertEquals((postRet?.body as SalePostResponse).errList!![0], NO_ADDITIONAL_ITEMS)

        invalidReq.additional_item = invalidAdditonalItem1
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)
        assertEquals((postRet?.body as SalePostResponse).errList!!.size, 1)
        assertEquals((postRet?.body as SalePostResponse).errList!![0], LAST_4_KEY_ERROR)

        invalidReq.additional_item = invalidAdditonalItemForCard1
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)
        assertEquals((postRet?.body as SalePostResponse).errList!!.size, 1)
        assertEquals((postRet?.body as SalePostResponse).errList!![0], LAST_4_VALUE_ERROR)

        invalidReq.additional_item = invalidAdditonalItemForCard2
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)
        assertEquals((postRet?.body as SalePostResponse).errList!!.size, 1)
        assertEquals((postRet?.body as SalePostResponse).errList!![0], LAST_4_VALUE_ERROR)

        invalidReq.additional_item = invalidAdditonalItemForCard3
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)
        assertEquals((postRet?.body as SalePostResponse).errList!!.size, 1)
        assertEquals((postRet?.body as SalePostResponse).errList!![0], LAST_4_VALUE_ERROR)

        invalidReq.payment_method = PaymentMethod.CASH_ON_DELIVERY
        invalidReq.additional_item = invalidAdditonalItemForCashOnDelivery
        postRet = restTemplate.postForEntity("/save-sale", invalidReq, SalePostResponse::class.java)
        assertNotNull(postRet)
        assertEquals(HttpStatus.BAD_REQUEST, postRet?.statusCode)
        assertEquals((postRet?.body as SalePostResponse).errList!!.size, 1)
        assertEquals((postRet?.body as SalePostResponse).errList!![0], COURIER_SERVICE_VALUE_ERROR)
    }

}