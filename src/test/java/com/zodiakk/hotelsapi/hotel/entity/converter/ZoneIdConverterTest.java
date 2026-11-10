package com.zodiakk.hotelsapi.hotel.entity.converter;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class ZoneIdConverterTest {

    private final ZoneIdConverter converter = new ZoneIdConverter();

    @Test
    void convertsBothWays() {
        ZoneId zone = ZoneId.of("Europe/Minsk");

        assertThat(converter.convertToDatabaseColumn(zone)).isEqualTo("Europe/Minsk");
        assertThat(converter.convertToEntityAttribute("Europe/Minsk")).isEqualTo(zone);
    }

    @Test
    void convertsNulls() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
        assertThat(converter.convertToEntityAttribute(null)).isNull();
    }
}
