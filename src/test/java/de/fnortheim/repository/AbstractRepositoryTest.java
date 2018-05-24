package de.fnortheim.repository;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author sebastianbasner
 */
public class AbstractRepositoryTest {
    @Autowired
    RfbLocationRepository rfbLocationRepository;

    @Autowired
    RfbEventRepository rfbEventRepository;

    @Autowired
    RfbEventAttendanceRepository rfbEventAttendanceRepository;

    @Autowired
    RfbUserRepository rfbUserRepository;

}
