package org.example.salauat;

import lombok.RequiredArgsConstructor;
import org.example.user.User;
import org.example.user.UserRankingDto;
import org.example.user.UserRepository;
import org.example.user.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalauatService {
    private final SalauatRepo salauatRepository;
    private final UserService userService;

    @CacheEvict(value = "todayStats", key = "#telegramId")
    public void addSalauat(Long telegramId, int count) {
        Salauat s = new Salauat();
        s.setTelegramId(telegramId);
        s.setCount(count);
        s.setDate(LocalDate.now());
        salauatRepository.save(s);
    }

    @Cacheable(value = "todayStats", key = "#telegramId")
    public int getToday(Long telegramId) {
        return salauatRepository
                .findByTelegramIdAndDate(telegramId, LocalDate.now())
                .stream()
                .mapToInt(Salauat::getCount)
                .sum();
    }

    public List<String> getTop3ThisMonth() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();

        List<Object[]> results = salauatRepository.findTop3ThisMonth(start, end);
        return results.stream()
                .map(r -> "@" + r[0] + " — " + r[1] + " салауат")
                .toList();
    }

    public int getWeeklyCount(Long telegramId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6); // последние 7 дней (включая сегодня)

        return salauatRepository.sumCountByTelegramIdAndDateBetween(telegramId, start, end)
                .orElse(0);
    }

    public String getMonthlyRankingExternal(Long currentUserId) {
        String currentUsername = userService.getUsernameById(currentUserId); // вне @Cacheable
        return getMonthlyRanking(currentUserId, currentUsername);
    }

    @Cacheable(value = "monthlyLeaderboard", key = "#currentUserId")
    public String getMonthlyRanking(Long currentUserId, String currentUsername) {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();

        List<Object[]> rawResults = salauatRepository.findMonthlyLeaderboard(start, end);


        // Преобразуем в DTO с рангом
        List<UserRankingDto> allRankings = new ArrayList<>();
        int rank = 1;
        for (Object[] row : rawResults) {
            String username = (String) row[0];
            Long total = ((Number) row[1]).longValue();
            allRankings.add(new UserRankingDto(username, total, rank++));
        }

        // Найдём позицию текущего пользователя
        Integer userRank = null;
        for (UserRankingDto dto : allRankings) {
            if (dto.username().equalsIgnoreCase(currentUsername)) {
                userRank = dto.rank();
                break;
            }
        }

        // Если пользователь не найден (например, не делал салауат)
        if (userRank == null) {
            allRankings = allRankings.stream().limit(3).toList(); // просто топ-3
            return format(allRankings, currentUsername);
        }

        // Если в топ-3 → показываем топ-3
        if (userRank <= 3) {
            allRankings = allRankings.stream().limit(3).toList();
            return format(allRankings, currentUsername);
        }

        // Иначе: топ-3 + сам пользователь
        List<UserRankingDto> result = new ArrayList<>(allRankings.subList(0, 3));
        result.add(allRankings.get(userRank - 1)); // добавляем себя

        String message = format(result, currentUsername);
        return message;
    }



    public String format(List<UserRankingDto> rankings, String currentUsername) {
        int numberOfMonth = LocalDate.now().getMonth().getValue();
        int year = LocalDate.now().getYear();
        String [] months = {"", "Қаңтар", "Ақпан",
                "Наурыз","Сәуір","Мамыр",
                "Маусым","Шілде","Тамыз",
                "Қыркүйек","Қазан","Қараша",
                "Желтоқсан"};


        StringBuilder sb = new StringBuilder("🏆 <b>Рейтинг (" + months[numberOfMonth] + " " + year +")</b>\n\n");

        for (UserRankingDto dto : rankings) {
            String medal = switch (dto.rank()) {
                case 1 -> "🥇";
                case 2 -> "🥈";
                case 3 -> "🥉";
                default -> dto.rank() + ")";
            };

            String line = medal + " <a href=\"https://t.me/" + dto.username() + "\">@" + dto.username() + "</a> — "
                    + dto.totalCount() + " салауатов\n";

            // выделяем текущего пользователя жирным
            if (dto.username().equalsIgnoreCase(currentUsername)) {
                line = medal + " <b><a href=\"https://t.me/" + dto.username() + "\">@" + dto.username() + "</a></b> — <b>"
                        + dto.totalCount() + "</b> салауатов\n";
            }

            sb.append(line);
        }

        return sb.toString();
    }
}

