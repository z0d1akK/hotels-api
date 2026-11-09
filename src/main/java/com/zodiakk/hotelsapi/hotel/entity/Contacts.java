package com.zodiakk.hotelsapi.hotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacts {

    @Column(name = "phone", nullable = false, length = 64)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;
}
