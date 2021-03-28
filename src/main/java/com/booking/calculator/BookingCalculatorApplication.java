package com.booking.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class BookingCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingCalculatorApplication.class, args);
    }
}
