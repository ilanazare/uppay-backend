package org.example.exceptions

class CustomerNotFoundException(
    message: String,
) : RuntimeException(message)
