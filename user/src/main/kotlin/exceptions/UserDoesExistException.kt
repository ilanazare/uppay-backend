package com.exceptions

class UserDoesExistException(
    message: String,
) : RuntimeException(message)
