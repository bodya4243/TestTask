package com.example.demo.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class CounterStateService {
    private int currentIndex = 0;
}
