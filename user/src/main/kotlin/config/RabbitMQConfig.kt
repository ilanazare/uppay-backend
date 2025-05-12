package com.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    companion object {
        const val EXCHANGE_NAME = "message_exchange"
        const val QUEUE_NAME = "message_queue"
        const val ROUTING_KEY = "message_routing_key"
    }

    @Bean
    fun exchange() = DirectExchange(EXCHANGE_NAME)

    @Bean
    fun queue() = Queue(QUEUE_NAME)

    @Bean
    fun binding(
        queue: Queue,
        exchange: DirectExchange,
    ): Binding =
        BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(ROUTING_KEY)

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory)
        template.messageConverter = jsonMessageConverter()
        return template
    }

    @Bean
    fun jsonMessageConverter(): MessageConverter = Jackson2JsonMessageConverter()
}
