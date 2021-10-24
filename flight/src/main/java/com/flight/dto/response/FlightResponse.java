package com.flight.dto.response;

import java.util.List;

public class FlightResponse {

    private String flightNumber;


    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public String toString() {
        return "FlightResponse{" +
                "flightNumber='" + flightNumber + '\'' +
                '}';
    }
}
