package com.example.demo.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CsvParser {
    private static final int CSV_MASK_NUM = 1;
    private static final int CSV_CHECKED_TIME = 0;
    private static final int STRING_HOURS = 0;
    private static final int STRING_MINUTES = 1;

    private final CsvReadUtil csvReadUtil;
    private final CounterStateService counterStateService;

    public int[] parseCsv() {
        List<String[]> csvStrings = csvReadUtil.readFromCSV();
        int currentIndex = counterStateService.getCurrentIndex();

        log.debug("Parsing CSV data: {}", csvStrings);
        int bitMaskNum;
        int hours;
        int minutes;

        if (currentIndex >= csvStrings.size()) {
            counterStateService.setCurrentIndex(0);
            currentIndex = 0;
        }

        String[] row = csvStrings.get(currentIndex);
        log.debug("Processing row: {}", (Object) row);

        bitMaskNum = Integer.parseInt(row[CSV_MASK_NUM]);
        hours = Integer.parseInt(row[CSV_CHECKED_TIME].split(":")[STRING_HOURS]);
        minutes = Integer.parseInt(row[CSV_CHECKED_TIME].split(":")[STRING_MINUTES]);

        counterStateService.setCurrentIndex(currentIndex +1);

        log.info("Parsed values from row - Bitmask: {}, Hours: {}, Minutes: {}", bitMaskNum, hours, minutes);
        return new int[]{bitMaskNum, hours, minutes};
    }
}
