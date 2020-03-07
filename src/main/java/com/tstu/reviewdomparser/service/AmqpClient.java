package com.tstu.reviewdomparser.service;

import com.tstu.commons.dto.rabbit.request.RabbitClientRequest;
import com.tstu.commons.dto.rabbit.response.RabbitClientResponse;
import org.springframework.messaging.Message;

public interface AmqpClient {
    void receive(RabbitClientRequest request);
    void send(RabbitClientResponse response);
}
