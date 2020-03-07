package com.tstu.reviewdomparser.service.impl;

import com.tstu.commons.dto.rabbit.request.ProductReviewParseRequest;
import com.tstu.commons.dto.rabbit.response.ProductParseResponse;
import com.tstu.commons.dto.rabbit.response.ReviewParseResponse;
import com.tstu.reviewdomparser.service.ParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class OtzovikParseServiceImpl implements ParseService {
    @Override
    public ProductParseResponse getProductInfo(ProductReviewParseRequest request) {
        return null;
    }

    @Override
    public Set<ReviewParseResponse> formReviewSet(List<String> reviewLinkList) {
        return null;
    }
}
