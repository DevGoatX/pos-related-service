package anymind.henry.posrelatedservice.service

import anymind.henry.posrelatedservice.persistance.dao.SaleInfoRepository
import anymind.henry.posrelatedservice.persistance.model.SaleInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SaleInfoService {
    @Autowired
    private lateinit var saleInfoRepository: SaleInfoRepository

    fun save(SaleInfo: SaleInfo) = saleInfoRepository.save(SaleInfo)
    fun delete(SaleInfo: SaleInfo) = saleInfoRepository.delete(SaleInfo)

    fun findAll() = saleInfoRepository.findAll()
    fun findByCustomerId(customer_id: String) = saleInfoRepository.findByCustomerId(customer_id)
    fun findAllByDatetimeBetween(startTime: String, endTime: String) =
        saleInfoRepository.findAllByDatetimeBetween(startTime, endTime)
}
