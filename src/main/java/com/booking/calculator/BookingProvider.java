package com.booking.calculator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingProvider {

    public List<Integer> getBookings() {
        return List.of(23, 45, 155, 374, 22, 99, 100, 101, 115, 209);
    }
}
