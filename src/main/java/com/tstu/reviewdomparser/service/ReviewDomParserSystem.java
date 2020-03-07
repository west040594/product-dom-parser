package com.tstu.reviewdomparser.service;

import com.tstu.commons.dto.rabbit.request.ProductReviewParseRequest;

public interface ReviewDomParserSystem {
    void execute(ProductReviewParseRequest request);
    void fallback(ProductReviewParseRequest request, Throwable throwable);
}
