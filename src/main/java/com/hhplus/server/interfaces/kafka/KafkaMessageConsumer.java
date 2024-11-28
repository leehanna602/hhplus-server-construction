package com.hhplus.server.interfaces.kafka;

public interface KafkaMessageConsumer {

    void consume(String message);

}