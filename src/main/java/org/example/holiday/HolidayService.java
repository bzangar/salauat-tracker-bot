package org.example.holiday;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class HolidayService {

    public String getDaysLeftToHolidays() {
        HijrahDate hijrahDate = HijrahDate.now();
        int currentHijraYear = hijrahDate.get(ChronoField.YEAR);

        HijrahDate ramadan = HijrahDate.of(currentHijraYear, 9, 1);
        HijrahDate maulit = HijrahDate.of(currentHijraYear, 3, 12);
        List<Holiday> holidays = List.of(new Holiday("\uD83C\uDF19","Рамазанға", ramadan), new Holiday("\uD83D\uDD4A\uFE0F", "Мәулітке", maulit));

        StringBuilder sb = new StringBuilder();

        for(Holiday holiday: holidays){
            if(holiday.getHijrahDate().isBefore(hijrahDate)){
                holiday.setHijrahDate(holiday.getHijrahDate().plus(1, ChronoUnit.YEARS));
            }

            LocalDate today = LocalDate.now();
            LocalDate holidayByGregorean = LocalDate.from(holiday.getHijrahDate());

            long daysLeft = ChronoUnit.DAYS.between(today, holidayByGregorean);

            sb.append(holiday.getEmoji() + " " + holiday.getName() + " <b>" + daysLeft + "</b> күн қалды\n\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        HolidayService holidayService = new HolidayService();
        System.out.println(holidayService.getDaysLeftToHolidays());
    }
}
