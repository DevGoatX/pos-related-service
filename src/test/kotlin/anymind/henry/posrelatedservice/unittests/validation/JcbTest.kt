package anymind.henry.posrelatedservice.unittests.validation

import anymind.henry.posrelatedservice.paymentmethod.*
import anymind.henry.posrelatedservice.paymentmethod.Jcb
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class JcbTest {
    private val payment = Jcb()

    @Test
    fun shouldBeFalseDuetoNoKey() {
        val errList = payment.checkValidation(1f)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NO_ADDITIONAL_ITEMS))
    }

    @Test
    fun shouldBeFalseDuetoWrongKey() {
        val additionalItem = HashMap<String, Any>()
        additionalItem["last4"] = "1223"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(LAST_4_KEY_ERROR))

    }

    @Test
    fun shouldBeFalseDuetoWrongValue1() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[LAST_4_KEY] = "ASDF"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(LAST_4_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongValue2() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[LAST_4_KEY] = "123a"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(LAST_4_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongValue3() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[LAST_4_KEY] = "123"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(LAST_4_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongValue4() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[LAST_4_KEY] = "12345"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(LAST_4_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongModifier1() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[LAST_4_KEY] = "1234"

        val errList = payment.checkValidation(0.94f, additionalItem)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeFalseDuetoWrongModifier2() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[LAST_4_KEY] = "1234"

        val errList = payment.checkValidation(1.01f, additionalItem)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeTrue() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[LAST_4_KEY] = "8956"

        val errList = payment.checkValidation(1f, additionalItem)

        assertEquals(true, errList.isEmpty())
    }

}