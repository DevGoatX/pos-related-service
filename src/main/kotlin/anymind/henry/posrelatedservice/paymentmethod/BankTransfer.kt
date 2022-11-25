package anymind.henry.posrelatedservice.paymentmethod

import anymind.henry.posrelatedservice.extension.ErrorList
import org.json.JSONException

const val ACCOUNT_NUMBER_KEY = "account_number"

const val ACCOUNT_NUMBER_KEY_ERROR = "There is no key for account number, 'account_number'"
const val ACCOUNT_NUMBER_VALUE_ERROR = "There is no account number"

class BankTransfer: PaymentMethodWithBank(BANK_TRANSFER, 0f) {

    /**
     * check if account number key "account_number" exists in the additional items
     */
    private fun isAccountNumberKeyValid(additionalItem: Map<String, Any>) = additionalItem.containsKey(ACCOUNT_NUMBER_KEY) && additionalItem[ACCOUNT_NUMBER_KEY] != null

    /**
     * check if "account_number" field's value is valid
     */
    private fun isAccountNumberValid(additionalItem: Map<String, Any>): Boolean {
        try {
            val itemValue = additionalItem[ACCOUNT_NUMBER_KEY].toString()
            if (itemValue.isNotEmpty())
                return true

        } catch (e: JSONException) {
            return false
        }

        return false
    }

    override fun checkValidationWithAnotherInfo(additionalItem: Map<String, Any>): ErrorList {
        val errList = ErrorList()
        if (isAccountNumberKeyValid(additionalItem)) {
            if (!isAccountNumberValid(additionalItem)) {
                errList.add(ACCOUNT_NUMBER_VALUE_ERROR)
            }
        } else {
            errList.add(ACCOUNT_NUMBER_KEY_ERROR)
        }

        return errList
    }
}