package com.example.pancharm.service.shipping;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.shipping.ShippingAddressRequest;
import com.example.pancharm.entity.ShippingAddresses;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.ShippingAddressMapper;
import com.example.pancharm.repository.ShippingAddressRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShippingAddressService {
    ShippingAddressRepository shippingAddressRepository;
    ShippingAddressMapper shippingAddressMapper;

    public ShippingAddresses createSimpleData(ShippingAddressRequest request) {
        var shippingAddress = shippingAddressMapper.toShippingAddresses(request);

        try {
            shippingAddress = shippingAddressRepository.save(shippingAddress);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return shippingAddress;
    }
}
