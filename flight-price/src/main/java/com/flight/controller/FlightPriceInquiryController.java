package com.flight.controller;

import com.flight.dto.request.FlightPriceRequest;
import com.flight.dto.response.ErrorResponse;
import com.flight.dto.response.FlightPriceResponse;
import com.flight.service.IFlightPriceSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.format.DateTimeParseException;

@RestController
public class FlightPriceInquiryController extends BaseController{

    @Autowired
    IFlightPriceSearchService flightSearchService;

    private static final Logger logger = LoggerFactory.getLogger(FlightPriceInquiryController.class);


    /**
     * This function will be used to fetch flight price against date, flight number.
     */
    @PostMapping("/price")
    //TODO swagger documentation
    private ResponseEntity<?> searchFlight(@RequestHeader HttpHeaders headers,
                                           @RequestBody FlightPriceRequest flightPriceRequest) {

        logger.info("Received flight price request");


        int responseStatusCode = SUCCESS_RESPONSE;//SUCCESS Code
        FlightPriceResponse response = null;

        try {
            if (flightPriceRequest != null)
            {
                response = flightSearchService.searchFlightPrice(flightPriceRequest);
            }
        }
        catch (DateTimeParseException dpe)
        {
            logger.info("flight price search request is invalid {} " , dpe);
            responseStatusCode = BAD_REQUEST;

            ErrorResponse errorResponse = new ErrorResponse("ERR_002","flight price search request is invalid ");
            return new ResponseEntity<>(errorResponse, constructResponseHeader(), resolveHttpStatus(responseStatusCode));
        }
        catch (Exception e)
        {
            responseStatusCode = ERROR_RESPONSE;
            logger.info("flight price search request failed due to exception {} " , e);
            //TODO errors can be externalized and handled properly, but as of now its out of POC scope
            ErrorResponse errorResponse = new ErrorResponse("ERR_001","Unknown Exception Occured");
            return new ResponseEntity<>(errorResponse, constructResponseHeader(), resolveHttpStatus(responseStatusCode));
        }
        finally {
            logger.info("flight price search request completed");
        }

        return new ResponseEntity<>(response, constructResponseHeader(), resolveHttpStatus(responseStatusCode));
    }
}
