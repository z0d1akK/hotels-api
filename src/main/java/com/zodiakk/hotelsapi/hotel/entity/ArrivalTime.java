package com.zodiakk.hotelsapi.hotel.entity;

import com.zodiakk.hotelsapi.hotel.entity.converter.ZoneIdConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.time.ZoneId;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrivalTime {

    @Column(name = "check_in", nullable = false)
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    @Convert(converter = ZoneIdConverter.class)
    @Column(name = "time_zone", nullable = false, length = 64)
    private ZoneId timeZone;
}
