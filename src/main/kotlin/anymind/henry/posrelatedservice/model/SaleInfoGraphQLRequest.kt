package anymind.henry.posrelatedservice.model

class AdditionalItem {
    var last_4: String? = null
    var courier_service: String? = null
    var bank: String? = null
    var account_number: String? = null
    var cheque_number: String? = null
}

class SaleInfoGraphQLRequest {
    var customer_id: String = ""
    var price: Float = 0.0f
    var price_modifier: Float = 1.0f
    var payment_method: String = ""
    var datetime: String = ""
    var additional_item: AdditionalItem? = null
}

class MutationResponse {
    var final_price: Float = 0f
    var points: Float = 0f
    var errors: String = ""
}