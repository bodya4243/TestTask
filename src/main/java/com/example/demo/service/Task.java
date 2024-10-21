package com.example.demo.service;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

@EnableScheduling
@Component
public class Task {
    private static final Logger logger = LogManager.getLogger(Task.class);
    private static final ZoneId LAGOS_ZONE = ZoneId.of("Africa/Lagos");
    private static final int CSV_MASK_NUM = 0;
    private static final int CSV_HOURS = 1;
    private static final int CSV_MINUTES = 2;
    private static final int STRING_HOURS = 0;
    private static final int STRING_MINUTES = 1;
    private static final char BIT_ONE = '1';

    private static int currentIndex = 0;

    @Scheduled(cron = "${scheduler.cron}")
    public static void run() {
        logger.info("Task run method started");

        List<String[]> csvStrings = CsvService.readFromCSV();
        logger.debug("CSV data read: {}", csvStrings);

        int[] csvParsed = parseCsv(csvStrings);
        int bitMaskNum = csvParsed[CSV_MASK_NUM];
        int hours = csvParsed[CSV_HOURS];
        int minutes = csvParsed[CSV_MINUTES];

        logger.info("Parsed CSV values - Bitmask: {}, Time: {}:{}", bitMaskNum, hours, minutes);

        LocalTime scheduledTime = LocalTime.of(hours, minutes);
        ZonedDateTime now = ZonedDateTime.now(LAGOS_ZONE);

        if (shouldRunApp(now, bitMaskNum, scheduledTime)) {
            logger.info("Launching app at {}", ZonedDateTime.now(LAGOS_ZONE));
        } else {
            logger.warn("Conditions not met for launching the app");
        }
    }

    private static List<DayOfWeek> getDays(int number) {
        logger.debug("Getting days for bitmask: {}", number);
        if (number == 0 || number > 127) {
            logger.error("Invalid bitmask number: {}", number);
            throw new IllegalArgumentException("Bitmask cannot be 0 or more than 127");
        }

        DayOfWeek[] daysOfWeek = new DayOfWeek[]{
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        };

        String binNum = convertToBinary(number);
        logger.debug("Converted bitmask {} to binary: {}", number, binNum);

        List<DayOfWeek> daysOfNum = new ArrayList<>();
        for (int i = 0; i < binNum.length(); i++) {
            if (binNum.charAt(i) == BIT_ONE) {
                daysOfNum.add(daysOfWeek[i]);
            }
        }

        logger.debug("Days corresponding to bitmask: {}", daysOfNum);
        return daysOfNum;
    }

    private static String convertToBinary(int number) {
        String binary = new StringBuilder(Integer.toBinaryString(number)).reverse().toString();
        logger.debug("Converted number {} to binary {}", number, binary);
        return binary;
    }

    private static boolean shouldRunApp(ZonedDateTime now, int binMaskNum, LocalTime scheduledTime) {
        boolean shouldRun = getDays(binMaskNum).contains(now.getDayOfWeek()) && now.toLocalTime().isAfter(scheduledTime)
                && now.toLocalTime().isBefore(scheduledTime.plusMinutes(2));
        logger.info("App run condition: {}", shouldRun);
        return shouldRun;
    }

    private static int[] parseCsv(List<String[]> csvStrings) {
        logger.debug("Parsing CSV data: {}", csvStrings);
        int bitMaskNum;
        int hours;
        int minutes;

        if (currentIndex >= csvStrings.size()) {
            currentIndex = 0;
        }

        String[] row = csvStrings.get(currentIndex);
        logger.debug("Processing row: {}", (Object) row);

        bitMaskNum = Integer.parseInt(row[CSV_MASK_NUM]);
        hours = Integer.parseInt(row[CSV_HOURS].split(":")[STRING_HOURS]);
        minutes = Integer.parseInt(row[CSV_MINUTES].split(":")[STRING_MINUTES]);

        currentIndex++;

        logger.info("Parsed values from row - Bitmask: {}, Hours: {}, Minutes: {}", bitMaskNum, hours, minutes);
        return new int[]{bitMaskNum, hours, minutes};
    }
}
