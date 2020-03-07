package com.tstu.reviewdomparser.service.impl;

import com.tstu.commons.constants.ReviewSystemsConstants;
import com.tstu.commons.dto.rabbit.request.ProductReviewParseRequest;
import com.tstu.commons.dto.rabbit.response.ProductParseResponse;
import com.tstu.commons.dto.rabbit.response.ReviewParseResponse;
import com.tstu.reviewdomparser.service.ParseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class IRecommendParseServiceImpl implements ParseService {

    private final WebDriver driver;

    @Override
    public ProductParseResponse getProductInfo(ProductReviewParseRequest request) {
        Document html = formHtmlDocument(request.getUrl());
        String productRating = html.getElementsByClass("average-rating").first().child(0).text();
        String imageUrl = html.select(".mainpic img").first().attr("src");
        int pageCount = 1;
        ProductParseResponse productResponse = ProductParseResponse.builder()
                .reviewSystem(request.getReviewSystem())
                .productId(request.getProductId())
                .rating(StringUtils.isEmpty(productRating) ? null : productRating)
                .imageUrl(imageUrl)
                .build();
        List<String> reviewLinkList = createReviewLinkList(html.getElementsByClass("list-comments").first());
        String firstLinkOnCurrentPage = null;
        String firstLinkOnNextPage = null;
        do {
            //формируем набор отзывов на текущей странице
            firstLinkOnCurrentPage = reviewLinkList.get(0);
            Set<ReviewParseResponse> reviewResponses = formReviewSet(reviewLinkList);
            productResponse.addReviewItems(reviewResponses);
            //формируем следующую страницу и набор ссылок на следующие отзывы
            html = formHtmlDocument(request.getUrl()+"?page="+pageCount);
            reviewLinkList = createReviewLinkList(html.getElementsByClass("list-comments").first());
            firstLinkOnNextPage = reviewLinkList.get(0);
            pageCount++;
            log.info("Первая ссылка на текущей странице - {} \n Первая ссылка на следующй странице - {}", firstLinkOnCurrentPage, firstLinkOnNextPage);
        }while (!firstLinkOnCurrentPage.equals(firstLinkOnNextPage));
        return productResponse;
    }

    @Override
    public Set<ReviewParseResponse> formReviewSet(List<String> reviewLinkList) {
        Set<ReviewParseResponse> reviewResponses = new LinkedHashSet<>();

        for (String reviewLink : reviewLinkList) {
            Document html = formHtmlDocument(reviewLink);
            String rating = html.select(".starsRating meta[itemprop=ratingValue]").attr("content");
            ReviewParseResponse reviewResponse = ReviewParseResponse.builder()
                    .username(html.select(".reviewer a").text())
                    .rating(StringUtils.isEmpty(rating) ? null : rating)
                    .postTime(LocalDateTime.parse(html.select(".dtreviewed meta").attr("content"), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    .title(html.select(".reviewTitle a").text())
                    .readLink(reviewLink)
                    .pluses(formQualityItems(html.select(".plus ul").first()))
                    .minuses(formQualityItems(html.select(".minus ul").first()))
                    .body(html.select(".reviewText .description").text())
                    .build();
            log.info("Получен отзыв - {}", reviewResponse);
            reviewResponses.add(reviewResponse);
        }
        return reviewResponses;
    }

    private Document formHtmlDocument(String url) {
        driver.get(url);
        String pageSource = driver.getPageSource();
        return  Jsoup.parse(pageSource);
    }

    private List<String> createReviewLinkList(Element ulElement) {
        List<String> reviewLinkList = new ArrayList<>();
        Elements listCommentsElements = ulElement.select("li");
        for (Element listCommentsElement : listCommentsElements) {
            String href = listCommentsElement.select(".reviewTitle a").attr("href");
            reviewLinkList.add(ReviewSystemsConstants.IRECOMMEND_DOMAIN_NAME + href);
        }
        return reviewLinkList;
    }

    private List<String> formQualityItems(Element ulElement) {
        List<String> qualityItems = new LinkedList<>();
        if(ulElement == null) {
            return qualityItems;
        }
        Elements plusMinusElements = ulElement.select("li");
        for (Element element : plusMinusElements) {
            qualityItems.add(element.text());
        }
        return qualityItems;
    }
}
