package anymind.henry.posrelatedservice.paymentmethod

import anymind.henry.posrelatedservice.extension.ErrorList
import org.json.JSONException

const val CHEQUE_NUMBER_KEY = "cheque_number"

const val CHEQUE_NUMBER_KEY_ERROR = "There is no key for cheque number, 'cheque_number'"
const val CHEQUE_NUMBER_VALUE_ERROR = "There is no cheque number"

class Cheque: PaymentMethodWithBank(CHEQUE, 0f) {
    init {
        modifierValidator.minModifier = 0.9f
        modifierValidator.maxModifier = 1f
    }

    /**
     * check if cheque number key "cheque_number" exists in the additional items
     */
    private fun isChequeNumberKeyValid(additionalItem: Map<String, Any>) = additionalItem.containsKey(CHEQUE_NUMBER_KEY) && additionalItem[CHEQUE_NUMBER_KEY] != null

    /**
     * check if cheque number field's value is valid in the additional items
     */
    private fun isChequeNumberValid(additionalItem: Map<String, Any>): Boolean {
        try {
            val itemValue = additionalItem[CHEQUE_NUMBER_KEY].toString()
            if (itemValue.isNotEmpty())
                return true

        } catch (e: JSONException) {
            return false
        }

        return false
    }

    override fun checkValidationWithAnotherInfo(additionalItem: Map<String, Any>): ErrorList {
        val errList = ErrorList()
        if (isChequeNumberKeyValid(additionalItem)) {
            if (!isChequeNumberValid(additionalItem)) {
                errList.add(CHEQUE_NUMBER_VALUE_ERROR)
            }
        } else {
            errList.add(CHEQUE_NUMBER_KEY_ERROR)
        }

        return errList
    }
}