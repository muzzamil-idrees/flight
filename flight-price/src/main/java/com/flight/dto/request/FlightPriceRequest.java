package com.flight.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Objects;

public class FlightPriceRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private String flightDate;//Assuming its Date only field, hence formatter is selected as Date only
    private String flightNumber;

    public FlightPriceRequest() {
    }

    public FlightPriceRequest(String flightNumber, String date) {
        this.flightNumber = flightNumber;
        this.flightDate = date;

    }
    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public String toString() {
        return "FlightRequest{" +
                ", flightNumber='" + flightNumber + '\'' +
                ", flightDate='" + flightDate + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightPriceRequest that = (FlightPriceRequest) o;
        return Objects.equals(flightNumber, that.flightNumber)
                && Objects.equals(flightDate, that.flightDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightNumber, flightDate);
    }


}

