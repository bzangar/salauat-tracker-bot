package org.example.holiday;

import lombok.Builder;
import lombok.Data;

import java.time.chrono.HijrahDate;

@Builder
@Data
public class Holiday {

    private String emoji;
    private String name;
    private HijrahDate hijrahDate;

}
