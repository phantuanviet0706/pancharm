package com.example.pancharm.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShippingAddressResponse {
    String recipientName;
    String address;
    String ward;
    String district;
    String province;
    String phoneNumber;
    //	String zipCode;
    short isDefault;
    String config;
}
