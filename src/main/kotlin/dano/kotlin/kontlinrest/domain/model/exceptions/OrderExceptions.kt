package dano.kotlin.kontlinrest.domain.model.exceptions

class InvalidOrderException(message: String) : Exception(message)

class OrderExistsException(message: String) : Exception(message)

class OrderNotFoundException(message: String) : Exception(message)

class OrderStateException(message: String) : Exception(message)
