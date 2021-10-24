package com.flight.dto.response;

public class FlightPriceResponse {

    private String price;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "FlightPriceResponse{" +
                "price='" + price + '\'' +
                '}';
    }
}
