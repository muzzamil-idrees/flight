package com.flight.controller;

import com.flight.dto.request.FlightRequest;
import com.flight.dto.response.ErrorResponse;
import com.flight.dto.response.FlightResponse;
import com.flight.service.IFlightSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.format.DateTimeParseException;

@RestController
public class FlightInquiryController extends BaseController{

    @Autowired
    IFlightSearchService flightSearchService;

    private static final Logger logger = LoggerFactory.getLogger(FlightInquiryController.class);


    /**
     * This function will be used to fetch flight number against date, arrival/departure information and it will initiate 5 independent connections and change flights based on schedule.
     */
    @PostMapping("/flight")
    //TODO swagger documentation
    private ResponseEntity<?> searchFlight(@RequestHeader HttpHeaders headers,
                                           @RequestBody FlightRequest flightRequest) {

        logger.info("Received flight request");
        int responseStatusCode = SUCCESS_RESPONSE;//SUCCESS Code
        FlightResponse response = null;

        try {
            if (flightRequest != null)
            {
                response = flightSearchService.searchFlight(flightRequest);
            }
        }
        catch (DateTimeParseException dpe)
        {
            logger.info("flight search request is invalid {} " , dpe);
            responseStatusCode = BAD_REQUEST;

            ErrorResponse errorResponse = new ErrorResponse("ERR_002","flight search request is invalid ");
            return new ResponseEntity<>(errorResponse, constructResponseHeader(), resolveHttpStatus(responseStatusCode));
        }
        catch (Exception e)
        {
            responseStatusCode = ERROR_RESPONSE;
            logger.info("flight search request failed due to exception {} " , e);
            //TODO errors can be externalized and handled properly, but as of now its out of POC scope
            ErrorResponse errorResponse = new ErrorResponse("ERR_001","Unknown Exception Occured");
            return new ResponseEntity<>(errorResponse, constructResponseHeader(), resolveHttpStatus(responseStatusCode));
        }
        finally {
            logger.info("flight search request completed");
        }

        return new ResponseEntity<>(response, constructResponseHeader(), resolveHttpStatus(responseStatusCode));
    }

    @GetMapping ("/flights")
    private ResponseEntity<?> searchFlight(@RequestHeader HttpHeaders headers) {
        return new ResponseEntity<>(flightSearchService.getAllFlights(),resolveHttpStatus(SUCCESS_RESPONSE));
    }

}
