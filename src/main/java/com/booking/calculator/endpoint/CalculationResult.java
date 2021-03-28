package com.booking.calculator.endpoint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationResult {
    private int numberOfPremiumRooms;
    private int premiumRoomIncome;
    private int numberOfStandardRooms;
    private int standardRoomIncome;

    public void addPremiumRoom(int roomPrice){
        this.numberOfPremiumRooms++;
        this.premiumRoomIncome += roomPrice;
    }

    public void addStandardRoom(int roomPrice){
        this.numberOfStandardRooms++;
        this.standardRoomIncome += roomPrice;
    }

}
