package com.tstu.reviewdomparser.service;

import com.tstu.commons.dto.rabbit.response.ProductParseResponse;

public interface ResponseService {
    void process(ProductParseResponse productResponse);
    void process(Throwable throwable);
}
