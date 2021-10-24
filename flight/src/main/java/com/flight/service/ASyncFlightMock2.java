package com.flight.service;

import com.flight.utils.FlightConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class ASyncFlightMock2 {

    private static final Logger logger = LoggerFactory.getLogger(ASyncFlightMock2.class);

    @Async("asyncThreadPoolTaskExecutor")
    public CompletableFuture<String> asyncMethodWithReturnType(String requestID, int randomProcessingDelay) {

        logger.info("start asyncMethodWithReturnType with request {} and delay {}", requestID, randomProcessingDelay);

        try {

            CompletableFuture future = CompletableFuture.completedFuture("ASync - Thread " + Thread.currentThread().getName());
            Thread.sleep(randomProcessingDelay);
            return future;
        } catch (InterruptedException e) {
            logger.error("Exception occured ",e);
        }

        return null;
    }
}
