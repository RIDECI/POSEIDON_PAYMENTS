package edu.dosw.rideci.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQAuthConfig {

    // Configuración para ESCUCHAR eventos de Autenticación
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String PAYMENT_USER_EVENTS_QUEUE = "payment.user.events.queue";
    public static final String USER_ROUTING_KEY = "auth.user.#";

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE, true, false);
    }

    @Bean
    public Queue paymentUserEventsQueue() {
        return new Queue(PAYMENT_USER_EVENTS_QUEUE, true);
    }

    @Bean
    public Binding paymentUserEventsBinding() {
        return BindingBuilder
                .bind(paymentUserEventsQueue())
                .to(userExchange())
                .with(USER_ROUTING_KEY);
    }
}
