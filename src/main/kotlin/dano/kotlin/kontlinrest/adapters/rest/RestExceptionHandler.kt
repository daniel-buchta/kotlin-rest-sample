package dano.kotlin.kontlinrest.adapters.rest

import dano.kotlin.kontlinrest.domain.model.exceptions.OrderNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant
import java.time.LocalDateTime
import javax.validation.ConstraintViolationException


@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

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

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
            ex: ConstraintViolationException,
            request: WebRequest
    ): ResponseEntity<Any> {
        val body = ValidationError(
                status = HttpStatus.BAD_REQUEST,
                message = ex.localizedMessage,
                errors = ex.constraintViolations.map { it.propertyPath.toString() + ": " + it.message }
        )
        return ResponseEntity(body, HttpHeaders(), body.status)
    }

    override fun handleMethodArgumentNotValid(
            ex: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        val body = ValidationError(
                status = status,
                message = ex.localizedMessage,
                errors = ex.bindingResult.fieldErrors.map { it.field + ": " + it.defaultMessage }
        )

        return ResponseEntity(body, headers, status)
    }

    data class ValidationError(
            val status: HttpStatus,
            val message: String,
            val timestamp: LocalDateTime = LocalDateTime.now(),
            val errors: List<String>
    )
}
