package com.example.account_service.mq;


import com.example.account_service.business.service.impl.AccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration // spring считывает бины и создает соотв. каналы
// описываются все каналы с помощью функциональных методов
// этот способ - рекомендуемый, вместо старого способа (@Binding, интерфейсы)
public class MessageFunc {

    // для заполнения тестовых данных
    private AccountServiceImpl accountService;

    public MessageFunc(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    // получает id пользователя и запускает создание тестовых данных
    // название метода должно совпадать с настройками definition и bindings в файлах properties (или yml)
    @Bean
    public Consumer<Message<Long>> newUserActionConsume() {
        return message -> accountService.initAccount(message.getPayload());
    }
}

