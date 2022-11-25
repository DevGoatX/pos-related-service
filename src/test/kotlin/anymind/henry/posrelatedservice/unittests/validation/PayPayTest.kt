package anymind.henry.posrelatedservice.unittests.validation

import anymind.henry.posrelatedservice.paymentmethod.*
import anymind.henry.posrelatedservice.paymentmethod.PayPay
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PayPayTest {
    private val payment = PayPay()

    @Test
    fun shouldBeFalseDuetoWrongModifier1() {
        val errList = payment.checkValidation(0.99f)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeFalseDuetoWrongModifier2() {
        val errList = payment.checkValidation(1.01f)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeTrue1() {
        val errList = payment.checkValidation(1f)

        assertEquals(true, errList.isEmpty())
    }
}