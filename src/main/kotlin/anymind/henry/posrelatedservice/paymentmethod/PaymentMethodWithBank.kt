package anymind.henry.posrelatedservice.paymentmethod

import anymind.henry.posrelatedservice.extension.ErrorList
import org.json.JSONException

const val BANK_KEY = "bank"

const val BANK_KEY_ERROR = "There is no key for bank, 'bank'"
const val BANK_VALUE_ERROR = "There is no bank information"

abstract class PaymentMethodWithBank(name: String, pointsRate: Float): PaymentMethod(name, pointsRate) {

    /**
     * check if bank key "bank" exists in the additional items
     */
    private fun isBankKeyValid(additionalItem: Map<String, Any>) = additionalItem.containsKey(BANK_KEY) && additionalItem[BANK_KEY] != null

    /**
     * check if "bank" field's value is valid in the additional items
     */
    private fun isBankValid(additionalItem: Map<String, Any>): Boolean {
        try {
            val itemValue: String = additionalItem[BANK_KEY].toString()
            if (itemValue.isNotEmpty())
                return true

        } catch (e: JSONException) {
            return false
        }

        return false
    }

    abstract fun checkValidationWithAnotherInfo(additionalItem: Map<String, Any>): ErrorList

    /**
     * check validation for payment method without additional items
     */
    private fun checkValidationWithBank(priceModifier: Float, additionalItem: Map<String, Any>?): ErrorList {
        val errList = ErrorList()

        // first check price modifier and check if additional items exist
        errList.addAll(this.checkBasicErrors(priceModifier, additionalItem, true))

        if (additionalItem != null) {
            // check bank key and value
            if (isBankKeyValid(additionalItem)) {
                if (!isBankValid(additionalItem)) {
                    errList.add(BANK_VALUE_ERROR)
                }
            } else {
                errList.add(BANK_KEY_ERROR)
            }

            // check another key and value
            errList.addAll(checkValidationWithAnotherInfo((additionalItem)))
        }

        return errList
    }

    override fun checkValidation(priceModifier: Float, additionalItem: Map<String, Any>?) = this.checkValidationWithBank(priceModifier, additionalItem)
}