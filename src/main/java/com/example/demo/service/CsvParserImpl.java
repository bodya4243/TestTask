package com.example.demo.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CsvParserImpl implements CsvParser {
    private static final int CSV_MASK_NUM = 1;
    private static final int CSV_CHECKED_TIME = 0;
    private static final int STRING_HOURS = 0;
    private static final int STRING_MINUTES = 1;
    private static final String ROW_SEPARATOR = ":";

    private final CsvReadUtil csvReadUtil;
    private final CounterHolder counterHolder;

    public int[] parseCsv() {
        List<String[]> csvStrings = csvReadUtil.readFromCSV();
        int currentIndex = counterHolder.getCurrentIndex();

        log.debug("Parsing CSV data: {}", csvStrings);

        if (currentIndex >= csvStrings.size()) {
            counterHolder.setCurrentIndex(0);
            currentIndex = 0;
        }

        String[] row = csvStrings.get(currentIndex);
        log.debug("Processing row: {}", (Object) row);

        int bitMaskNum = Integer.parseInt(row[CSV_MASK_NUM]);
        int hours = Integer.parseInt(row[CSV_CHECKED_TIME].split(ROW_SEPARATOR)[STRING_HOURS]);
        int minutes = Integer.parseInt(row[CSV_CHECKED_TIME].split(ROW_SEPARATOR)[STRING_MINUTES]);

        counterHolder.setCurrentIndex(currentIndex +1);

        log.info("Parsed values from row - Bitmask: {}, Hours: {}, Minutes: {}", bitMaskNum, hours, minutes);
        return new int[]{bitMaskNum, hours, minutes};
    }
}
