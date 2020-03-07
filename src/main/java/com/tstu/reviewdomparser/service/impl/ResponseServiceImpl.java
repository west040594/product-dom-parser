package com.tstu.reviewdomparser.service.impl;

import com.tstu.commons.dto.rabbit.enums.ResponseStatus;
import com.tstu.commons.dto.rabbit.response.ProductParseResponse;
import com.tstu.commons.dto.rabbit.response.RabbitClientResponse;
import com.tstu.commons.dto.rabbit.response.RabbitResponseError;
import com.tstu.reviewdomparser.exception.ReviewDomParserErrors;
import com.tstu.reviewdomparser.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final RabbitClientImpl rabbitClient;

    @Override
    public void process(ProductParseResponse productResponse) {
        log.info("Отзывы получены. Отправление успешного ответа в product-info");
        RabbitClientResponse<ProductParseResponse> response = RabbitClientResponse.<ProductParseResponse>builder()
                .actualTimestamp(Instant.now())
                .system(productResponse.getReviewSystem())
                .status(ResponseStatus.SUCCESS)
                .data(productResponse)
                .build();
        rabbitClient.send(response);
    }

    @Override
    public void process(Throwable throwable) {
        log.info("Не удалось получить отзывы. Отправление ошибочного ответа в product-info");
        RabbitClientResponse response = RabbitClientResponse.builder()
                .actualTimestamp(Instant.now())
                .status(ResponseStatus.ERROR)
                .error(getResponseError(throwable))
                .build();
        rabbitClient.send(response);
    }


    private RabbitResponseError getResponseError(Throwable throwable) {
        ReviewDomParserErrors reviewError = ReviewDomParserErrors.getByDescription(throwable.getMessage());
        return new RabbitResponseError(
                UUID.randomUUID().toString(),
                reviewError.getErrorCode().toString(),
                reviewError.getErrorDescription());
    }
}
