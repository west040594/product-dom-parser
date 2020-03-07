package com.tstu.reviewdomparser.service.impl;

import com.tstu.commons.dto.rabbit.request.RabbitClientRequest;
import com.tstu.commons.exception.PrsException;
import com.tstu.commons.model.enums.ReviewSystem;
import com.tstu.reviewdomparser.exception.ReviewDomParserErrors;
import com.tstu.reviewdomparser.service.ParseService;
import com.tstu.reviewdomparser.service.ParseSystemDecision;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseSystemDecisionImpl implements ParseSystemDecision {
    private final ApplicationContext applicationContext;
    public ParseService defineService(String system) {
        ReviewSystem reviewSystem = ReviewSystem.fromValue(system);
        switch (reviewSystem) {
            case OTZOVIK:
                return applicationContext.getBean(OtzovikParseServiceImpl.class);
            case IRECOMMEND:
                return applicationContext.getBean(IRecommendParseServiceImpl.class);
            default:
                throw new PrsException(ReviewDomParserErrors.UNSUPPORTED_REVIEW_SYSTEM);
        }
    }
}
