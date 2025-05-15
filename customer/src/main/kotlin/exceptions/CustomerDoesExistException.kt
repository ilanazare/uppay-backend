package org.example.exceptions

class CustomerDoesExistException(
    message: String,
) : RuntimeException(message)
