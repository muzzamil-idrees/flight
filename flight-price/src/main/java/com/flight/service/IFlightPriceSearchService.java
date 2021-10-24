package com.flight.service;

import com.flight.dto.request.FlightPriceRequest;
import com.flight.dto.response.FlightPriceResponse;

import java.time.format.DateTimeParseException;


public interface IFlightPriceSearchService {

    FlightPriceResponse searchFlightPrice(FlightPriceRequest flightPriceRequest) throws DateTimeParseException, Exception;

}
