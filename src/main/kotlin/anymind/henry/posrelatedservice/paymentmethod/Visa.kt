package anymind.henry.posrelatedservice.paymentmethod

import org.json.JSONObject

class Visa: PaymentMethodWithCard(VISA, 0.03f) {
    init {
        modifierValidator.minModifier = 0.95f
        modifierValidator.maxModifier = 1f
    }
}