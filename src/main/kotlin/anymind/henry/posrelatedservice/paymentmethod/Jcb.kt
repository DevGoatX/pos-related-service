package anymind.henry.posrelatedservice.paymentmethod

import org.json.JSONObject

class Jcb: PaymentMethodWithCard(JCB, 0.05f) {
    init {
        modifierValidator.minModifier = 0.95f
        modifierValidator.maxModifier = 1f
    }
}