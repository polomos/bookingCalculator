package com.booking.calculator;

import com.booking.calculator.endpoint.CalculationResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RevenueCalculator {

    @Value("${minimum.price.for.premium:100}")
    private Integer minimumPriceForPremium;

    private Predicate<Integer> canBePremium = (v) -> v >= minimumPriceForPremium;

    private Logger log = LoggerFactory.getLogger("SampleLogger");

    private final BookingProvider bookingProvider;

    public CalculationResult calculateRevenue(Integer numberOfPremiumRooms, Integer numberOfStandardRooms) {
        List<Integer> bookings = bookingProvider.getBookings();
        log.debug("Provided bookings: {}", bookings);

        CalculationResult result = new CalculationResult();

        bookPremiumRooms(bookings, numberOfPremiumRooms, result);
        bookStandardRooms(bookings, numberOfStandardRooms, numberOfPremiumRooms - result.getNumberOfPremiumRooms(), result);

        return result;
    }

    private void bookStandardRooms(List<Integer> bookings, Integer numberOfStandardRooms, Integer emptyPremiumRooms, CalculationResult result) {
        List<Integer> roomsBelowLimit = bookings.stream()
                .filter(canBePremium.negate())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (numberOfStandardRooms > roomsBelowLimit.size() && emptyPremiumRooms > 0) {
            List<Integer> standardRooms = bookings.stream()
                    .sorted(Integer::compareTo)
                    .limit(Math.min(numberOfStandardRooms, bookings.size() - result.getNumberOfPremiumRooms()))
                    .collect(Collectors.toList());
            result.setNumberOfStandardRooms(standardRooms.size());
            result.setStandardRoomIncome(standardRooms.stream().mapToInt(Integer::intValue).sum());

        } else {
            result.setNumberOfStandardRooms(numberOfStandardRooms);
            result.setStandardRoomIncome(roomsBelowLimit.stream().limit(numberOfStandardRooms).mapToInt(Integer::intValue).sum());
        }


    }

    private void bookPremiumRooms(List<Integer> bookings, Integer numberOfPremiumRooms, CalculationResult result) {
        List<Integer> premiumRooms = bookings.stream()
                .filter(canBePremium)
                .sorted(Comparator.reverseOrder())
                .limit(numberOfPremiumRooms)
                .collect(Collectors.toList());

        result.setNumberOfPremiumRooms(premiumRooms.size());
        result.setPremiumRoomIncome(premiumRooms.stream().mapToInt(Integer::intValue).sum());
    }

}
