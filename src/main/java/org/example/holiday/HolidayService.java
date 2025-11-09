package org.example.holiday;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;


@Service
public class HolidayService {

    public String getDaysLeftToHolidays() {
        HijrahDate hijrahDate = HijrahDate.now();
        int currentHijraYear = hijrahDate.get(ChronoField.YEAR);
        HijrahDate ramadan = HijrahDate.of(currentHijraYear, 9, 1);

        if(ramadan.isBefore(hijrahDate)){
            ramadan = ramadan.plus(1, ChronoUnit.YEARS);
        }

        LocalDate ramadanByGregorean = LocalDate.from(ramadan);
        LocalDate today = LocalDate.now();

        long daysLeft = ChronoUnit.DAYS.between(today, ramadanByGregorean);
        String emoji;

        if (daysLeft <= 30) {
            emoji = "\uD83E\uDD29\uD83E\uDD29"; // 🤩
        } else {
            emoji = "\uD83D\uDE0A\uD83D\uDE0A"; // 😊
        }

        return "\uD83C\uDF19 Рамазанға <b>" + daysLeft + "</b> күн қалды " + emoji;
    }
}
