package edu.dosw.rideci.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQPaymentsConfig {

    public static final String PAYMENTS_EXCHANGE = "rideci.payments.exchange";
    public static final String PAYMENTS_EVENTS_QUEUE = "rideci.payments.events";
    public static final String PAYMENTS_DLQ_QUEUE = "rideci.payments.events.dlq";
    public static final String PAYMENTS_ROUTING_KEY = "events.payments.#";
    public static final String PAYMENTS_DLQ_ROUTING_KEY = "events.payments.dlq";

    @Bean
    public DirectExchange paymentsExchange() {
        return new DirectExchange(PAYMENTS_EXCHANGE, true, false);
    }

    @Bean
    public Queue paymentsEventsQueue() {
        return QueueBuilder.durable(PAYMENTS_EVENTS_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENTS_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PAYMENTS_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue paymentsDeadLetterQueue() {
        return QueueBuilder.durable(PAYMENTS_DLQ_QUEUE).build();
    }

    @Bean
    public Binding paymentsBinding() {
        return BindingBuilder
                .bind(paymentsEventsQueue())
                .to(paymentsExchange())
                .with(PAYMENTS_ROUTING_KEY);
    }

    @Bean
    public Binding paymentsDlqBinding() {
        return BindingBuilder
                .bind(paymentsDeadLetterQueue())
                .to(paymentsExchange())
                .with(PAYMENTS_DLQ_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate paymentsRabbitTemplate(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
