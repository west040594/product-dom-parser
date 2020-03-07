package com.tstu.reviewdomparser.service;


import com.tstu.commons.dto.rabbit.request.ProductReviewParseRequest;
import com.tstu.commons.dto.rabbit.response.ProductParseResponse;
import com.tstu.commons.dto.rabbit.response.ReviewParseResponse;

import java.util.List;
import java.util.Set;

public interface ParseService {
    ProductParseResponse getProductInfo(ProductReviewParseRequest request);
    Set<ReviewParseResponse> formReviewSet(List<String> reviewLinkList);
}
