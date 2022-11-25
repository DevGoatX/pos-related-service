package anymind.henry.posrelatedservice.paymentmethod

open class PaymentMethodWithoutAdditionalItem(name: String, pointsRate: Float): PaymentMethod(name, pointsRate) {
    init {
        modifierValidator.minModifier = 1f
        modifierValidator.maxModifier = 1f
    }
    override fun checkValidation(priceModifier: Float, additionalItem: Map<String, Any>?) = this.checkBasicErrors(priceModifier)
}