package org.example.bot.handler;

import lombok.RequiredArgsConstructor;
import org.example.bot.Bot;
import org.example.bot.BotSender;
import org.example.salauat.SalauatService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final SalauatService salauatService;
    private final BotSender botSender;

    public void handleMessage(Long chatId, String text, Bot bot) {
        if (text.matches("\\d+")) {
            int count = Integer.parseInt(text);
            salauatService.addSalauat(chatId, count);
            botSender.send(chatId, "+ <b>" + count + "</b> салауат ✅ Машаллах!", bot);
        } else {
            botSender.send(chatId, "Салауат санын санмен жазып жіберші, өтініш 🙏", bot);
        }
    }
}
