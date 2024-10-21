package com.example.demo.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CsvService {
    public static List<String[]> readFromCSV() {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/action_times_bitmask.csv"))) {
            return reader.readAll();
        } catch (IOException | CsvException e) {
            throw new RuntimeException("cannot find the file");
        }
    }
}
