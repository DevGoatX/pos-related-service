package anymind.henry.posrelatedservice.unittests.validation

import anymind.henry.posrelatedservice.paymentmethod.*
import anymind.henry.posrelatedservice.paymentmethod.Cheque
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ChequeTest {
    private val payment = Cheque()

    @Test
    fun shouldBeFalseDuetoNoKey() {
        val errList = payment.checkValidation(1f)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NO_ADDITIONAL_ITEMS))
    }

    @Test
    fun shouldBeFalseDuetoWrongKey() {
        val additionalItem = HashMap<String, Any>()
        additionalItem["ban"] = "1223"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(BANK_KEY_ERROR))

    }

    @Test
    fun shouldBeFalseDuetoWrongValue1() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = ""

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(BANK_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongValue2() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = "USA BANK"
        additionalItem["cheque number"] = "112211"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(CHEQUE_NUMBER_KEY_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongValue3() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = "USA BANK"
        additionalItem[CHEQUE_NUMBER_KEY] = ""

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(CHEQUE_NUMBER_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongModifier1() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = "USA BANK"
        additionalItem[CHEQUE_NUMBER_KEY] = "111222231232"

        val errList = payment.checkValidation(0.89f, additionalItem)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeFalseDuetoWrongModifier2() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = "USA BANK"
        additionalItem[CHEQUE_NUMBER_KEY] = "111222231232"

        val errList = payment.checkValidation(1.01f, additionalItem)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeTrue() {

        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = "USA BANK"
        additionalItem[CHEQUE_NUMBER_KEY] = "111222231232"

        val errList = payment.checkValidation(1f, additionalItem)

        assertEquals(true, errList.isEmpty())
    }

}