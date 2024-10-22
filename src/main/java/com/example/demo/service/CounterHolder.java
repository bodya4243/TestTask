package com.example.demo.service;

import org.springframework.stereotype.Component;

@Component
public class CounterHolder {
    private static final ThreadLocal<Integer> currentIndex = ThreadLocal.withInitial(() -> 0);

    public int getCurrentIndex() {
        return currentIndex.get();
    }

    public void setCurrentIndex(int index) {
        currentIndex.set(index);
    }
}
