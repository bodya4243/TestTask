package com.example.demo.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CsvReadUtil {
    @Value("${csv.file.path}")
    private String csvFilePath;

    public List<String[]> readFromCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            return reader.readAll();
        } catch (IOException | CsvException e) {
            throw new IllegalArgumentException("Cannot find the file: " + csvFilePath, e);
        }
    }
}
