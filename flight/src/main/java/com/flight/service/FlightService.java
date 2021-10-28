package com.flight.service;

import com.flight.utils.FlightConstants;
import com.flight.dto.request.FlightRequest;
import com.flight.dto.response.FlightResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FlightService implements IFlightSearchService {


        private final String datePattern = "yyyy-MM-dd";

        @Value("${app.flight.number.change.interval.mins}")
        int flightNumberChangeIntervalMins;

        private static ConcurrentHashMap<String, LocalDateTime> flightNumberToDateMap = new ConcurrentHashMap<String, LocalDateTime>();

        private static ConcurrentHashMap<FlightRequest, String> flightRequestToFlightNumberMap = new ConcurrentHashMap<FlightRequest, String>();

        static {//Initial Values, after that dynamically new values will be added as flight number, but fliqhts will only will be available as mentioned below in list
            flightRequestToFlightNumberMap.put(new FlightRequest("DXB","LHE", "2021-10-21"), "EK123");
            flightRequestToFlightNumberMap.put(new FlightRequest("DXB","LHR", "2021-10-21"), "EK456");
            flightRequestToFlightNumberMap.put(new FlightRequest("DXB","ISB", "2021-10-21"), "EK789");
        }

        private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

        @Autowired
        private ASyncFlightMock1 mock1;

        @Autowired
        private ASyncFlightMock2 mock2;

        @Autowired
        private ASyncFlightMock3 mock3;

        @Autowired
        private ASyncFlightMock4 mock4;

        @Autowired
        private ASyncFlightMock5 mock5;


    /**
     * This function will be used to fetch flight number against date, arrival/departure information and it will initiate 5 independent connections and change flights based on schedule.
     *
     * @param flightRequest It should contain the date, arrival/departure information
     * @return It will return flight number which will be changing after configurable time interval and only limited flights are available as configured in this class
     * @throws DateTimeParseException It will be thrown if client will pass date in wrong format
     */
        @Override
        public FlightResponse searchFlight(FlightRequest flightRequest) throws DateTimeParseException, Exception {

            logger.info("start searchFlight with request {}", flightRequest);

            FlightResponse response = new FlightResponse();

            searchFlightAsyncInternal();// As per requirement, Mocked independent downstream connections will be established


            LocalDate dateParsed = LocalDate.parse(flightRequest.getFlightDate(), DateTimeFormatter.ISO_LOCAL_DATE);

            //If parsing is successsfull then proceed
            response.setFlightNumber(getFlightNumber(flightRequest.getFlightDate(),
                    flightRequest.getAirportOfDepartureCode(),
                    flightRequest.getAirportOfArrivalCode()));

            logger.info("searchFlight request completed with response {}",response);
            return response;

        }

        private String getFlightNumber(String date, String airportOfDepartureCode, String airportOfArrivalCode) {
            logger.info("getting flight number");


            //Idenitfy the record of flight based on the flight request
            Optional<String> flightNumberFromMap = flightRequestToFlightNumberMap.entrySet().stream().
                    filter(entry -> entry.getKey().getAirportOfArrivalCode().equalsIgnoreCase(airportOfArrivalCode)
                            && entry.getKey().getAirportOfDepartureCode().equalsIgnoreCase(airportOfDepartureCode)
                    && entry.getKey().getFlightDate().equalsIgnoreCase(date))
                    .map(Map.Entry :: getValue).findFirst();

            if (flightNumberFromMap.isEmpty()) {
                return "No Flight available";
            }

            String flightNumber = flightNumberFromMap.get();
            LocalDateTime dateOfFlightNow = LocalDateTime.now();

            //it means flight entry exists, so validate date if 24 hours passed then change it, else return same flight number

            String finalFlightNumber = flightNumber;
            Optional<LocalDateTime> dateValueOfFlightFromMap = flightNumberToDateMap.entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(finalFlightNumber))
                    .map(Map.Entry::getValue).findFirst();

            if(dateValueOfFlightFromMap.isPresent()) {
                logger.info("date value {} against flight {} in map present ",dateValueOfFlightFromMap, flightNumber);//TODO compare it now and if 24 hour passed, set new value in map

                LocalDateTime currentDatetime = LocalDateTime.now();

                logger.info("Flight number change interval in mins is {}",flightNumberChangeIntervalMins);

                if(currentDatetime.minusMinutes(flightNumberChangeIntervalMins)
                                .isAfter(dateValueOfFlightFromMap.get())) {
                    //It means time passed, so chnage flight number and updatemap also
                    String oldFlightNumber = flightNumber;
                    flightNumber = "EK-"+UUID.randomUUID().toString();
                    flightNumberToDateMap.put(flightNumber, currentDatetime);//add new flight info
                    flightRequestToFlightNumberMap.put(new FlightRequest(airportOfDepartureCode, airportOfArrivalCode, currentDatetime.format(DateTimeFormatter.ISO_LOCAL_DATE)),flightNumber);
                    flightNumberToDateMap.remove(oldFlightNumber);//remove old value

                    logger.info("Flight Number {} changed to {} with new Date {} in map ",oldFlightNumber, flightNumber, dateValueOfFlightFromMap.get());
                }
            }
            else {
                flightNumberToDateMap.put(flightNumber, dateOfFlightNow);
                logger.info("Flight Number {} with Date {} added in map ",flightNumber, dateOfFlightNow);
            }

            logger.info("returning flight number");
            return flightNumber;
        }

        private void searchFlightAsyncInternal() throws Exception{

            logger.info("start searchFlightAsyncInternal with request");

            String currentThreadName = Thread.currentThread().getName();
            long startTime = System.currentTimeMillis();

            Double randomNo = ((Math.random() * (FlightConstants.THREAD_SLEEP_MAX_TIME - FlightConstants.THREAD_SLEEP_MIN_TIME)) + FlightConstants.THREAD_SLEEP_MIN_TIME);
            int randomProcessingDelay = randomNo.intValue()/5; //As we have created 5 mocks below, so dividing the random number by 5 so that elapsed time will remain within SLA

            logger.info("Async calls time randomly set to {} for each thread ",randomProcessingDelay);

            CompletableFuture<String> future1 = mock1.asyncMethodWithReturnType(currentThreadName,randomProcessingDelay);
            logger.info("Asynchronous process initiated - {}", future1.get());

            CompletableFuture<String> future2 = mock2.asyncMethodWithReturnType(currentThreadName, randomProcessingDelay);
            logger.info("Asynchronous process initiated - {}", future2.get());

            CompletableFuture<String> future3 = mock3.asyncMethodWithReturnType(currentThreadName,randomProcessingDelay);
            logger.info("Asynchronous process initiated - {}", future3.get());

            CompletableFuture<String> future4 = mock4.asyncMethodWithReturnType(currentThreadName, randomProcessingDelay);
            logger.info("Asynchronous process initiated - {}", future4.get());

            CompletableFuture<String> future5 = mock5.asyncMethodWithReturnType(currentThreadName,randomProcessingDelay);
            logger.info("Asynchronous process initiated - {}", future5.get());

            CompletableFuture.allOf(future1,future2,future3,future4,future5).join();

            logger.info("All Responses Received, Elapsed time: " + (System.currentTimeMillis() - startTime));

        }

}
