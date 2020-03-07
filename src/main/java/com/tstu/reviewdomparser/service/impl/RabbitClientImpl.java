package com.tstu.reviewdomparser.service.impl;

import com.tstu.commons.dto.rabbit.request.RabbitClientRequest;
import com.tstu.commons.dto.rabbit.response.RabbitClientResponse;
import com.tstu.reviewdomparser.service.AmqpClient;
import com.tstu.reviewdomparser.service.ReviewDomParserSystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitClientImpl implements AmqpClient {

    private ReviewDomParserSystem parserSystem;
    private AmqpTemplate template;

    @Value("${rabbit.product-info.exchange}")
    private String exchange;

    public RabbitClientImpl(@Lazy ReviewDomParserSystem parserSystem, AmqpTemplate template) {
        this.parserSystem = parserSystem;
        this.template = template;
    }

    @Override
    @RabbitListener(queues = "${rabbit.product-info.inQueue}")
    public void receive(RabbitClientRequest request) {
        log.info("Получено сообщение - {}", request);
        parserSystem.execute(request.getData());
    }

    @Override
    public void send(RabbitClientResponse response) {
        log.info("Отправка сообщения");
        template.convertAndSend(exchange, "RS", response);
    }
}
