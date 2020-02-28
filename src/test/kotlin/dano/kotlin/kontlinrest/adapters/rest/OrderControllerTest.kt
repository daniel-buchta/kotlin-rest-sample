package dano.kotlin.kontlinrest.adapters.rest

import com.fasterxml.jackson.databind.ObjectMapper
import dano.kotlin.kontlinrest.domain.model.entities.Order
import dano.kotlin.kontlinrest.domain.model.enums.OrderState
import dano.kotlin.kontlinrest.domain.model.valueobjects.OrderItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
internal class OrderControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var json: ObjectMapper

    @Test
    fun `saveOrder `() {
        val expectedOrder = Order(id = 1,
                state = OrderState.NEW,
                items = listOf(OrderItem("Test", 2), (OrderItem("Other", 3))))

        val responseString = mvc.perform(put("/api/orders/id-${expectedOrder.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{
                    "items": [{ 
                        "name": "${expectedOrder.items[0].name}", 
                        "count": ${expectedOrder.items[0].count}
                      }, { 
                        "name": "${expectedOrder.items[1].name}", 
                        "count": ${expectedOrder.items[1].count}
                    }]}"""))
                .andExpect(status().is2xxSuccessful)
                .andReturn().response.contentAsString

        val order = json.readValue(responseString, Order::class.java)

        assertThat(order).isEqualTo(expectedOrder)
    }

    @Test
    fun `getOrder `() {
        val responseString = mvc.perform(get("/api/orders/id-42"))
                .andExpect(status().isNotFound)
                .andReturn().response.contentAsString

        println(responseString)

        assertThat(responseString).isNotEmpty()
    }
}
