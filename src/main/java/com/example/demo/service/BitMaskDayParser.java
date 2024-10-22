package com.example.demo.service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BitMaskDayParser {
    private static final int MIN_BITMASK_VALUE = 1;
    private static final int MAX_BITMASK_VALUE = 127;
    private static final char BIT_ONE = '1';

    private final DayOfWeek[] daysOfWeek = new DayOfWeek[]{
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
    };

    public List<DayOfWeek> getDays(int number) {
        log.debug("Getting days for bitmask: {}", number);
        if (number < MIN_BITMASK_VALUE || number > MAX_BITMASK_VALUE) {
            log.error("Invalid bitmask number: {}", number);
            throw new IllegalArgumentException("Bitmask cannot be 0 or more than 127");
        }

        String binNum = convertToBinary(number);
        log.debug("Converted bitmask {} to binary: {}", number, binNum);

        List<DayOfWeek> daysOfNum = new ArrayList<>();
        for (int i = 0; i < binNum.length(); i++) {
            if (binNum.charAt(i) == BIT_ONE) {
                daysOfNum.add(daysOfWeek[i]);
            }
        }

        log.debug("Days corresponding to bitmask: {}", daysOfNum);
        return daysOfNum;
    }

    private String convertToBinary(int number) {
        String binary = new StringBuilder(Integer.toBinaryString(number)).reverse().toString();
        log.debug("Converted number {} to binary {}", number, binary);
        return binary;
    }
}
