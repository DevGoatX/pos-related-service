package anymind.henry.posrelatedservice.persistance.dao

import anymind.henry.posrelatedservice.persistance.model.SaleInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SaleInfoRepository : JpaRepository<SaleInfo, Long> {

    @Query("SELECT info FROM SaleInfo info WHERE customer_id = :customer_id")
    fun findByCustomerId(@Param("customer_id") customer_id: String): List<SaleInfo>

    fun findAllByDatetimeBetween(startTime: String, endTime: String): List<SaleInfo>
}