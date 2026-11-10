package com.zodiakk.hotelsapi.hotel.service.impl;

import com.zodiakk.hotelsapi.common.exception.ConflictException;
import com.zodiakk.hotelsapi.common.exception.HotelNotFoundException;
import com.zodiakk.hotelsapi.common.exception.InvalidTimeZoneException;
import com.zodiakk.hotelsapi.hotel.dto.filter.HotelSearchFilterDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelCreateDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelUpdateDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelDetailResponseDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelShortResponseDto;
import com.zodiakk.hotelsapi.hotel.entity.Amenity;
import com.zodiakk.hotelsapi.hotel.entity.Hotel;
import com.zodiakk.hotelsapi.hotel.mapper.HotelMapper;
import com.zodiakk.hotelsapi.hotel.repository.AmenityRepository;
import com.zodiakk.hotelsapi.hotel.repository.HotelRepository;
import com.zodiakk.hotelsapi.hotel.support.HotelTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void findAllShort_returnsMappedHotels() {
        Hotel hotel = hotel(1L);
        HotelShortResponseDto shortDto = HotelShortResponseDto.builder().id(1L).name(HotelTestData.NAME).build();
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(hotelMapper.toShortDto(hotel)).thenReturn(shortDto);

        List<HotelShortResponseDto> result = hotelService.findAllShort();

        assertThat(result).containsExactly(shortDto);
    }

    @Test
    void findByIdDetail_returnsDetail() {
        Hotel hotel = hotel(1L);
        HotelDetailResponseDto detail = HotelDetailResponseDto.builder().id(1L).name(HotelTestData.NAME).build();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDetailDto(hotel)).thenReturn(detail);

        HotelDetailResponseDto result = hotelService.findByIdDetail(1L);

        assertThat(result).isEqualTo(detail);
    }

    @Test
    void findByIdDetail_throwsWhenMissing() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.findByIdDetail(99L))
                .isInstanceOf(HotelNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void search_usesSpecification() {
        Hotel hotel = hotel(1L);
        HotelShortResponseDto shortDto = HotelShortResponseDto.builder().id(1L).build();
        when(hotelRepository.findAll(any(Specification.class))).thenReturn(List.of(hotel));
        when(hotelMapper.toShortDto(hotel)).thenReturn(shortDto);

        List<HotelShortResponseDto> result = hotelService.search(HotelSearchFilterDto.builder().city(HotelTestData.CITY).build());

        assertThat(result).containsExactly(shortDto);
        verify(hotelRepository).findAll(any(Specification.class));
    }

    @Test
    void create_savesHotelWithoutAmenities() {
        HotelCreateDto dto = HotelTestData.createDto();
        Hotel mapped = hotel(null);
        Hotel saved = hotel(1L);
        HotelShortResponseDto shortDto = HotelShortResponseDto.builder().id(1L).build();

        when(hotelMapper.toEntity(dto)).thenReturn(mapped);
        when(hotelRepository.save(mapped)).thenReturn(saved);
        when(hotelMapper.toShortDto(saved)).thenReturn(shortDto);

        HotelShortResponseDto result = hotelService.create(dto);

        assertThat(result).isEqualTo(shortDto);
        assertThat(mapped.getAmenities()).isEmpty();
    }

    @Test
    void create_throwsOnInvalidTimeZone() {
        HotelCreateDto dto = HotelTestData.createDto();
        dto.setArrivalTime(HotelTestData.arrivalTimeRequest("Not/AZone"));

        assertThatThrownBy(() -> hotelService.create(dto))
                .isInstanceOf(InvalidTimeZoneException.class)
                .hasMessageContaining("Not/AZone");
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void update_updatesAndReturnsDetail() {
        HotelUpdateDto dto = HotelTestData.updateDto();
        Hotel hotel = hotel(1L);
        HotelDetailResponseDto detail = HotelDetailResponseDto.builder().id(1L).build();

        when(hotelRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.saveAndFlush(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailDto(hotel)).thenReturn(detail);

        HotelDetailResponseDto result = hotelService.update(1L, dto);

        assertThat(result).isEqualTo(detail);
        verify(hotelMapper).updateEntity(dto, hotel);
    }

    @Test
    void update_throwsWhenMissing() {
        when(hotelRepository.findByIdForUpdate(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.update(5L, HotelTestData.updateDto()))
                .isInstanceOf(HotelNotFoundException.class);
    }

    @Test
    void update_throwsOnOptimisticLock() {
        HotelUpdateDto dto = HotelTestData.updateDto();
        Hotel hotel = hotel(1L);
        when(hotelRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.saveAndFlush(hotel)).thenThrow(new OptimisticLockingFailureException("conflict"));

        assertThatThrownBy(() -> hotelService.update(1L, dto))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void delete_removesExistingHotel() {
        when(hotelRepository.existsById(1L)).thenReturn(true);

        hotelService.delete(1L);

        verify(hotelRepository).deleteById(1L);
    }

    @Test
    void delete_throwsWhenMissing() {
        when(hotelRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> hotelService.delete(1L))
                .isInstanceOf(HotelNotFoundException.class);
        verify(hotelRepository, never()).deleteById(any());
    }

    @Test
    void replaceAmenities_createsMissingAndReplaces() {
        Hotel hotel = hotel(1L);
        Amenity wifi = Amenity.builder().id(10L).name("Free WiFi").nameNormalized("free wifi").build();
        HotelDetailResponseDto detail = HotelDetailResponseDto.builder().id(1L).amenities(List.of("Free WiFi")).build();

        when(hotelRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByNameNormalized("free wifi")).thenReturn(Optional.empty());
        when(amenityRepository.saveAndFlush(any(Amenity.class))).thenReturn(wifi);
        when(hotelRepository.saveAndFlush(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailDto(hotel)).thenReturn(detail);

        HotelDetailResponseDto result = hotelService.replaceAmenities(1L, java.util.Arrays.asList(" Free WiFi ", " ", null));

        assertThat(result).isEqualTo(detail);
        ArgumentCaptor<Amenity> amenityCaptor = ArgumentCaptor.forClass(Amenity.class);
        verify(amenityRepository).saveAndFlush(amenityCaptor.capture());
        assertThat(amenityCaptor.getValue().getName()).isEqualTo("Free WiFi");
        assertThat(hotel.getAmenities()).containsExactly(wifi);
    }

    @Test
    void replaceAmenities_usesExistingAmenity() {
        Hotel hotel = hotel(1L);
        Amenity parking = Amenity.builder().id(2L).name("Free parking").nameNormalized("free parking").build();
        when(hotelRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByNameNormalized("free parking")).thenReturn(Optional.of(parking));
        when(hotelRepository.saveAndFlush(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailDto(hotel)).thenReturn(HotelDetailResponseDto.builder().id(1L).build());

        hotelService.replaceAmenities(1L, List.of("Free parking"));

        verify(amenityRepository, never()).saveAndFlush(any());
        assertThat(hotel.getAmenities()).containsExactly(parking);
    }

    @Test
    void replaceAmenities_handlesNullListAsClear() {
        Hotel hotel = hotel(1L);
        hotel.getAmenities().add(Amenity.builder().id(1L).name("Old").nameNormalized("old").build());
        when(hotelRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.saveAndFlush(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailDto(hotel)).thenReturn(HotelDetailResponseDto.builder().id(1L).build());

        hotelService.replaceAmenities(1L, null);

        assertThat(hotel.getAmenities()).isEmpty();
    }

    @Test
    void replaceAmenities_throwsWhenHotelMissing() {
        when(hotelRepository.findByIdForUpdate(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.replaceAmenities(1L, List.of("WiFi")))
                .isInstanceOf(HotelNotFoundException.class);
    }

    @Test
    void replaceAmenities_throwsOnOptimisticLock() {
        Hotel hotel = hotel(1L);
        when(hotelRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.saveAndFlush(hotel)).thenThrow(new OptimisticLockingFailureException("lock"));

        assertThatThrownBy(() -> hotelService.replaceAmenities(1L, List.of()))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void replaceAmenities_recoversFromConcurrentAmenityCreate() {
        Hotel hotel = hotel(1L);
        Amenity existing = Amenity.builder().id(3L).name("Pool").nameNormalized("pool").build();
        when(hotelRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByNameNormalized("pool"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(existing));
        when(amenityRepository.saveAndFlush(any(Amenity.class)))
                .thenThrow(new DataIntegrityViolationException("unique"));
        when(hotelRepository.saveAndFlush(hotel)).thenReturn(hotel);
        when(hotelMapper.toDetailDto(hotel)).thenReturn(HotelDetailResponseDto.builder().id(1L).build());

        hotelService.replaceAmenities(1L, List.of("Pool"));

        assertThat(hotel.getAmenities()).containsExactly(existing);
    }

    @Test
    void replaceAmenities_throwsWhenAmenityConflictUnresolved() {
        Hotel hotel = hotel(1L);
        when(hotelRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByNameNormalized("spa")).thenReturn(Optional.empty());
        when(amenityRepository.saveAndFlush(any(Amenity.class)))
                .thenThrow(new DataIntegrityViolationException("unique"));

        assertThatThrownBy(() -> hotelService.replaceAmenities(1L, List.of("Spa")))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void create_allowsNullArrivalTimeZoneBranch() {
        HotelCreateDto dto = HotelTestData.createDto();
        dto.setArrivalTime(null);
        Hotel mapped = hotel(null);
        when(hotelMapper.toEntity(dto)).thenReturn(mapped);
        when(hotelRepository.save(mapped)).thenReturn(hotel(1L));
        when(hotelMapper.toShortDto(any())).thenReturn(HotelShortResponseDto.builder().id(1L).build());

        hotelService.create(dto);

        verify(hotelRepository).save(mapped);
    }

    private static Hotel hotel(Long id) {
        return HotelTestData.hotel(id);
    }
}
