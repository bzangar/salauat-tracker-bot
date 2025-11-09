package org.example.bot.handler;

import lombok.RequiredArgsConstructor;
import org.example.bot.Bot;
import org.example.bot.BotSender;
import org.example.salauat.SalauatService;
import org.example.user.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final SalauatService salauatService;
    private final BotSender botSender;
    private final UserService userService;

    public void handleMessage(Update update, Bot bot) {
        Integer messageId = update.getMessage().getMessageId();
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        //Long userId = update.getMessage().getFrom().getId();


        String username = update.getMessage().getFrom().getUserName();
        userService.registerIfAbsent(username, chatId);

        if (text.matches("\\d+")) {
            int count = Integer.parseInt(text);
            salauatService.addSalauat(username, count);
            Message sentMessage = botSender.send(chatId, "+ <b>" + count + "</b> салауат ✅ Машаллах!\n\n(Бүгінге " + salauatService.getToday(username)+ " салауат)", bot);
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setMessageId(messageId);
            deleteMessage.setChatId(chatId);

            DeleteMessage botMessage = new DeleteMessage();
            botMessage.setMessageId(sentMessage.getMessageId());
            botMessage.setChatId(sentMessage.getChatId());
            try{
                Thread.sleep(10000);
                bot.execute(deleteMessage);
                bot.execute(botMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
