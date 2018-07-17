package de.fnortheim.repository;

import de.fnortheim.RfbloyaltyApp;
import de.fnortheim.bootstrap.RfbBootstrap;
import de.fnortheim.domain.RfbLocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author sebastianbasner
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RfbloyaltyApp.class})
public class RfbLocationRepositoryTest extends AbstractRepositoryTest {

    @Before
    public void setUp() throws Exception {
        RfbBootstrap bootstrap = new RfbBootstrap(rfbLocationRepository, rfbEventRepository, rfbEventAttendanceRepository, userRepository, passwordEncoder);
    }

    @Test
    public void findAllByRunDayOfWeek() {
        final List<RfbLocation> mondayLocations = rfbLocationRepository.findAllByRunDayOfWeek(DayOfWeek.MONDAY.getValue());
        final List<RfbLocation> tuesdayLocations = rfbLocationRepository.findAllByRunDayOfWeek(DayOfWeek.TUESDAY.getValue());
        final List<RfbLocation> wednesdayLocations = rfbLocationRepository.findAllByRunDayOfWeek(DayOfWeek.WEDNESDAY.getValue());

        assertEquals(2, mondayLocations.size());
        assertEquals(2, tuesdayLocations.size());
        assertEquals(1, wednesdayLocations.size());

    }
}
