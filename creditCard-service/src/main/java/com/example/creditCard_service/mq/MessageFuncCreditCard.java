package com.example.creditCard_service.mq;


import com.example.creditCard_service.business.service.CreditCardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Function;

@Configuration // spring считывает бины и создает соотв. каналы
// описываются все каналы с помощью функциональных методов
// этот способ - рекомендуемый, вместо старого способа (@Binding, интерфейсы)
public class MessageFuncCreditCard {

    // для заполнения тестовых данных
    private CreditCardService creditCardService;

    public MessageFuncCreditCard(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    // получает id пользователя и запускает создание тестовых данных
    // название метода должно совпадать с настройками definition и bindings в файлах properties (или yml)
    @Bean
    public Function<Message<Long>, String> fetchCreditCardsByUserId() {
        return message -> {
            Long customerId = message.getPayload();
            // Ваш код для получения данных или генерации сообщения
            return "The data received for customer {}: " + customerId;
        };
    }
}

    /*@Bean
    public Function<Message<Long>, String> fetchCreditCardsByUserId() {
        return message -> {
            Long customerId = message.getPayload();
            // Ваш код для получения данных или генерации сообщения
            return "Данные получены для пользователя с ID: " + customerId;
        };
    }*/



/*@Configuration // spring считывает бины и создает соотв. каналы
// описываются все каналы с помощью функциональных методов
// этот способ - рекомендуемый, вместо старого способа (@Binding, интерфейсы)
public class CreditCardStreamConfig {

    // для заполнения тестовых данных
    private CreditCardService creditCardService;

    public CreditCardStreamConfig(CreditCardServiceImpl creditCardService) {
        this.creditCardService = creditCardService;
    }

    // получает id пользователя и запускает создание тестовых данных
    // название метода должно совпадать с настройками definition и bindings в файлах properties (или yml)
  *//*  @Bean
    public Function<Message<Long>, List<CreditCard>> fetchCreditCardsByUserId() {
        return message -> creditCardService.getAllCreditCardsByCustomerId(message.getPayload());
    }*/
