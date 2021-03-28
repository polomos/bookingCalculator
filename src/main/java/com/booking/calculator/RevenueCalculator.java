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

@Service
@RequiredArgsConstructor
public class RevenueCalculator {
    private Logger log = LoggerFactory.getLogger(RevenueCalculator.class);

    @Value("${minimum.price.for.premium:100}")
    private Integer minimumPriceForPremium;

    private Predicate<Integer> canBePremium = (v) -> v >= minimumPriceForPremium;


    private final BookingProvider bookingProvider;

    public CalculationResult calculateRevenue(Integer availablePremiumRooms, Integer availableStandardRooms) {
        List<Integer> bookings = bookingProvider.getBookings();
        log.debug("Provided bookings: {}", bookings);

        CalculationResult result = new CalculationResult();

        bookPremiumRooms(bookings, availablePremiumRooms, result);
        bookStandardRooms(bookings, availableStandardRooms, availablePremiumRooms - result.getNumberOfPremiumRooms(), result);

        return result;
    }

    private void bookStandardRooms(List<Integer> bookings, Integer availableStandardRooms, Integer availablePremiumRoomsAfterBooking, CalculationResult result) {
        List<Integer> bookingsBelowLimit = bookings.stream()
                .filter(canBePremium.negate())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (availableStandardRooms > bookingsBelowLimit.size()) {
            bookingsBelowLimit
                    .forEach(result::addStandardRoom);
        } else {
            if (availablePremiumRoomsAfterBooking > 0) {
                int howManyPremiumShouldBeBooked = Math.min(availablePremiumRoomsAfterBooking, bookingsBelowLimit.size()-availableStandardRooms);
                bookingsBelowLimit.stream()
                        .limit(howManyPremiumShouldBeBooked)
                        .forEach(result::addPremiumRoom);
                bookingsBelowLimit.stream()
                        .skip(howManyPremiumShouldBeBooked)
                        .limit(availableStandardRooms)
                        .forEach(result::addStandardRoom);

            } else {
                bookingsBelowLimit.stream()
                        .limit(availableStandardRooms)
                        .forEach(result::addStandardRoom);
//                        .collect(Collectors.toList());
//                result.setNumberOfStandardRooms(standardRooms.size());
//                result.setStandardRoomIncome(standardRooms.stream().mapToInt(Integer::intValue).sum());
            }

        }


    }

    private void bookPremiumRooms(List<Integer> bookings, Integer availablePremiumRooms, CalculationResult result) {
        bookings.stream()
                .filter(canBePremium)
                .sorted(Comparator.reverseOrder())
                .limit(availablePremiumRooms)
                .forEach(result::addPremiumRoom);
    }

}
