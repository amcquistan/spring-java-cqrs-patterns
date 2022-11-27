package com.thecodinginterface.pricing.config;

import com.thecodinginterface.pricing.domain.model.ProductPricingEntity;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.application.name}")
    String appName;

    @Value("${spring.kafka.bootstrap-servers}")
    String bootstrapServers;

    @Value("${kafka-topics.pricing}")
    String pricingTopic;

    @Value("${kafka-topics.dlt-suffix}")
    String dltSuffix;

    @Value("${kafka-topics.partitions}")
    int partitions;

    @Value("${kafka-topics.replicas}")
    int replicas;

    @Value("${kafka-topics.backoff.initial-interval}")
    Duration backoffInitInterval;

    @Value("${kafka-topics.backoff.max-interval}")
    Duration backoffMaxInterval;

    @Value("${kafka-topics.backoff.max-retries}")
    int backoffMaxRetries;

    @Value("${kafka-topics.backoff.multiplier}")
    int backoffMultiplier;

    @Bean
    public KafkaAdmin.NewTopics topics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name(pricingTopic)
                        .partitions(partitions)
                        .replicas(replicas)
                        .build(),
                TopicBuilder.name(pricingTopic + dltSuffix)
                        .partitions(partitions)
                        .replicas(replicas)
                        .build()
        );
    }

    public Map<String, Object> producerConfig() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.LongSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class
        );
    }

    @Bean
    public ProducerFactory<?, ?> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    public Map<String, Object> consumerConfig() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, appName,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        );
    }

    @Bean
    public ConsumerFactory<Long, ProductPricingEntity> consumerFactory() {
        return new DefaultKafkaConsumerFactory<Long, ProductPricingEntity>(
            consumerConfig(),
            new LongDeserializer(),
            new JsonDeserializer<>(ProductPricingEntity.class)
        );
    }

    @Bean
    public DefaultErrorHandler defaultErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        var backoff = new ExponentialBackOffWithMaxRetries(backoffMaxRetries);
        backoff.setInitialInterval(backoffInitInterval.toMillis());
        backoff.setMultiplier(backoffMultiplier);
        backoff.setMaxInterval(backoffMaxInterval.toMillis());
        return new DefaultErrorHandler(recoverer, backoff);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, ProductPricingEntity> productPricingListenerFactory(KafkaTemplate<Object, Object> kafkaTemplate) {
        var factory = new ConcurrentKafkaListenerContainerFactory<Long, ProductPricingEntity>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setCommonErrorHandler(defaultErrorHandler(kafkaTemplate));
        return factory;
    }
}
