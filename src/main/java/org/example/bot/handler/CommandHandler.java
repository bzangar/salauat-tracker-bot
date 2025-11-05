package org.example.bot.handler;

import lombok.RequiredArgsConstructor;
import org.example.bot.Bot;
import org.example.bot.BotSender;
import org.example.salauat.SalauatService;
import org.example.user.UserRankingDto;
import org.example.user.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final SalauatService salauatService;
    private final UserService userService;
    private final BotSender sender; // отдельный класс для отправки сообщений (чтобы не дублировать execute)


    public void handleCommand(Update update, Bot bot) {
        String command = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();

        userService.registerIfAbsent(chatId, username);

        switch (command) {
            case "/start" -> sender.send(chatId, "Salauat Bot-қа қош келдіңіз!\n\nБүгін айтқан салауат санын жазып жіберіңіз 🙌", bot);

            case "/today" -> {
                int total = salauatService.getToday(chatId);
                sender.send(chatId, "Бүгін сіз <b>" + total + "</b> салауат айттыңыз 🌸", bot);
            }

            case "/week" -> {
                int total = salauatService.getWeeklyCount(chatId);
                sender.send(chatId, "7 күнде  — <b>" + total + "</b> салауат 💫", bot);
            }

            case "/top" -> {
                String top = salauatService.getTopAllTime();
                sender.send(chatId, top, bot);
            }


            case "/monthTop" -> {
                String leaderboard = salauatService.getMonthlyRankingExternal(chatId);

//                StringBuilder sb = new StringBuilder("🏆 *Рейтинг за месяц*\n\n");
//                for (UserRankingDto dto : leaderboard) {
//                    sb.append(dto.rank())
//                            .append(") ")
//                            .append(dto.username())
//                            .append(" — ")
//                            .append(dto.totalCount())
//                            .append(" салауатов\n");
//                }


                sender.send(chatId, leaderboard, bot);
            }


            default -> sender.send(chatId, "Белгісіз команда 🤔", bot);
        }
    }




}

