package de.fnortheim.repository;

import de.fnortheim.RfbloyaltyApp;
import de.fnortheim.bootstrap.RfbBootstrap;
import de.fnortheim.domain.RfbEvent;
import de.fnortheim.domain.RfbLocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;

/**
 * @author sebastianbasner
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RfbloyaltyApp.class})
public class RfbEventRepositoryTest extends AbstractRepositoryTest {

    @Before
    public void setUp() throws Exception {
        RfbBootstrap rfbBootstrap = new RfbBootstrap(rfbLocationRepository, rfbEventRepository, rfbEventAttendanceRepository, userRepository, passwordEncoder);
    }

    @Test
    public void findAllByRfbLocationAndEventDate() {
        RfbLocation aleAndTehWitch = rfbLocationRepository.findByLocationName("St Pete - Ale and the Witch");
        assertNotNull(aleAndTehWitch);

        RfbEvent rfbEvent = rfbEventRepository.findByRfbLocationAndEventDate(aleAndTehWitch, LocalDate.now());
        assertNotNull(rfbEvent);

    }


}
