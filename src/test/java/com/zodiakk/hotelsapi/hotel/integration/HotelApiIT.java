package com.zodiakk.hotelsapi.hotel.integration;

import com.zodiakk.hotelsapi.common.constant.ApiPaths;
import com.zodiakk.hotelsapi.hotel.dto.request.AddressRequestDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelCreateDto;
import com.zodiakk.hotelsapi.hotel.dto.request.HotelUpdateDto;
import com.zodiakk.hotelsapi.hotel.support.HotelTestData;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HotelApiIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Long hotelId;

    private static Long secondHotelId;

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(mockMvc).isNotNull();
        assertThat(objectMapper).isNotNull();
    }

    @Test
    @Order(2)
    void createHotel_returnsCreatedShortDto() throws Exception {
        HotelCreateDto dto = HotelTestData.createDto();

        MvcResult result = mockMvc.perform(post(ApiPaths.HOTELS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(HotelTestData.NAME))
                .andExpect(jsonPath("$.address").value(HotelTestData.FORMATTED_ADDRESS))
                .andExpect(jsonPath("$.phone").value(HotelTestData.PHONE))
                .andReturn();

        hotelId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        assertThat(hotelId).isPositive();
    }

    @Test
    @Order(3)
    void createSecondHotel_forSearchFilters() throws Exception {
        HotelCreateDto dto = HotelCreateDto.builder()
                .name("City Inn")
                .description(HotelTestData.DESCRIPTION)
                .brand("brand")
                .address(AddressRequestDto.builder()
                        .houseNumber(HotelTestData.HOUSE_NUMBER)
                        .street(HotelTestData.STREET)
                        .city("Grodno")
                        .country(HotelTestData.COUNTRY)
                        .postCode(HotelTestData.POST_CODE)
                        .build())
                .contacts(HotelTestData.contactsRequest())
                .arrivalTime(HotelTestData.arrivalTimeRequest(HotelTestData.TIME_ZONE))
                .build();

        MvcResult result = mockMvc.perform(post(ApiPaths.HOTELS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        secondHotelId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    @Order(4)
    void getAllHotels_containsCreatedHotels() throws Exception {
        mockMvc.perform(get(ApiPaths.HOTELS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + hotelId + ")]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.id == " + secondHotelId + ")]").isNotEmpty());
    }

    @Test
    @Order(5)
    void getHotelById_returnsDetails() throws Exception {
        mockMvc.perform(get(ApiPaths.HOTELS + "/" + hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hotelId))
                .andExpect(jsonPath("$.brand").value(HotelTestData.BRAND))
                .andExpect(jsonPath("$.arrivalTime.timeZone").value(HotelTestData.TIME_ZONE))
                .andExpect(jsonPath("$.amenities").isArray());
    }

    @Test
    @Order(6)
    void replaceAmenities_updatesList() throws Exception {
        mockMvc.perform(post(ApiPaths.HOTELS + "/" + hotelId + "/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(HotelTestData.AMENITIES)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities.length()").value(2))
                .andExpect(jsonPath("$.amenities", containsInAnyOrder("Free WiFi", "Free parking")));

        mockMvc.perform(post(ApiPaths.HOTELS + "/" + secondHotelId + "/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                ["Free parking"]
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void search_byCityBrandCountryNameAndAmenities() throws Exception {
        mockMvc.perform(get(ApiPaths.SEARCH).param("city", "minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + hotelId + ")]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.id == " + secondHotelId + ")]").isEmpty());

        mockMvc.perform(get(ApiPaths.SEARCH).param("brand", "brand").param("country", "belarus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + secondHotelId + ")]").isNotEmpty());

        mockMvc.perform(get(ApiPaths.SEARCH).param("name", "hotel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + hotelId + ")]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.id == " + secondHotelId + ")]").isEmpty());

        mockMvc.perform(get(ApiPaths.SEARCH)
                        .param("amenities", "Free WiFi")
                        .param("amenities", "Free parking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + hotelId + ")]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.id == " + secondHotelId + ")]").isEmpty());

        mockMvc.perform(get(ApiPaths.SEARCH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + hotelId + ")]").isNotEmpty())
                .andExpect(jsonPath("$[?(@.id == " + secondHotelId + ")]").isNotEmpty());
    }

    @Test
    @Order(8)
    void histogramByCityAndAmenities() throws Exception {
        MvcResult cityResult = mockMvc.perform(get(ApiPaths.API_PREFIX + "/histogram/city"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode cityBody = objectMapper.readTree(cityResult.getResponse().getContentAsString());
        assertThat(cityBody.get(HotelTestData.CITY).asLong()).isGreaterThanOrEqualTo(1L);

        mockMvc.perform(get(ApiPaths.API_PREFIX + "/histogram/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Free WiFi']").isNumber());

        mockMvc.perform(get(ApiPaths.API_PREFIX + "/histogram/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['" + HotelTestData.BRAND + "']").isNumber());

        mockMvc.perform(get(ApiPaths.API_PREFIX + "/histogram/country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['" + HotelTestData.COUNTRY + "']").isNumber());
    }

    @Test
    @Order(9)
    void updateHotel_returnsUpdatedDetail() throws Exception {
        HotelUpdateDto dto = HotelTestData.updateDto();

        mockMvc.perform(put(ApiPaths.HOTELS + "/" + hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(HotelTestData.UPDATED_NAME))
                .andExpect(jsonPath("$.brand").value(HotelTestData.UPDATED_BRAND))
                .andExpect(jsonPath("$.amenities.length()").value(2));
    }

    @Test
    @Order(10)
    void errorCases_notFoundValidationAndUnknownHistogram() throws Exception {
        mockMvc.perform(get(ApiPaths.HOTELS + "/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        mockMvc.perform(post(ApiPaths.HOTELS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors").isArray());

        mockMvc.perform(get(ApiPaths.API_PREFIX + "/histogram/rooms"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(11)
    void deleteHotel_removesResource() throws Exception {
        mockMvc.perform(delete(ApiPaths.HOTELS + "/" + hotelId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(ApiPaths.HOTELS + "/" + hotelId))
                .andExpect(status().isNotFound());
    }
}
