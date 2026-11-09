package com.zodiakk.hotelsapi.hotel.service;

import com.zodiakk.hotelsapi.common.exception.ConflictException;
import com.zodiakk.hotelsapi.common.exception.HotelNotFoundException;
import com.zodiakk.hotelsapi.common.exception.InvalidTimeZoneException;
import com.zodiakk.hotelsapi.hotel.dto.filter.HotelSearchFilterDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelCreateDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelUpdateDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelDetailResponseDto;
import com.zodiakk.hotelsapi.hotel.dto.response.HotelShortResponseDto;

import java.util.List;

/**
 * Application service for hotel CRUD, search and amenities management.
 */
public interface HotelService {

    /**
     * Returns short information for all hotels.
     *
     * @return list of short hotel DTOs
     */
    List<HotelShortResponseDto> findAllShort();

    /**
     * Returns detailed information for a hotel by id.
     *
     * @param id hotel identifier
     * @return detailed hotel DTO
     * @throws HotelNotFoundException if hotel does not exist
     */
    HotelDetailResponseDto findByIdDetail(Long id);

    /**
     * Searches hotels by filter criteria using AND semantics.
     *
     * @param filter search filter
     * @return matching hotels in short representation
     */
    List<HotelShortResponseDto> search(HotelSearchFilterDto filter);

    /**
     * Creates a new hotel without amenities.
     *
     * @param dto create payload
     * @return short hotel representation
     * @throws InvalidTimeZoneException if arrival time zone is invalid
     */
    HotelShortResponseDto create(HotelCreateDto dto);

    /**
     * Fully updates scalar and embedded hotel fields without changing amenities.
     *
     * @param id  hotel identifier
     * @param dto update payload
     * @return detailed hotel representation
     * @throws HotelNotFoundException   if hotel does not exist
     * @throws ConflictException        on optimistic lock conflict
     * @throws InvalidTimeZoneException if arrival time zone is invalid
     */
    HotelDetailResponseDto update(Long id, HotelUpdateDto dto);

    /**
     * Deletes a hotel by id.
     *
     * @param id hotel identifier
     * @throws HotelNotFoundException if hotel does not exist
     */
    void delete(Long id);

    /**
     * Replaces the full amenities set of a hotel. Missing amenities are created on the fly.
     *
     * @param id           hotel identifier
     * @param amenityNames amenity display names; empty list clears amenities
     * @return detailed hotel representation
     * @throws HotelNotFoundException if hotel does not exist
     * @throws ConflictException      on optimistic lock or amenity unique conflict
     */
    HotelDetailResponseDto replaceAmenities(Long id, List<String> amenityNames);
}
