package anymind.henry.posrelatedservice.unittests.validation

import anymind.henry.posrelatedservice.paymentmethod.*
import anymind.henry.posrelatedservice.paymentmethod.CashOnDelivery
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CashOnDeliveryTest {
    private val payment = CashOnDelivery()

    @Test
    fun shouldBeFalseDuetoNoKey() {
        val errList = payment.checkValidation(1f)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NO_ADDITIONAL_ITEMS))
    }

    @Test
    fun shouldBeFalseDuetoWrongKey() {
        val additionalItem = HashMap<String, Any>()
        additionalItem["courier service"] = "1223"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(COURIER_SERVICE_KEY_ERROR))

    }

    @Test
    fun shouldBeFalseDuetoWrongValue1() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[COURIER_SERVICE_KEY] = ""

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(COURIER_SERVICE_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongValue2() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[COURIER_SERVICE_KEY] = "SGAWA"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(COURIER_SERVICE_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongValue3() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[COURIER_SERVICE_KEY] = "YAMOTOS"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(COURIER_SERVICE_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongModifier1() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[COURIER_SERVICE_KEY] = "YAMOTO"

        val errList = payment.checkValidation(0.99f, additionalItem)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeFalseDuetoWrongModifier2() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[COURIER_SERVICE_KEY] = "YAMOTO"

        val errList = payment.checkValidation(1.03f, additionalItem)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeTrue1() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[COURIER_SERVICE_KEY] = "YAMATO"

        val errList = payment.checkValidation(1f, additionalItem)

        assertEquals(true, errList.isEmpty())
    }

    @Test
    fun shouldBeTrue2() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[COURIER_SERVICE_KEY] = "SAGAWA"

        val errList = payment.checkValidation(1f, additionalItem)

        assertEquals(true, errList.isEmpty())
    }
}