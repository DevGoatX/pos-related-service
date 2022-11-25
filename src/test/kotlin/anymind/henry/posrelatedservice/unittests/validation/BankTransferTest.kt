package anymind.henry.posrelatedservice.unittests.validation

import anymind.henry.posrelatedservice.paymentmethod.*
import anymind.henry.posrelatedservice.paymentmethod.BankTransfer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BankTransferTest {
    private val payment = BankTransfer()

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
        additionalItem["account number"] = "112211"

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(ACCOUNT_NUMBER_KEY_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongValue3() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = "USA BANK"
        additionalItem[ACCOUNT_NUMBER_KEY] = ""

        val errList = payment.checkValidation(1f, additionalItem)
        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(ACCOUNT_NUMBER_VALUE_ERROR))
    }

    @Test
    fun shouldBeFalseDuetoWrongModifier1() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = "USA BANK"
        additionalItem[ACCOUNT_NUMBER_KEY] = "111222231232"

        val errList = payment.checkValidation(0.9f, additionalItem)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeFalseDuetoWrongModifier2() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = "USA BANK"
        additionalItem[ACCOUNT_NUMBER_KEY] = "111222231232"

        val errList = payment.checkValidation(1.1f, additionalItem)

        assertEquals(false, errList.isEmpty())
        assertEquals(true, errList.contains(NOT_IN_POSSIBLE_RANGE))
    }

    @Test
    fun shouldBeTrue() {
        val additionalItem = HashMap<String, Any>()
        additionalItem[BANK_KEY] = "USA BANK"
        additionalItem[ACCOUNT_NUMBER_KEY] = "111222231232"

        val errList = payment.checkValidation(1f, additionalItem)

        assertEquals(true, errList.isEmpty())
    }

}