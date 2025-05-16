package org.example.exceptions

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class FeignErrorDecoder : ErrorDecoder {
    override fun decode(
        methodKey: String,
        response: Response,
    ): Exception =
        when (response.status()) {
            404 -> ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found")
            500 -> ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error")
            else -> ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request")
        }
}
