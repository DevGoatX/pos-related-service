package anymind.henry.posrelatedservice.paymentmethod

import anymind.henry.posrelatedservice.extension.ErrorList
import org.json.JSONException

const val COURIER_SERVICE_KEY = "courier_service"
const val COURIER_SERVICE_REGEX_STRING = "^((?:YAMATO)|(?:SAGAWA))\$"

const val COURIER_SERVICE_KEY_ERROR = "There is no key for courier service, 'courier_service'"
const val COURIER_SERVICE_VALUE_ERROR = "The courier service should be only 'YAMATO' or 'SAGAWA'"

class CashOnDelivery: PaymentMethod(CASH_ON_DELIVERY, 0.05f) {

    init {
        modifierValidator.minModifier = 1f
        modifierValidator.maxModifier = 1.02f
    }

    override fun checkValidation(priceModifier: Float, additionalItem: Map<String, Any>?): ErrorList {
        val errList = ErrorList()

        // first check price modifier and check if additional items exist
        errList.addAll(this.checkBasicErrors(priceModifier, additionalItem, true))

        if (additionalItem != null) {
            // check courier service key and value
            if (isCourierServiceKeyValid(additionalItem)) {
                if (!isCourierServiceValid(additionalItem)) {
                    errList.add(COURIER_SERVICE_VALUE_ERROR)
                }
            } else {
                errList.add(COURIER_SERVICE_KEY_ERROR)
            }
        }

        return errList
    }

    /**
     * check if courier service key "courier_service" exists in the additional items
     */
    private fun isCourierServiceKeyValid(additionalItem: Map<String, Any>) = additionalItem.containsKey(COURIER_SERVICE_KEY) && additionalItem[COURIER_SERVICE_KEY] != null

    /**
     * check if "courier_service" field's value is valid in the additional items
     */
    private fun isCourierServiceValid(additionalItem: Map<String, Any>): Boolean {
        try {
            val itemValue = additionalItem[COURIER_SERVICE_KEY].toString()
            if (COURIER_SERVICE_REGEX_STRING.toRegex().matches(itemValue))
                return true

        } catch (e: JSONException) {
            return false
        }

        return false
    }
}