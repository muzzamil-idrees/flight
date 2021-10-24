package com.flight.service;

import com.flight.dto.request.FlightPriceRequest;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Component
public class FlightPriceRuleEngine {

    private static StringBuilder ruleEngineStringBuilder = new StringBuilder();

    @Value("${app.flight.price.rule.engine.memory}")
    int ruleEngineMemorySize;

    private static final int oneMB = 1048576;

    private static final Logger logger = LoggerFactory.getLogger(FlightPriceRuleEngine.class);

    @Async("asyncThreadPoolTaskExecutor")
    public CompletableFuture<String> asyncInitializeRuleEngine(String requestID) {

        logger.info("start asyncInitializeRuleEngine with request {}", requestID);


        InputStream inputStream = null;

        try {

            CompletableFuture future = CompletableFuture.completedFuture("ASync - Thread " + Thread.currentThread().getName());
            //initialize the ruleEngine and reserve memory as per ruleEngineMemorySize in rulEngine

            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("application.properties").getFile());

            BufferedReader br
                    = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                ruleEngineStringBuilder.append(line).append("\n");
            }

            String propertyFile = ruleEngineStringBuilder.toString();

            //Multiple the file size to reservice memory as per ruleEngineMemorySize configuration
            long ruleEngineMemorySizeInBytes = Math.multiplyFull(oneMB,ruleEngineMemorySize);
            while (ruleEngineStringBuilder.length() <= ruleEngineMemorySizeInBytes) {
                ruleEngineStringBuilder.append(propertyFile);
            }

            return future;
        } catch (Exception e) {
            logger.error("Exception occured ",e);
        }
        finally {
            logger.info(" asyncInitializeRuleEngine with request {} completed, memory reserved {} ", requestID, ruleEngineStringBuilder.length());

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public void applyRules(FlightPriceRequest request) {
        //Just access the ruleEngine and do nothing and return
        logger.info("Flight Price Rule Engine Applied, Occupying Memory {}", ruleEngineStringBuilder.length());
        return;
    }

}
