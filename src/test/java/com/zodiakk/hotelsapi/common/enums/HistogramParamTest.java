package com.zodiakk.hotelsapi.common.enums;

import com.zodiakk.hotelsapi.common.exception.InvalidHistogramParamException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HistogramParamTest {

    @Test
    void from_parsesCaseInsensitive() {
        assertThat(HistogramParam.from("city")).isEqualTo(HistogramParam.CITY);
        assertThat(HistogramParam.from("AMENITIES")).isEqualTo(HistogramParam.AMENITIES);
    }

    @Test
    void from_throwsOnUnknown() {
        assertThatThrownBy(() -> HistogramParam.from("rooms"))
                .isInstanceOf(InvalidHistogramParamException.class)
                .hasMessageContaining("rooms");
    }

    @Test
    void from_throwsOnBlank() {
        assertThatThrownBy(() -> HistogramParam.from(" "))
                .isInstanceOf(InvalidHistogramParamException.class);
        assertThatThrownBy(() -> HistogramParam.from(null))
                .isInstanceOf(InvalidHistogramParamException.class);
    }
}
