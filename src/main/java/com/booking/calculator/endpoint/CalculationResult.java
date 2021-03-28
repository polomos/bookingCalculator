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

}
