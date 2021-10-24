package com.flight.service;

import com.flight.dto.request.FlightRequest;
import com.flight.dto.response.FlightResponse;

import java.text.ParseException;
import java.time.format.DateTimeParseException;


public interface IFlightSearchService {

    FlightResponse searchFlight(FlightRequest flightRequest) throws DateTimeParseException, Exception;

}
