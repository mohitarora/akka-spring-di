package com.sample;

import org.springframework.stereotype.Component;

/**
 * A simple service that can increment a number.
 */
@Component
public class CountingService {

    /**
     * Increment the given number by one.
     */
    public int increment(int count) {
        return count + 1;
    }

}
