package de.fnortheim.service;

import de.fnortheim.domain.RfbEvent;
import de.fnortheim.domain.RfbLocation;
import de.fnortheim.repository.RfbEventRepository;
import de.fnortheim.repository.RfbLocationRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


/**
 * Run daily and create random event codes for Rfb
 *
 * @author sebastianbasner
 */
@Service
public class RfbEventCodeService {
    private final Logger log = LoggerFactory.getLogger(RfbEventCodeService.class);

    private final RfbLocationRepository rfbLocationRepository;
    private final RfbEventRepository rfbEventRepository;

    public RfbEventCodeService(RfbLocationRepository rfbLocationRepository, RfbEventRepository rfbEventRepository) {
        this.rfbLocationRepository = rfbLocationRepository;
        this.rfbEventRepository = rfbEventRepository;
    }


    @Scheduled(cron = "0 0 * * * ?") // once per hour at top of hour
//    @Scheduled(cron = "0 * * * * ?") // once per min
//    @Scheduled(cron = "* * * * * ?") // once per second
    public void generateRunEventCodes() {
        log.debug("Generating Events");

        List<RfbLocation> rfbLocations = rfbLocationRepository.findAllByRunDayOfWeek(LocalDate.now().getDayOfWeek().getValue());

        log.debug("Locations found for Events: " + rfbLocations.size());

        rfbLocations.forEach(rfbLocation -> {
                log.debug("Checking Events for location: " + rfbLocation.getId());
                RfbEvent existingEvent = rfbEventRepository.findByRfbLocationAndEventDate(rfbLocation, LocalDate.now());

                if (existingEvent == null) {
                    log.debug("Event not Found, creating Event");

                    //create Event for day
                    RfbEvent newEvent = new RfbEvent();
                    newEvent.setRfbLocation(rfbLocation);
                    newEvent.setEventDate(LocalDate.now());
                    newEvent.setEventCode(RandomStringUtils.randomAlphanumeric(10).toUpperCase());

                    rfbEventRepository.save(newEvent);

                    log.debug("created Event: " + newEvent.toString());
                } else {
                    log.debug("Event exists for day");
                }
            }
        );
    }
}
