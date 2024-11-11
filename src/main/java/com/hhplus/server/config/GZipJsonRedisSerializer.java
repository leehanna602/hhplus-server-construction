package com.hhplus.server.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

class GZipJsonRedisSerializer<T> implements RedisSerializer<T> {
    private static final int BUFFER_SIZE = 1024 * 4; // 4KB buffer
    private static final int GZIP_COMPRESSION_LEVEL = 6; // 기본값: 6 (0-9 사이, 높을수록 더 높은 압축률)
    private static final int COMPRESSION_THRESHOLD = 1024; // 1KB 이상일 때만 압축

    private final Jackson2JsonRedisSerializer<T> jsonRedisSerializer;

    public GZipJsonRedisSerializer(Class<T> type) {
        this.jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(createObjectMapper(), type);
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return objectMapper;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }

        byte[] jsonBytes = jsonRedisSerializer.serialize(t);

        // 1KB 미만이면 그대로 반환
        assert jsonBytes != null;
        if (jsonBytes.length < COMPRESSION_THRESHOLD) {
            return jsonBytes;
        }

        // 1KB 이상이면 GZIP 압축
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream) {{
                def.setLevel(GZIP_COMPRESSION_LEVEL);
            }}) {
                gzipStream.write(jsonBytes);
            }
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Error during GZIP compression", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        // GZIP 매직 넘버 체크 (0x1f 0x8b)
        boolean isCompressed = bytes.length >= 2 &&
                (bytes[0] & 0xFF) == 0x1F &&
                (bytes[1] & 0xFF) == 0x8B;

        if (!isCompressed) {
            return jsonRedisSerializer.deserialize(bytes);
        }

        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
             GZIPInputStream gzipStream = new GZIPInputStream(byteStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = gzipStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            byte[] decompressedBytes = byteArrayOutputStream.toByteArray();
            return jsonRedisSerializer.deserialize(decompressedBytes);
        } catch (IOException e) {
            throw new SerializationException("Error during GZIP decompression", e);
        }
    }
}