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
    private static Logger log = LoggerFactory.getLogger(RevenueCalculator.class);

    @Value("${minimum.price.for.premium:100}")
    private int minimumPriceForPremium;

    private Predicate<Integer> canBePremium = (v) -> v >= minimumPriceForPremium;


    private final BookingProvider bookingProvider;

    public CalculationResult calculateRevenue(int availablePremiumRooms, int availableStandardRooms) {
        List<Integer> bookings = bookingProvider.getBookings();
        log.debug("Provided bookings: {}", bookings);

        CalculationResult result = new CalculationResult();

        bookPremiumRooms(bookings, availablePremiumRooms, result);
        bookStandardRooms(bookings, availableStandardRooms, availablePremiumRooms - result.getNumberOfPremiumRooms(), result);

        return result;
    }

    /**
     * Book available standard rooms with bookings below {@link #minimumPriceForPremium}
     * In case all standard rooms are booked and there are bookings below {@link #minimumPriceForPremium} and there are empty premium rooms (when all premium bookings are booked),
     * then bookings with highest value have preference to be booked in premium rooms
     */
    private void bookStandardRooms(List<Integer> bookings, int availableStandardRooms, int availablePremiumRoomsAfterBooking, CalculationResult result) {
        List<Integer> bookingsBelowLimit = bookings.stream()
                .filter(canBePremium.negate())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        //there is more standard rooms than bookings below limit, so all will be booked as standard
        if (availableStandardRooms > bookingsBelowLimit.size()) {
            bookingsBelowLimit.forEach(result::addStandardRoom);
        } else {
            // in case there are more bookings than rooms, try to book some in premium rooms
            if (availablePremiumRoomsAfterBooking > 0) {
                int howManyPremiumShouldBeBooked = Math.min(availablePremiumRoomsAfterBooking, bookingsBelowLimit.size() - availableStandardRooms);
                // book premium rooms first
                bookingsBelowLimit.stream()
                        .limit(howManyPremiumShouldBeBooked)
                        .forEach(result::addPremiumRoom);
                // book standard rooms
                bookingsBelowLimit.stream()
                        .skip(howManyPremiumShouldBeBooked)
                        .limit(availableStandardRooms)
                        .forEach(result::addStandardRoom);

                // in case there are only standard rooms available, book as many as possible
            } else {
                bookingsBelowLimit.stream()
                        .limit(availableStandardRooms)
                        .forEach(result::addStandardRoom);
            }

        }
    }

    /**
     * Book available premium rooms with bookings where price is above or equals {@link #minimumPriceForPremium}
     * Bookings with highest values have precedence
     */
    private void bookPremiumRooms(List<Integer> bookings, int availablePremiumRooms, CalculationResult result) {
        bookings.stream()
                .filter(canBePremium)
                .sorted(Comparator.reverseOrder())
                .limit(availablePremiumRooms)
                .forEach(result::addPremiumRoom);
    }
}
