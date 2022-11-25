package anymind.henry.posrelatedservice.paymentmethod

import anymind.henry.posrelatedservice.extension.ErrorList

import kotlin.collections.ArrayList

const val NOT_IN_POSSIBLE_RANGE = "The price modifier value is out of the possible range"
const val NO_ADDITIONAL_ITEMS = "There is no additional item information"

abstract class PaymentMethod(val name: String = "", val pointsRate: Float = 0.0f) {
    companion object {
        const val CASH = "CASH"
        const val CASH_ON_DELIVERY = "CASH_ON_DELIVERY"
        const val VISA = "VISA"
        const val MASTERCARD = "MASTERCARD"
        const val AMEX = "AMEX"
        const val JCB = "JCB"
        const val LINE_PAY = "LINE PAY"
        const val PAYPAY = "PAYPAY"
        const val POINTS = "POINTS"
        const val GRAB_PAY = "GRAB PAY"
        const val BANK_TRANSFER = "BANK TRANSFER"
        const val CHEQUE = "CHEQUE"
    }

    object ModifierValidator {
        var minModifier: Float = 1.0f
        var maxModifier: Float = 1.0f

        fun isModifierValid(modifier: Float): Boolean = modifier in minModifier..maxModifier
    }

    var modifierValidator = ModifierValidator

    /**
     * check price modifier range and check if additional items exist
     */
    protected fun checkBasicErrors(priceModifier: Float, additionalItem: Map<String, Any>? = null, bNeedAddtionalItem: Boolean = false): ArrayList<String> {
        val errList = ArrayList<String>()
        if (!modifierValidator.isModifierValid(priceModifier)) {
            errList.add(NOT_IN_POSSIBLE_RANGE)
        }

        if (bNeedAddtionalItem && additionalItem == null) {
            errList.add(NO_ADDITIONAL_ITEMS)
        }
        return errList
    }

    abstract fun checkValidation(priceModifier: Float = 1f, additionalItem: Map<String, Any>? = null): ErrorList
}