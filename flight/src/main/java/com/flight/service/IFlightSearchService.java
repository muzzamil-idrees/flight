package com.flight.service;

import com.flight.dto.request.FlightRequest;
import com.flight.dto.response.FlightResponse;

import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ConcurrentHashMap;


public interface IFlightSearchService {

    FlightResponse searchFlight(FlightRequest flightRequest) throws DateTimeParseException, Exception;

    public ConcurrentHashMap<FlightRequest,String> getAllFlights();
}
