package com.flight.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Objects;

public class FlightRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private String flightDate;//Assuming its Date only field, hence formatter is selected as Date only
    private String airportOfDepartureCode;//As of now there is no lookup reference data is available so considering it as String valu
    private String airportOfArrivalCode;//As of now there is no lookup reference data is available so considering it as String value
    public FlightRequest() {
    }

    public FlightRequest (String airportOfDepartureCode, String airportOfArrivalCode, String date) {
        this.airportOfDepartureCode = airportOfDepartureCode;
        this.airportOfArrivalCode =  airportOfArrivalCode;
        this.flightDate = date;

    }
    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getAirportOfDepartureCode() {
        return airportOfDepartureCode;
    }

    public void setAirportOfDepartureCode(String airportOfDepartureCode) {
        this.airportOfDepartureCode = airportOfDepartureCode;
    }

    public String getAirportOfArrivalCode() {
        return airportOfArrivalCode;
    }

    public void setAirportOfArrivalCode(String airportOfArrivalCode) {
        this.airportOfArrivalCode = airportOfArrivalCode;
    }

    @Override
    public String toString() {
        return "FlightRequest{" +
                "date='" + flightDate + '\'' +
                ", airportOfDepartureCode='" + airportOfDepartureCode + '\'' +
                ", flightDate='" + flightDate + '\'' +
                ", airportOfArrivalCode='" + airportOfArrivalCode + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightRequest that = (FlightRequest) o;
        return Objects.equals(airportOfDepartureCode, that.airportOfDepartureCode) && Objects.equals(airportOfArrivalCode, that.airportOfArrivalCode)
                && Objects.equals(flightDate, that.flightDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportOfDepartureCode, airportOfArrivalCode, flightDate);
    }


}

