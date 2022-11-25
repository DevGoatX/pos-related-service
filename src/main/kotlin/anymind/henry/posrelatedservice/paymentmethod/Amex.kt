package anymind.henry.posrelatedservice.paymentmethod

class Amex: PaymentMethodWithCard(AMEX, 0.02f) {
    init {
        modifierValidator.minModifier = 0.98f
        modifierValidator.maxModifier = 1.01f
    }
}