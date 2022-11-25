package anymind.henry.posrelatedservice.persistance.model

import javax.persistence.*

@Entity
class SaleInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    // customer id
    var customer_id: String = ""

    // payment method name
    var payment_method: String = ""

    // sales = final price of request
    @Column(precision = 10, scale = 2)
    var sales: Float = 0.0f

    // points = sales * points_ratio
    @Column(precision = 10, scale = 2)
    var points: Float = 0.0f

    // date time, format "2022-11-10T00:00:00Z"
    @Column(length = 20)
    var datetime: String = ""

    var additional_item: String = ""
}