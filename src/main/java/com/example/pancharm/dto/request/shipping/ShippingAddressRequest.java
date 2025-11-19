package com.example.pancharm.dto.request.shipping;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingAddressRequest {
    @NotBlank(message = "ORDER_RECIPIENT_NAME_REQUIRE")
    String recipientName;

    @NotBlank(message = "ORDER_ADDRESS_REQUIRE")
    String address;

    String ward;
    String district;

    @NotBlank(message = "ORDER_PROVINCE_REQUIRE")
    String province;

    @NotBlank(message = "ORDER_PHONE_NUMBER_REQUIRE")
    String phoneNumber;

    String zipCode;
}
