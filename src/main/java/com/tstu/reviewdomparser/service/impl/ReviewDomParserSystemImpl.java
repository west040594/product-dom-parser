package com.tstu.reviewdomparser.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.tstu.commons.dto.rabbit.request.ProductReviewParseRequest;
import com.tstu.commons.dto.rabbit.response.ProductParseResponse;
import com.tstu.reviewdomparser.service.ParseService;
import com.tstu.reviewdomparser.service.ParseSystemDecision;
import com.tstu.reviewdomparser.service.ResponseService;
import com.tstu.reviewdomparser.service.ReviewDomParserSystem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewDomParserSystemImpl implements ReviewDomParserSystem {

    private final ParseSystemDecision parseSystemDecision;
    private final ResponseService responseService;
    private final WebDriver driver;

    @Override
    @HystrixCommand(
            fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "6000000"),
                    //@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "400000"),
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "false"),
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30")
            }
    )
    public void execute(ProductReviewParseRequest request) {
        ParseService parseService = parseSystemDecision.defineService(request.getReviewSystem());
        ProductParseResponse productResponse = parseService.getProductInfo(request);
        responseService.process(productResponse);
    }

    @Override
    public void fallback(ProductReviewParseRequest request, Throwable throwable) {
        log.info("Произошла ошибка. Error - {}. StackTrace - {}",
                throwable.getMessage(), throwable.getStackTrace());
        //log.info("Выходим из браузера");
        responseService.process(throwable);
    }
}
