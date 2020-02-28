package dano.kotlin.kontlinrest.adapters.rest

import dano.kotlin.kontlinrest.domain.model.exceptions.OrderNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant

@ControllerAdvice
class RestExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(OrderNotFoundException::class)])
    fun handlerOrderNotFound(ex: OrderNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val errorDetail = ErrorDetail(
                type = "/errors/orders/not-found",
                title = "Order not found",
                status = HttpStatus.NOT_FOUND.value(),
                detail = ex.message ?: "",
                instance = "/api/errors/42",
                timestamp = Instant.now(),
                requestPath = request.contextPath
        )
        return ResponseEntity(errorDetail, HttpStatus.NOT_FOUND)
    }
}
