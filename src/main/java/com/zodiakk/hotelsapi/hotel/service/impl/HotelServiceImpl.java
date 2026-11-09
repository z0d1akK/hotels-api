package com.zodiakk.hotelsapi.hotel.service.impl;

import com.zodiakk.hotelsapi.common.exception.ConflictException;
import com.zodiakk.hotelsapi.common.exception.HotelNotFoundException;
import com.zodiakk.hotelsapi.common.exception.InvalidTimeZoneException;
import com.zodiakk.hotelsapi.common.message.ErrorMessages;
import com.zodiakk.hotelsapi.hotel.dto.filter.HotelSearchFilterDto;
import com.zodiakk.hotelsapi.hotel.dto.request.ArrivalTimeRequestDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelCreateDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelUpdateDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelDetailResponseDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelShortResponseDto;
import com.zodiakk.hotelsapi.hotel.entity.Amenity;
import com.zodiakk.hotelsapi.hotel.entity.Hotel;
import com.zodiakk.hotelsapi.hotel.mapper.HotelMapper;
import com.zodiakk.hotelsapi.hotel.repository.AmenityRepository;
import com.zodiakk.hotelsapi.hotel.repository.HotelRepository;
import com.zodiakk.hotelsapi.hotel.repository.specification.HotelSpecification;
import com.zodiakk.hotelsapi.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    private final AmenityRepository amenityRepository;

    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortResponseDto> findAllShort() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDetailResponseDto findByIdDetail(Long id) {
        Hotel hotel = getHotelOrThrow(id);

        hotel.getAmenities().size();

        return hotelMapper.toDetailDto(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelShortResponseDto> search(HotelSearchFilterDto filter) {
        return hotelRepository.findAll(HotelSpecification.filter(filter)).stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }

    @Override
    public HotelShortResponseDto create(HotelCreateDto dto) {
        validateTimeZone(dto.getArrivalTime());

        Hotel hotel = hotelMapper.toEntity(dto);

        hotel.setAmenities(new LinkedHashSet<>());

        Hotel saved = hotelRepository.save(hotel);

        return hotelMapper.toShortDto(saved);
    }

    @Override
    public HotelDetailResponseDto update(Long id, HotelUpdateDto dto) {
        validateTimeZone(dto.getArrivalTime());

        Hotel hotel = hotelRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new HotelNotFoundException(ErrorMessages.HOTEL_NOT_FOUND.formatted(id)));

        hotelMapper.updateEntity(dto, hotel);

        try {
            Hotel saved = hotelRepository.saveAndFlush(hotel);
            saved.getAmenities().size();

            return hotelMapper.toDetailDto(saved);
        } catch (OptimisticLockingFailureException ex) {
            throw new ConflictException(ErrorMessages.HOTEL_OPTIMISTIC_LOCK, ex);
        }
    }

    @Override
    public void delete(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new HotelNotFoundException(ErrorMessages.HOTEL_NOT_FOUND.formatted(id));
        }
        hotelRepository.deleteById(id);
    }

    @Override
    public HotelDetailResponseDto replaceAmenities(Long id, List<String> amenityNames) {
        Hotel hotel = hotelRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new HotelNotFoundException(ErrorMessages.HOTEL_NOT_FOUND.formatted(id)));

        List<String> safeNames = amenityNames == null ? List.of() : amenityNames;

        Set<Amenity> resolved = new LinkedHashSet<>();

        for (String rawName : safeNames) {
            if (!StringUtils.hasText(rawName)) {
                continue;
            }
            resolved.add(resolveAmenity(rawName.trim()));
        }

        hotel.setAmenities(resolved);

        try {
            Hotel saved = hotelRepository.saveAndFlush(hotel);
            saved.getAmenities().size();
            return hotelMapper.toDetailDto(saved);
        } catch (OptimisticLockingFailureException ex) {
            throw new ConflictException(ErrorMessages.HOTEL_OPTIMISTIC_LOCK, ex);
        }
    }

    private Amenity resolveAmenity(String name) {
        String normalized = name.toLowerCase(Locale.ROOT);
        return amenityRepository.findByNameNormalized(normalized)
                .orElseGet(() -> createAmenity(name, normalized));
    }

    private Amenity createAmenity(String name, String normalized) {
        try {
            Amenity amenity = Amenity.builder()
                    .name(name)
                    .nameNormalized(normalized)
                    .build();
            return amenityRepository.saveAndFlush(amenity);
        } catch (DataIntegrityViolationException ex) {
            return amenityRepository.findByNameNormalized(normalized)
                    .orElseThrow(() -> new ConflictException(ErrorMessages.AMENITY_CONFLICT, ex));
        }
    }

    private Hotel getHotelOrThrow(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(ErrorMessages.HOTEL_NOT_FOUND.formatted(id)));
    }

    private void validateTimeZone(ArrivalTimeRequestDto arrivalTime) {
        if (arrivalTime == null || !StringUtils.hasText(arrivalTime.getTimeZone())) {
            return;
        }
        try {
            ZoneId.of(arrivalTime.getTimeZone().trim());
        } catch (DateTimeException ex) {
            throw new InvalidTimeZoneException(ErrorMessages.INVALID_TIME_ZONE.formatted(arrivalTime.getTimeZone()), ex);
        }
    }
}
