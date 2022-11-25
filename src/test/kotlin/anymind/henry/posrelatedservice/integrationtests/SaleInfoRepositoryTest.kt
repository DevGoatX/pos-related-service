package anymind.henry.posrelatedservice.integrationtests

import anymind.henry.posrelatedservice.persistance.dao.SaleInfoRepository
import anymind.henry.posrelatedservice.persistance.model.SaleInfo
import junit.framework.Assert.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class SaleInfoRepositoryTest {
    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var saleInfoRepository: SaleInfoRepository

    @Test
    fun whenFindById_thenReturnSaleInfo() {
        val saleInfo = SaleInfo()
        saleInfo.customer_id = "11111"
        saleInfo.payment_method = "VISA"
        saleInfo.sales = 100f
        saleInfo.datetime = "2022-11-20T11:11:11Z"
        entityManager.persist(saleInfo)
        entityManager.flush()

        val saleInfoFound = saleInfoRepository.findByIdOrNull(saleInfo.id)
        assertThat(saleInfo == saleInfoFound)
    }

    @Test
    fun whenFindByDateRange_thenReturnSaleInfoList() {
        val saleInfo1 = SaleInfo()
        saleInfo1.customer_id = "11111"
        saleInfo1.payment_method = "VISA"
        saleInfo1.sales = 100f
        saleInfo1.datetime = "2022-11-20T11:11:11Z"
        saleInfoRepository.save(saleInfo1)

        val saleInfo2 = SaleInfo()
        saleInfo2.customer_id = "11111"
        saleInfo2.payment_method = "VISA"
        saleInfo2.sales = 100f
        saleInfo2.datetime = "2022-11-22T10:10:10Z"
        saleInfoRepository.save(saleInfo2)

        val saleInfo3 = SaleInfo()
        saleInfo3.customer_id = "11111"
        saleInfo3.payment_method = "VISA"
        saleInfo3.sales = 100f
        saleInfo3.datetime = "2022-11-24T10:10:10Z"
        saleInfoRepository.save(saleInfo3)

        var saleInfoList = saleInfoRepository.findAllByDatetimeBetween("2022-11-20T00:00:00Z", "2022-11-25T00:00:00Z")
        assertEquals(saleInfoList.size, 3)

        saleInfoList = saleInfoRepository.findAllByDatetimeBetween("2022-11-21T00:00:00Z", "2022-11-25T00:00:00Z")
        assertEquals(saleInfoList.size, 2)

        saleInfoList = saleInfoRepository.findAllByDatetimeBetween("2022-11-25T00:00:00Z", "2022-11-27T00:00:00Z")
        assertEquals(saleInfoList.size, 0)
    }
}