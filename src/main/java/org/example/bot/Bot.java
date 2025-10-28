package org.example.bot;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.bot.handler.CommandHandler;
import org.example.bot.handler.MessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.List;


@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot{

    //@Value("${telegram.name}")
    private String botUsername = "salauat-bot";

    //@Value("${telegram.token}")
    private String botToken = "8298807960:AAFEW-JDsAIIriG16OmqyLIkD-9a-BdJvPE";

    private final CommandHandler commandHandler;
    private final MessageHandler messageHandler;



    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        if (text.startsWith("/")) {
            // если это команда (/start, /today...)
            commandHandler.handleCommand(update, this);
        } else {
            // если это обычное сообщение (например, “100”)
            messageHandler.handleMessage(chatId, text, this);
        }
    }

    @PostConstruct
    public void initCommands() {
        try {
            List<BotCommand> commands = List.of(
                    new BotCommand("/start", "Ботты қосу"),
                    new BotCommand("/today", "Бүгінгі салауаттарым"),
                    new BotCommand("/week", "Апталық салауаттарым"),
                    new BotCommand("/rating", "Рейтинг")
            );

            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
            System.out.println("✅ Команды успешно добавлены");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}

