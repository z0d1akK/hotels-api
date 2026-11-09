package com.zodiakk.hotelsapi.hotel.repository;

import com.zodiakk.hotelsapi.hotel.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Persistence access for {@link Amenity} catalog entries.
 */
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    /**
     * Finds an amenity by its normalized (lowercased trimmed) name.
     *
     * @param nameNormalized normalized amenity name
     * @return amenity if present
     */
    Optional<Amenity> findByNameNormalized(String nameNormalized);

    /**
     * Counts hotels per amenity including amenities with zero hotels.
     *
     * @return pairs of amenity display name and hotel count
     */
    @Query("""
            select a.name, count(h.id)
            from Amenity a
            left join Hotel h on a member of h.amenities
            group by a.id, a.name
            order by a.name
            """)
    List<Object[]> countHotelsGroupedByAmenity();
}
