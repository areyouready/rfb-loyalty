package de.fnortheim.bootstrap;

import de.fnortheim.domain.RfbEvent;
import de.fnortheim.domain.RfbEventAttendance;
import de.fnortheim.domain.RfbLocation;
import de.fnortheim.domain.User;
import de.fnortheim.repository.RfbEventAttendanceRepository;
import de.fnortheim.repository.RfbEventRepository;
import de.fnortheim.repository.RfbLocationRepository;
import de.fnortheim.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class RfbBootstrap implements CommandLineRunner {

    private final RfbLocationRepository rfbLocationRepository;
    private final RfbEventRepository rfbEventRepository;
    private final RfbEventAttendanceRepository rfbEventAttendanceRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final AuthorityRepository authorityRepository;

    //    public RfbBootstrap(RfbLocationRepository rfbLocationRepository, RfbEventRepository rfbEventRepository,
//                        RfbEventAttendanceRepository rfbEventAttendanceRepository, RfbUserRepository rfbUserRepository,
//                        PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
    public RfbBootstrap(RfbLocationRepository rfbLocationRepository, RfbEventRepository rfbEventRepository,
                        RfbEventAttendanceRepository rfbEventAttendanceRepository, UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.rfbLocationRepository = rfbLocationRepository;
        this.rfbEventRepository = rfbEventRepository;
        this.rfbEventAttendanceRepository = rfbEventAttendanceRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
//        this.authorityRepository = authorityRepository;
    }

    @Transactional
    @Override
    public void run(String... strings) throws Exception {

        // init RFB Locations
        if (rfbLocationRepository.count() == 0) {
            //only load data if no data loaded
            initData();
        }

    }

    private void initData() {
        User user = new User();
        user.setFirstName("Sebastian");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setLogin("Sebastian");
        user.setEmail("sebastian@runningforbrews.com");
        user.setActivated(true);
        userRepository.save(user);

        //load data
        RfbLocation aleAndWitch = getRfbLocation("St Pete - Ale and the Witch", DayOfWeek.MONDAY.getValue());

        user.setHomeLocation(aleAndWitch);
        userRepository.save(user);

        RfbEvent aleEvent = getRfbEvent(aleAndWitch);

        getRfbEventAttendance(user, aleEvent);

        RfbLocation ratc = getRfbLocation("St Pete - Right Around The Corner", DayOfWeek.TUESDAY.getValue());

        RfbEvent ratcEvent = getRfbEvent(ratc);

        getRfbEventAttendance(user, ratcEvent);

        RfbLocation stPeteBrew = getRfbLocation("St Pete - St Pete Brewing", DayOfWeek.WEDNESDAY.getValue());

        RfbEvent stPeteBrewEvent = getRfbEvent(stPeteBrew);

        getRfbEventAttendance(user, stPeteBrewEvent);

        RfbLocation yardOfAle = getRfbLocation("St Pete - Yard of Ale", DayOfWeek.THURSDAY.getValue());

        RfbEvent yardOfAleEvent = getRfbEvent(yardOfAle);

        getRfbEventAttendance(user, yardOfAleEvent);

        RfbLocation pourHouse = getRfbLocation("Tampa - Pour House", DayOfWeek.MONDAY.getValue());
        RfbLocation macDintons = getRfbLocation("Tampa - Mac Dintons", DayOfWeek.TUESDAY.getValue());

        RfbLocation satRun = getRfbLocation("Saturday Run for testing", DayOfWeek.SATURDAY.getValue());
    }


    private void getRfbEventAttendance(User user, RfbEvent rfbEvent) {
        RfbEventAttendance rfbAttendance = new RfbEventAttendance();
        rfbAttendance.setRfbEvent(rfbEvent);
        rfbAttendance.setUser(user);
        rfbAttendance.setAttendanceDate(LocalDate.now());

        System.out.println(rfbAttendance.toString());

        rfbEventAttendanceRepository.save(rfbAttendance);
        rfbEventRepository.save(rfbEvent);
    }

    private RfbEvent getRfbEvent(RfbLocation rfbLocation) {
        RfbEvent rfbEvent = new RfbEvent();
        rfbEvent.setEventCode(UUID.randomUUID().toString());
        rfbEvent.setEventDate(LocalDate.now()); // will not be on assigned day...
        rfbLocation.addRfbEvent(rfbEvent);
        rfbLocationRepository.save(rfbLocation);
        rfbEventRepository.save(rfbEvent);
        return rfbEvent;
    }

    private RfbLocation getRfbLocation(String locationName, int value) {
        RfbLocation rfbLocation = new RfbLocation();
        rfbLocation.setLocationName(locationName);
        rfbLocation.setRunDayOfWeek(value);
        rfbLocationRepository.save(rfbLocation);
        return rfbLocation;
    }
}
