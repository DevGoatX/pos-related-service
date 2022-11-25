package anymind.henry.posrelatedservice.model

import anymind.henry.posrelatedservice.extension.ErrorList
import anymind.henry.posrelatedservice.persistance.model.SaleInfo

class SaleInfoPostRequest {
    var customer_id: String = ""
    var price: Float = 0.0f
    var price_modifier: Float = 1.0f
    var payment_method: String = ""
    var datetime: String = ""
    var additional_item: Map<String, Any>? = null
}

class SalePostResponse {
    class Result {
        var final_price: Float = 0f
        var points: Float = 0f
    }

    var success: Boolean = false
    var result: Result? = null
    var errList: ErrorList? = null
}

class SaleInfoGetResponse {
    var success: Boolean = false
    var result: List<SaleInfo>? = null
    var errList: ErrorList? = null
}
