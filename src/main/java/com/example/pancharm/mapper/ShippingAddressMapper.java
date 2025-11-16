package com.example.pancharm.mapper;

import org.mapstruct.Mapper;

import com.example.pancharm.dto.request.shipping.ShippingAddressRequest;
import com.example.pancharm.entity.ShippingAddresses;

@Mapper(componentModel = "spring")
public interface ShippingAddressMapper {
    ShippingAddresses toShippingAddresses(ShippingAddressRequest request);
}
