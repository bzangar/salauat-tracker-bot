package org.example.bot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bot.Bot;
import org.example.bot.BotSender;
import org.example.salauat.SalauatService;
import org.example.user.UserRankingDto;
import org.example.user.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final SalauatService salauatService;
    private final UserService userService;
    private final BotSender sender; // отдельный класс для отправки сообщений (чтобы не дублировать execute)


    public void handleCommand(Update update, Bot bot) {
        String command = update.getMessage().getText().trim();
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        //Long userId = update.getMessage().getFrom().getId();
        log.info("ПРИШЛА КОММАНДА " + command);

        userService.registerIfAbsent(username);



        if (command.startsWith("/start")){
            sender.send(chatId, "Salauat Bot-қа қош келдіңіз!\n\nБүгін айтқан салауат санын жазып жіберіңіз 🙌", bot);
        }

        else if (command.startsWith("/today")){
            int total = salauatService.getToday(username);
            sender.send(chatId, "Бүгін сіз <b>" + total + "</b> салауат айттыңыз 🌸", bot);
        }

        else if (command.startsWith("/week")){
            int total = salauatService.getWeeklyCount(chatId);
            sender.send(chatId, "7 күнде  — <b>" + total + "</b> салауат 💫", bot);
        }

        else if(command.startsWith("/top")){
            String top = salauatService.getTopAllTime();
            sender.send(chatId, top, bot);
        }

        else if(command.startsWith("/month_top")){
            String leaderboard = salauatService.getMonthlyRankingExternal(username);
            sender.send(chatId, leaderboard, bot);
        }

        else {
            sender.send(chatId, "Белгісіз команда 🤔", bot);
        }

//        switch (command) {
//
//            case "/start" -> sender.send(chatId, "Salauat Bot-қа қош келдіңіз!\n\nБүгін айтқан салауат санын жазып жіберіңіз 🙌", bot);
//            case "/today" -> {
//                int total = salauatService.getToday(username);
//                sender.send(chatId, "Бүгін сіз <b>" + total + "</b> салауат айттыңыз 🌸", bot);
//            }
//            case "/week" -> {
//                int total = salauatService.getWeeklyCount(chatId);
//                sender.send(chatId, "7 күнде  — <b>" + total + "</b> салауат 💫", bot);
//            }
//            case "/top" -> {
//                String top = salauatService.getTopAllTime();
//                sender.send(chatId, top, bot);
//            }
//            case "/month_top" -> {
//                String leaderboard = salauatService.getMonthlyRankingExternal(username);
//                sender.send(chatId, leaderboard, bot);
//            }
//            default -> sender.send(chatId, "Белгісіз команда 🤔", bot);
//        }
    }
}

