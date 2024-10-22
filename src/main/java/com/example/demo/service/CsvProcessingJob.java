package com.example.demo.service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@EnableScheduling
@Component
@Slf4j
public class CsvProcessingJob {
    private static final ZoneId LAGOS_ZONE = ZoneId.of("Africa/Lagos");
    private static final int CSV_MASK_NUM = 0;
    private static final int CSV_HOURS = 1;
    private static final int CSV_MINUTES = 2;

    private final CsvParser csvParser;
    private final BitMaskDayParser bitMaskDayParser;

    @Scheduled(cron = "${scheduler.cron}")
    public void run() {
        log.info("Task run method started");

        int[] csvParsed = csvParser.parseCsv();

        int bitMaskNum = csvParsed[CSV_MASK_NUM];
        int hours = csvParsed[CSV_HOURS];
        int minutes = csvParsed[CSV_MINUTES];

        log.info("Parsed CSV values - Bitmask: {}, Time: {}:{}", bitMaskNum, hours, minutes);

        LocalTime scheduledTime = LocalTime.of(hours, minutes);
        ZonedDateTime now = ZonedDateTime.now(LAGOS_ZONE);

        if (shouldRunApp(now, bitMaskNum, scheduledTime)) {
            log.info("Launching app at {}", ZonedDateTime.now(LAGOS_ZONE));
        } else {
            log.warn("Conditions not met for launching the app");
        }
    }

    private boolean shouldRunApp(ZonedDateTime now, int binMaskNum, LocalTime scheduledTime) {
        boolean shouldRun = bitMaskDayParser.getDays(binMaskNum).contains(now.getDayOfWeek())
                && now.toLocalTime().isAfter(scheduledTime)
                && now.toLocalTime().isBefore(scheduledTime.plusMinutes(2));
        log.info("App run condition: {}", shouldRun);
        return shouldRun;
    }
}
