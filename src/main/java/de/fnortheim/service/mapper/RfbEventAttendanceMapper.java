package de.fnortheim.service.mapper;

import de.fnortheim.domain.*;
import de.fnortheim.service.dto.RfbEventAttendanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RfbEventAttendance and its DTO RfbEventAttendanceDTO.
 */
@Mapper(componentModel = "spring", uses = {RfbEventMapper.class, RfbUserMapper.class})
public interface RfbEventAttendanceMapper extends EntityMapper<RfbEventAttendanceDTO, RfbEventAttendance> {

    @Mapping(source = "rfbEvent.id", target = "rfbEventId")
    @Mapping(source = "rfbUser.id", target = "rfbUserId")
    @Mapping(source = "rfbUser.username", target = "rfbUsername")
    @Mapping(source = "rfbEvent.eventDate", target = "rfbEventDate")
    @Mapping(source = "rfbEvent.rfbLocation.locationName", target = "rfbEventLocation")
    RfbEventAttendanceDTO toDto(RfbEventAttendance rfbEventAttendance);

    @Mapping(source = "rfbEventId", target = "rfbEvent")
    @Mapping(source = "rfbUserId", target = "rfbUser")
    RfbEventAttendance toEntity(RfbEventAttendanceDTO rfbEventAttendanceDTO);

    default RfbEventAttendance fromId(Long id) {
        if (id == null) {
            return null;
        }
        RfbEventAttendance rfbEventAttendance = new RfbEventAttendance();
        rfbEventAttendance.setId(id);
        return rfbEventAttendance;
    }
}
