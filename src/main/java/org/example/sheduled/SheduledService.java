package org.example.sheduled;

import lombok.RequiredArgsConstructor;
import org.example.bot.Bot;
import org.example.bot.BotSender;
import org.example.hadith.Hadith;
import org.example.hadith.HadithRepository;
import org.example.salauat.SalauatService;
import org.example.user.User;
import org.example.user.UserRepository;
import org.example.user.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SheduledService {

    private final UserService userService;
    private final BotSender sender;
    private final Bot bot;
    private final HadithRepository hadithRepository;

    @Scheduled(cron = "0 0 19 * * *") //cron = "0 0 19 * * *"
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

        String message = "— — — — — — — — — —\n" +
                "\n" +
                "\uD83C\uDF38 \uD83C\uDF38\uD83C\uDF38\n" +
                 arabic_text +
                "\n\n"
                + kazakh_text + "\n" +
                "\n" +
                "\uD83C\uDF38\uD83C\uDF38\uD83C\uDF38\n" +
                "\n" +
                "— \uD83D\uDCDA "+ source +" \n" +
                "\n" +
                "— — — — — — — — — —";

        for(var user: users){
            long userId = user.getTelegramId();

            sender.send(userId, message, bot);
        }

    }
}
