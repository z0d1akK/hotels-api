package com.zodiakk.hotelsapi.hotel.repository.specification;

import com.zodiakk.hotelsapi.hotel.dto.filter.HotelSearchFilterDto;
import com.zodiakk.hotelsapi.hotel.entity.Amenity;
import com.zodiakk.hotelsapi.hotel.entity.Hotel;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@UtilityClass
public class HotelSpecification {

    public static Specification<Hotel> filter(HotelSearchFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(filter.getName())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        contains(filter.getName())
                ));
            }

            if (StringUtils.hasText(filter.getBrand())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("brand")),
                        contains(filter.getBrand())
                ));
            }

            if (StringUtils.hasText(filter.getCity())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("address").get("city")),
                        contains(filter.getCity())
                ));
            }

            if (StringUtils.hasText(filter.getCountry())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("address").get("country")),
                        contains(filter.getCountry())
                ));
            }

            if (filter.getAmenities() != null && !filter.getAmenities().isEmpty()) {
                List<String> normalizedAmenities = filter.getAmenities().stream()
                        .filter(StringUtils::hasText)
                        .map(value -> value.trim().toLowerCase(Locale.ROOT))
                        .distinct()
                        .toList();

                for (String amenityName : normalizedAmenities) {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Hotel> subRoot = subquery.from(Hotel.class);
                    Join<Hotel, Amenity> amenityJoin = subRoot.join("amenities", JoinType.INNER);
                    subquery.select(subRoot.get("id"))
                            .where(
                                    criteriaBuilder.equal(subRoot.get("id"), root.get("id")),
                                    criteriaBuilder.equal(amenityJoin.get("nameNormalized"), amenityName)
                            );
                    predicates.add(criteriaBuilder.exists(subquery));
                }

                if (query != null) query.distinct(true);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String contains(String value) {
        return "%" + value.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
