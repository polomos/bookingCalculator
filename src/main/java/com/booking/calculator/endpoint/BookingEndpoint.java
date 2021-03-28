package com.booking.calculator.endpoint;


import com.booking.calculator.RevenueCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("booking/")
@RequiredArgsConstructor
public class BookingEndpoint {

    private final RevenueCalculator revenueCalculator;

    @RequestMapping("calculate/revenue")
    public ResponseEntity<CalculationResult> calculateRevenue(@RequestParam(name = "numberOfPremiumRooms") Integer numberOfPremiumRooms,
                                                              @RequestParam(name = "numberOfStandardRooms") Integer numberOfStandardRooms) {

        return ResponseEntity.ok(revenueCalculator.calculateRevenue(numberOfPremiumRooms, numberOfStandardRooms));
    }
}
