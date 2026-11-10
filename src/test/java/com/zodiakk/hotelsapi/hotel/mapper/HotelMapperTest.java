package com.zodiakk.hotelsapi.hotel.mapper;

import com.zodiakk.hotelsapi.hotel.dto.request.HotelCreateDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelUpdateDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelDetailResponseDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelShortResponseDto;
import com.zodiakk.hotelsapi.hotel.entity.Amenity;
import com.zodiakk.hotelsapi.hotel.entity.Hotel;
import com.zodiakk.hotelsapi.hotel.support.HotelTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HotelMapperTest {

    private HotelMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new HotelMapperImpl();
    }

    @Test
    void toEntity_mapsNestedFieldsAndInitAmenities() {
        HotelCreateDto dto = HotelTestData.createDto();

        Hotel hotel = mapper.toEntity(dto);

        assertThat(hotel.getName()).isEqualTo(dto.getName());
        assertThat(hotel.getBrand()).isEqualTo(dto.getBrand());
        assertThat(hotel.getAddress().getCity()).isEqualTo(HotelTestData.CITY);
        assertThat(hotel.getContacts().getEmail()).isEqualTo(HotelTestData.EMAIL);
        assertThat(hotel.getArrivalTime().getTimeZone()).isEqualTo(ZoneId.of(HotelTestData.TIME_ZONE));
        assertThat(hotel.getAmenities()).isEmpty();
        assertThat(hotel.getId()).isNull();
    }

    @Test
    void updateEntity_updatesFieldsWithoutTouchingAmenities() {
        Hotel hotel = HotelTestData.hotel(1L);
        Amenity amenity = HotelTestData.amenity(1L, "WiFi");
        hotel.setAmenities(new LinkedHashSet<>(Set.of(amenity)));
        HotelUpdateDto dto = HotelTestData.updateDto();

        mapper.updateEntity(dto, hotel);

        assertThat(hotel.getName()).isEqualTo(HotelTestData.UPDATED_NAME);
        assertThat(hotel.getBrand()).isEqualTo(HotelTestData.UPDATED_BRAND);
        assertThat(hotel.getAmenities()).containsExactly(amenity);
        assertThat(hotel.getId()).isEqualTo(1L);
    }

    @Test
    void toShortDto_formatsAddressAndPhone() {
        Hotel hotel = HotelTestData.hotel(1L);

        HotelShortResponseDto dto = mapper.toShortDto(hotel);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getAddress()).isEqualTo(HotelTestData.FORMATTED_ADDRESS);
        assertThat(dto.getPhone()).isEqualTo(HotelTestData.PHONE);
    }

    @Test
    void toDetailDto_mapsAmenitiesNames() {
        Hotel hotel = HotelTestData.hotel(1L);
        hotel.setAmenities(new LinkedHashSet<>(List.of(
                HotelTestData.amenity(1L, "Free WiFi"),
                HotelTestData.amenity(2L, "Free parking")
        )));

        HotelDetailResponseDto dto = mapper.toDetailDto(hotel);

        assertThat(dto.getBrand()).isEqualTo(HotelTestData.BRAND);
        assertThat(dto.getArrivalTime().getTimeZone()).isEqualTo(HotelTestData.TIME_ZONE);
        assertThat(dto.getAmenities()).containsExactly("Free WiFi", "Free parking");
    }

    @Test
    void formatAddress_nullSafe() {
        assertThat(mapper.formatAddress(null)).isNull();
    }

    @Test
    void mapAmenities_handlesNullAndEmpty() {
        assertThat(mapper.mapAmenities(null)).isEmpty();
        assertThat(mapper.mapAmenities(Set.of())).isEmpty();
    }

    @Test
    void zoneIdMapping_roundTrip() {
        assertThat(mapper.map((ZoneId) null)).isNull();
        assertThat(mapper.map((String) null)).isNull();
        assertThat(mapper.map(ZoneId.of("UTC"))).isEqualTo("UTC");
        assertThat(mapper.map(HotelTestData.TIME_ZONE)).isEqualTo(ZoneId.of(HotelTestData.TIME_ZONE));
    }

    @Test
    void initAmenitiesOnCreate_keepsExistingCollection() {
        Hotel hotel = Hotel.builder().amenities(new LinkedHashSet<>()).build();

        mapper.initAmenitiesOnCreate(hotel);

        assertThat(hotel.getAmenities()).isNotNull().isEmpty();
    }
}
