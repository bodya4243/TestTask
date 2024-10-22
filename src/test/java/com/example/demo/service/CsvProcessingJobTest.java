package com.example.demo.service;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;


@ExtendWith(MockitoExtension.class)
public class CsvProcessingJobTest {
    private static final ZoneId LAGOS_ZONE = ZoneId.of("Africa/Lagos");

    @Mock
    private CsvParser csvParser;

    @Mock
    private BitMaskDayParser bitMaskDayParser;

    @InjectMocks
    private CsvProcessingJob csvProcessingJob;

    @Test
    public void testRun_ConditionsMet_ShouldLaunchApp() {
        when(csvParser.parseCsv()).thenReturn(new int[]{127, 10, 30});

        when(bitMaskDayParser.getDays(127)).thenReturn(List.of(DayOfWeek.TUESDAY));

        ZonedDateTime fixedNow = ZonedDateTime.of(
                LocalDate.of(2024, 10, 22),
                LocalTime.of(10, 31),
                LAGOS_ZONE);

        try (MockedStatic<ZonedDateTime> mockedZonedDateTime = mockStatic(ZonedDateTime.class)) {
            mockedZonedDateTime.when(() -> ZonedDateTime.now(LAGOS_ZONE)).thenReturn(fixedNow);

            csvProcessingJob.run();

            verify(csvParser, times(1)).parseCsv();
            verify(bitMaskDayParser, times(1)).getDays(127);
        }
    }

    @Test
    public void testRun_ConditionsNotMet_ShouldNotLaunchApp() {
        when(csvParser.parseCsv()).thenReturn(new int[]{127, 10, 30});

        when(bitMaskDayParser.getDays(127)).thenReturn(List.of(DayOfWeek.MONDAY));

        ZonedDateTime fixedNow = ZonedDateTime.of(
                LocalDate.of(2024, 10, 22),
                LocalTime.of(10, 31),
                LAGOS_ZONE);

        try (MockedStatic<ZonedDateTime> mockedZonedDateTime = mockStatic(ZonedDateTime.class)) {
            mockedZonedDateTime.when(() -> ZonedDateTime.now(LAGOS_ZONE)).thenReturn(fixedNow);

            csvProcessingJob.run();

            verify(csvParser, times(1)).parseCsv();
            verify(bitMaskDayParser, times(1)).getDays(127);
        }
    }
}
