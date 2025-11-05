package org.example.sheduled;

import lombok.RequiredArgsConstructor;
import org.example.bot.Bot;
import org.example.bot.BotSender;
import org.example.hadith.Hadith;
import org.example.hadith.HadithRepository;

import org.example.user.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SheduledService {

    private final UserService userService;
    private final BotSender sender;
    private final Bot bot;
    private final HadithRepository hadithRepository;

    @Scheduled(cron = "0 0 10 * * *", zone = "Asia/Almaty") //cron = "0 0 20 * * *"
    public void sendDailyHadith(){
        var users = userService.getAllUsersCached();

        int dayOfYear = LocalDate.now().getDayOfYear();
        int hadithIndex = dayOfYear % 40;

        Hadith hadith = hadithRepository.findById(hadithIndex)
                .orElse(Hadith.builder()
                        .arabic_text("إِنَّ الدِّينَ عِندَ اللّهِ الإِسْلاَمُ")
                        .kazakh_text("Шын мәнінде, Алланың алдындағы дін – Ислам.")
                        .source("(Әли Имран 3:19)").build());

        String kazakh_text = hadith.getKazakh_text();
        String arabic_text = hadith.getArabic_text();
        String source = hadith.getSource();

        String message = "\uD83C\uDF38\uD83C\uDF38\uD83C\uDF38\n" +
                "<b>" + arabic_text+"</b>\n" +
                "\n" +
                "<i>" + kazakh_text+"</i>\n" +
                "\n" +
                "\uD83C\uDF38\uD83C\uDF38\uD83C\uDF38\n" +
                "\n" +
                "— \uD83D\uDCDA <i>" + source+"</i>";

        for(var user: users){
            long userId = user.getTelegramId();

            sender.send(userId, message, bot);
        }
    }

    @Scheduled(cron = "0 0 10 * * FRI", zone = "Asia/Almaty") //cron = "0 0 10 * * FRI"
    public void sendJumaNotification(){
        var users = userService.getAllUsersCached();
        String message = "\uD83C\uDF38 \uD83C\uDF19 <b>Жұма қабыл болсын, досым!</b> \uD83C\uDF19 \uD83C\uDF38\n";

        for(var user: users){
            long userId = user.getTelegramId();

            sender.send(userId, message, bot);
        }

    }
}
