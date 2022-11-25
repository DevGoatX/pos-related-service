package anymind.henry.posrelatedservice.paymentmethod

class Cash: PaymentMethodWithoutAdditionalItem(CASH, 0.05f) {
    init {
        modifierValidator.minModifier = 0.9f
        modifierValidator.maxModifier = 1f
    }
}