package anymind.henry.posrelatedservice.paymentmethod

import anymind.henry.posrelatedservice.extension.ErrorList
import org.json.JSONException

const val LAST_4_KEY = "last_4"
const val FOUR_DIGIT_REGEX_STRING = "^\\d{4}\$"

const val LAST_4_KEY_ERROR = "There is no key for last 4, 'last_4'"
const val LAST_4_VALUE_ERROR = "The last 4 numbers are invalid"
open class PaymentMethodWithCard(name: String, pointsRate: Float): PaymentMethod(name, pointsRate) {

    /**
     * check if last 4 key "last_4" exists in the additional items
     */
    private fun isLast4KeyValid(additionalItem: Map<String, Any>) = additionalItem.containsKey(LAST_4_KEY) && additionalItem[LAST_4_KEY] != null

    /**
     * check if "last_4" field's value is valid in the additional items
     */
    private fun isLast4DigitsValid(additionalItem: Map<String, Any>): Boolean {
        try {
            val itemValue: String = additionalItem[LAST_4_KEY].toString()
            if (FOUR_DIGIT_REGEX_STRING.toRegex().matches(itemValue))
                return true

        } catch (e: JSONException) {
            return false
        }

        return false
    }

    /**
     * check validation for payment method without additional items
     */
    private fun checkValidationWithCard(priceModifier: Float, additionalItem: Map<String, Any>?): ErrorList {
        val errList = ErrorList()

        // first check price modifier and check if additional items exist
        errList.addAll(this.checkBasicErrors(priceModifier, additionalItem, true))

        if (additionalItem != null) {
            // check last 4 key and digits
            if (isLast4KeyValid(additionalItem)) {
                if (!isLast4DigitsValid(additionalItem)) {
                    errList.add(LAST_4_VALUE_ERROR)
                }
            } else {
                errList.add(LAST_4_KEY_ERROR)
            }
        }

        return errList
    }

    override fun checkValidation(priceModifier: Float, additionalItem: Map<String, Any>?) = this.checkValidationWithCard(priceModifier, additionalItem)
}