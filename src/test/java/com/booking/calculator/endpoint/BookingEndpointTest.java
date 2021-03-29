package com.booking.calculator.endpoint;

import com.booking.calculator.BookingCalculatorApplication;
import com.booking.calculator.WebConfig;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
@WebAppConfiguration
@ContextConfiguration(classes = BookingCalculatorApplication.class)
@WebMvcTest
public class BookingEndpointTest {
    private Logger log = LoggerFactory.getLogger(BookingEndpointTest.class);
    @Parameter
    public int numberOfPremiumRooms;
    @Parameter(value = 1)
    public int numberOfStandardRooms;
    @Parameter(value = 2)
    public int bookedPremiumRooms;
    @Parameter(value = 3)
    public int premiumRoomIncome;
    @Parameter(value = 4)
    public int bookedStandardRooms;
    @Parameter(value = 5)
    public int standardRoomIncome;

    @Autowired
    private BookingEndpoint bookingEndpoint;

    private TestContextManager testContextManager;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> params = new ArrayList<>();
        params.add(new Object[]{0, 0, 0, 0, 0, 0});//no bookings
        params.add(new Object[]{3, 0, 3, 738, 0, 0});//no standard rooms
        params.add(new Object[]{0, 3, 0, 0, 3, 167});//no premium rooms
        params.add(new Object[]{0, 3, 0, 0, 3, 167});//rooms are booked only for standard
        params.add(new Object[]{10, 0, 10, 1243, 0, 0});//there are only premium rooms, so all will be booked as premium
        params.add(new Object[]{0, 10, 0, 0, 4, 189});//there are only standard rooms, so all below limit will be booked
        params.add(new Object[]{3, 3, 3, 738, 3, 167});//not all rooms are booked
        params.add(new Object[]{7, 5, 6, 1054, 4, 189});//all premium rooms are booked
        params.add(new Object[]{2, 7, 2, 583, 4, 189});//all standard rooms are booked
        params.add(new Object[]{7, 1, 7, 1153, 1, 45});//bookings below limit are booked as premium
        params.add(new Object[]{99, 99, 6, 1054, 4, 189});//test huge number of available rooms
        return params;
    }

    @Before
    public void setup() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    @Test
    public void testCalculateRevenue() {
        log.debug("Input parameters: numberOfPremiumRooms <{}>, numberOfStandardRooms<{}>", numberOfPremiumRooms, numberOfStandardRooms);
        CalculationResult expectedRevenue = getExpectedRevenue(bookedPremiumRooms, premiumRoomIncome, bookedStandardRooms, standardRoomIncome);
        log.debug("Expected result: bookedPremiumRooms <{}>, premiumRoomIncome <{}>, bookedStandardRooms <{}>, standardRoomIncome <{}>", bookedPremiumRooms, premiumRoomIncome, bookedStandardRooms, standardRoomIncome);
        CalculationResult revenue = getRevenue(numberOfPremiumRooms, numberOfStandardRooms);
        Assertions.assertThat(revenue).isEqualTo(expectedRevenue);
    }

    private CalculationResult getExpectedRevenue(int numberOfPremiumRooms, int premiumRoomIncome, int numberOfStandardRooms, int standardRoomIncome) {
        return CalculationResult.builder()
                .numberOfPremiumRooms(numberOfPremiumRooms)
                .premiumRoomIncome(premiumRoomIncome)
                .numberOfStandardRooms(numberOfStandardRooms)
                .standardRoomIncome(standardRoomIncome)
                .build();
    }

    private CalculationResult getRevenue(int numberOfPremiumRooms, int numberOfStandardRooms) {
        return bookingEndpoint.calculateRevenue(numberOfPremiumRooms, numberOfStandardRooms).getBody();
    }
}
