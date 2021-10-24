package com.flight.controller;

import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class BaseController {
    protected static final int SUCCESS_RESPONSE = 200;
    protected static final int ERROR_RESPONSE = 500;
    protected static final int BAD_REQUEST = 400;

    protected HttpStatus resolveHttpStatus(int httpStatusCode) {
        switch (httpStatusCode) {
            case 200:
                return HttpStatus.OK;
            case 201:
                return HttpStatus.CREATED;
            case 400:
                return HttpStatus.BAD_REQUEST;
            case 412:
                return HttpStatus.PRECONDITION_FAILED;
            case 500:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.BAD_REQUEST;
    }

    public MultiValueMap<String, String> constructResponseHeader() {
        MultiValueMap<String, String> responseHeaders = new LinkedMultiValueMap<>();
//TODO this can be used to send some header value to client for correlating request later, e.g. correlation id
        return responseHeaders;
    }
}
