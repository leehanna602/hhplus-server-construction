package com.hhplus.server.interfaces.kafka;

public interface KafkaMessageConsumer<T> {

    void consume(T message);

}