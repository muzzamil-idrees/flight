package com.flight.service;

import com.flight.dto.request.FlightPriceRequest;
import com.flight.dto.response.FlightPriceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FlightPriceService implements IFlightPriceSearchService {


        private final String datePattern = "yyyy-MM-dd";

        private static ConcurrentHashMap<FlightPriceRequest, String> flightPriceRequestToPriceMap = new ConcurrentHashMap<FlightPriceRequest, String>();

        static {//Initial Values, after that dynamically new values will be added as flight number, but fliqhts will only will be available as mentioned below in list
            flightPriceRequestToPriceMap.put(new FlightPriceRequest("EK123", "2021-10-21"), "1000");
            flightPriceRequestToPriceMap.put(new FlightPriceRequest("EK456", "2021-10-21"), "2000");
            flightPriceRequestToPriceMap.put(new FlightPriceRequest("EK789", "2021-10-21"), "3000");
        }

        private static final Logger logger = LoggerFactory.getLogger(FlightPriceService.class);

        @Autowired
        private FlightPriceRuleEngine flightPriceRuleEngine;


    /**
     * This function will be used to fetch flight price against date, flight number.
     *
     * @param flightPriceRequest It should contain the date, flight number
     * @return It will return flight price
     * @throws DateTimeParseException It will be thrown if client will pass date in wrong format
     */
        @Override
        public FlightPriceResponse searchFlightPrice(FlightPriceRequest flightPriceRequest) throws DateTimeParseException, Exception {

            logger.info("start searchFlightPrice with request {}", flightPriceRequest);
            long startTime = System.currentTimeMillis();

            FlightPriceResponse response = new FlightPriceResponse();

            LocalDate dateParsed = LocalDate.parse(flightPriceRequest.getFlightDate(), DateTimeFormatter.ISO_LOCAL_DATE);

            //If parsing is successsfull then proceed
            response.setPrice(getFlightPrice(flightPriceRequest));

            logger.info("searchFlightPrice request completed");
            logger.info("searchFlightPrice request completed, Elapsed time: " + (System.currentTimeMillis() - startTime));

            return response;

        }

        //TODO response time to change
        private String getFlightPrice(FlightPriceRequest flightPriceRequest) {
            logger.info("getting flight price");

            String finalFlightNumber = flightPriceRequest.getFlightNumber();
            //Idenitify the record of flight based on the flight request
            Optional<String> flightPriceFromMap = flightPriceRequestToPriceMap.entrySet().stream().
                    filter(entry -> entry.getKey().getFlightNumber().equalsIgnoreCase(finalFlightNumber) && entry.getKey().getFlightDate().equalsIgnoreCase(flightPriceRequest.getFlightDate()))
                    .map(Map.Entry :: getValue).findFirst();

            if (flightPriceFromMap.isEmpty()) {
                return "No Flight Price available";
            }

            //Apply Rule Engine rules
            flightPriceRuleEngine.applyRules(flightPriceRequest);

            String flightPrice = flightPriceFromMap.get();
            logger.info("returning flight price {} ", flightPrice);
            return flightPrice;
        }

}
