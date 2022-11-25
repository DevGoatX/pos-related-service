package anymind.henry.posrelatedservice.integrationtests

import anymind.henry.posrelatedservice.PosRelatedServiceApplication
import anymind.henry.posrelatedservice.persistance.dao.SaleInfoRepository
import com.graphql.spring.boot.test.GraphQLTestTemplate
import graphql.Assert.assertTrue
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource


@SpringBootTest(
    classes = [PosRelatedServiceApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
    locations = arrayOf("classpath:application-integrationtest.properties")
)
class AppGraphQLIntegrationTest {

    @Autowired
    lateinit var graphQLTestTemplate: GraphQLTestTemplate

    @Autowired
    private lateinit var saleInfoRepository: SaleInfoRepository

    @Test
    fun whenAllSalesCalled_thenShouldReturnEmpty() {
        val response = graphQLTestTemplate.postForResource("/graphql/all-sales.graphql")
//        assertTrue(response.isOk());
    }

    @Test
    fun givenCorrectSaleData_whenSavedCalled_thenShouldBeOK() {
        val response = graphQLTestTemplate.postForResource("/graphql/save-sale.graphql")
//        assertTrue(response.isOk());
//        assertEquals(response.get("$.data.final_price").toFloat(), 95f)
//        assertEquals(response.get("$.data.points").toFloat(), 5f)
    }

    @Test
    fun givenCorrectSaleData_whenSavedCalledAndSearchWithTimeRange_thenReturnSalesInfo() {
        saleInfoRepository.deleteAll()

        var response = graphQLTestTemplate.postForResource("/graphql/save-sale.graphql")
//        assertTrue(response.isOk());
//        assertEquals(response.get("$.data.saveSaleInfo.final_price").toFloat(), 95f)
//        assertEquals(response.get("$.data.saveSaleInfo.points").toFloat(), 5f)

        response = graphQLTestTemplate.postForResource("/graphql/sales-by-time-range.graphql")
//        assertTrue(response.isOk())
//        assertEquals(response.get("$.data.salesByTimeRange[0].customer_id"), "12345")
    }

}