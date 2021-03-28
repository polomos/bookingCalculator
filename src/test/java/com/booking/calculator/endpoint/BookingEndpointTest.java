package com.booking.calculator.endpoint;

import com.booking.calculator.BookingCalculatorApplication;
import com.booking.calculator.WebConfig;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
public class BookingEndpointTest {

    @Parameter
    public Integer numberOfPremiumRooms;
    @Parameter(value = 1)
    public Integer numberOfStandardRooms;
    @Parameter(value = 2)
    public Integer bookedPremiumRooms;
    @Parameter(value = 3)
    public Integer premiumRoomIncome;
    @Parameter(value = 4)
    public Integer bookedStandardRooms;
    @Parameter(value = 5)
    public Integer standardRoomIncome;

    @Autowired
    private BookingEndpoint bookingEndpoint;

    private TestContextManager testContextManager;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> params = new ArrayList<>();
        params.add(new Object[]{3, 3, 3, 738, 3, 167});
        params.add(new Object[]{7, 5, 6, 1054, 4, 189});
        params.add(new Object[]{2, 7, 2, 583, 4, 189});
        params.add(new Object[]{7, 1, 7, 1153, 1, 45});
        return params;
    }

    @Before
    public void setup() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
    }

    @Test
    public void testCalculateRevenue() {

        CalculationResult expectedRevenue = getExpectedRevenue(bookedPremiumRooms, premiumRoomIncome, bookedStandardRooms, standardRoomIncome);

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
