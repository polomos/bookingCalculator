package com.booking.calculator.endpoint;

import com.booking.calculator.RevenueCalculator;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationResult {
    private final static Logger log = LoggerFactory.getLogger(CalculationResult.class);

    private int numberOfPremiumRooms;
    private int premiumRoomIncome;
    private int numberOfStandardRooms;
    private int standardRoomIncome;

    public void addPremiumRoom(int roomPrice){
        log.debug("New premium booking: <{}>", roomPrice);
        this.numberOfPremiumRooms++;
        this.premiumRoomIncome += roomPrice;
    }

    public void addStandardRoom(int roomPrice){
        log.debug("New standard booking: <{}>", roomPrice);
        this.numberOfStandardRooms++;
        this.standardRoomIncome += roomPrice;
    }
}