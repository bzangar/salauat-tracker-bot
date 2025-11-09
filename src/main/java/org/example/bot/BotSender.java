package org.example.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class BotSender {

    @Async
    public Message send(Long chatId, String text, Bot bot) {
        try {
            Message m = bot.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode("HTML")
                    .build());

            return m;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
