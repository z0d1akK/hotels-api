package com.zodiakk.hotelsapi.hotel.repository;

import com.zodiakk.hotelsapi.hotel.entity.Hotel;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Persistence access for {@link Hotel} aggregates.
 */
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    /**
     * Loads a hotel with a pessimistic write lock for concurrent updates and amenity replacement.
     *
     * @param id hotel identifier
     * @return hotel if present
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select h from Hotel h where h.id = :id")
    Optional<Hotel> findByIdForUpdate(@Param("id") Long id);

    /**
     * Aggregates hotels by brand for histogram.
     *
     * @return pairs of brand and hotel count
     */
    @Query("select h.brand, count(h) from Hotel h group by h.brand")
    List<Object[]> countGroupByBrand();

    /**
     * Aggregates hotels by city for histogram.
     *
     * @return pairs of city and hotel count
     */
    @Query("select h.address.city, count(h) from Hotel h group by h.address.city")
    List<Object[]> countGroupByCity();

    /**
     * Aggregates hotels by country for histogram.
     *
     * @return pairs of country and hotel count
     */
    @Query("select h.address.country, count(h) from Hotel h group by h.address.country")
    List<Object[]> countGroupByCountry();
}
