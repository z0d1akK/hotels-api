package com.zodiakk.hotelsapi.hotel.mapper;

import com.zodiakk.hotelsapi.hotel.dto.request.HotelCreateDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelUpdateDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelDetailResponseDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelShortResponseDto;
import com.zodiakk.hotelsapi.hotel.entity.Address;
import com.zodiakk.hotelsapi.hotel.entity.Amenity;
import com.zodiakk.hotelsapi.hotel.entity.Hotel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Hotel toEntity(HotelCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(HotelUpdateDto dto, @MappingTarget Hotel hotel);

    @Mapping(target = "address", source = "address", qualifiedByName = "formatAddress")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortResponseDto toShortDto(Hotel hotel);

    @Mapping(target = "amenities", source = "amenities")
    HotelDetailResponseDto toDetailDto(Hotel hotel);

    @AfterMapping
    default void initAmenitiesOnCreate(@MappingTarget Hotel hotel) {
        if (hotel.getAmenities() == null) {
            hotel.setAmenities(new java.util.LinkedHashSet<>());
        }
    }

    @Named("formatAddress")
    default String formatAddress(Address address) {
        if (address == null) {
            return null;
        }
        return "%d %s, %s, %s, %s".formatted(
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry()
        );
    }

    default List<String> mapAmenities(Set<Amenity> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            return List.of();
        }
        return amenities.stream().map(Amenity::getName).toList();
    }

    default String map(ZoneId zoneId) {
        return zoneId == null ? null : zoneId.getId();
    }

    default ZoneId map(String zone) {
        return zone == null ? null : ZoneId.of(zone);
    }
}
