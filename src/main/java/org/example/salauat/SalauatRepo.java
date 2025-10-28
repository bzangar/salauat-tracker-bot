package org.example.salauat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalauatRepo extends JpaRepository<Salauat, Long> {
    List<Salauat> findByTelegramIdAndDate(Long telegramId, LocalDate Date);

    @Query("""
    SELECT u.username, SUM(s.count)
    FROM Salauat s
    JOIN User u ON s.telegramId = u.telegramId
    WHERE s.date BETWEEN :start AND :end
    GROUP BY u.username
    ORDER BY SUM(s.count) DESC
    LIMIT 3
""")
    List<Object[]> findTop3ThisMonth(LocalDate start, LocalDate end);

    @Query("""
        SELECT SUM(s.count)
        FROM Salauat s
        WHERE s.telegramId = :telegramId
          AND s.date BETWEEN :start AND :end
    """)
    Optional<Integer> sumCountByTelegramIdAndDateBetween(Long telegramId, LocalDate start, LocalDate end);

    @Query("""
        SELECT u.username AS username, SUM(s.count) AS totalCount
        FROM Salauat s
        JOIN User u ON s.telegramId = u.telegramId
        WHERE s.date BETWEEN :start AND :end
        GROUP BY u.username
        ORDER BY totalCount DESC
    """)
    List<Object[]> findMonthlyLeaderboard(LocalDate start, LocalDate end);

}
