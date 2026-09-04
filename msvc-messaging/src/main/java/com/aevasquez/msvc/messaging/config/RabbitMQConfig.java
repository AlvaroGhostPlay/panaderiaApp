package com.aevasquez.msvc.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

@Configuration
public class RabbitMQConfig {

    private final MessagingProperties properties;

    public RabbitMQConfig(MessagingProperties properties) {
        this.properties = properties;
    }

    //crea la o las colas
    @Bean
    public Queue notificationQueue(){
        return QueueBuilder
                .durable(properties.getNotification().getQueue())
                .build();
    }

    @Bean
    public Queue pushQueue(){
        return QueueBuilder
                .durable(properties.getPush().getQueue())
                .build();
    }

    @Bean
    public Queue smsQueue(){
        return QueueBuilder
                .durable(properties.getSms().getQueue())
                .build();
    }

    //cra quien recibe las peticiones
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(properties.getExchange().getName());
    }

    //le dice al que recibe los mensaje a que cola ir con el routin key
    @Bean
    public Binding notificationBinding(
            Queue notificationQueue,
            DirectExchange emailExchange) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(emailExchange)
                .with(properties.getNotification().getRoutingKey());
    }

    @Bean
    public Binding psuhBinding(
            Queue pushQueue,
            DirectExchange emailExchange) {

        return BindingBuilder
                .bind(pushQueue)
                .to(emailExchange)
                .with(properties.getPush().getRoutingKey());
    }

    @Bean
    public Binding smsBinding(
            Queue smsQueue,
            DirectExchange emailExchange) {

        return BindingBuilder
                .bind(smsQueue)
                .to(emailExchange)
                .with(properties.getSms().getRoutingKey());
    }


    /*Esta es la forma de hacerlo quemado
    //colas
    public static final String EMAIL_NOTIFICATION = "notification.queue";
    public static final String SMS_QUEUE = "sms.queue";
    public static final String PUSH_QUEUE = "push.queue";

    //quien recibe los mensaje con llave
    public static final String EMAIL_EXCHANGE = "email.exchange";

    //LLaves
    public static final String EMAIL_ROUTING_KEY = "notification.send";
    public static final String SMS_ROUTING_KEY = "sms.send";
    public static final String PUSH_ROUTING_KEY = "push.send";



        //crea la o las colas
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder
                .durable(EMAIL_NOTIFICATION)
                .build();
    }

    @Bean
    public Queue smsQueue() {
        return QueueBuilder
                .durable(SMS_QUEUE)
                .build();
    }

    @Bean
    public Queue pushQueue() {
        return QueueBuilder
                .durable(PUSH_QUEUE)
                .build();
    }


    //cra quien recibe las peticiones
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }


    //le dice al que recibe los mensaje a que cola ir con el routin key
    @Bean
    public Binding notificationBinding(
            Queue notificationQueue,
            DirectExchange emailExchange) {

        return BindingBuilder
                .bind(notificationQueue)
                .to(emailExchange)
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding smsBinding(
            Queue smsQueue,
            DirectExchange notificationExchange) {

        return BindingBuilder
                .bind(smsQueue)
                .to(notificationExchange)
                .with(SMS_ROUTING_KEY);
    }

    @Bean
    public Binding pushBinding(
            Queue pushQueue,
            DirectExchange notificationExchange) {

        return BindingBuilder
                .bind(pushQueue)
                .to(notificationExchange)
                .with(PUSH_ROUTING_KEY);
    }

     */
}
