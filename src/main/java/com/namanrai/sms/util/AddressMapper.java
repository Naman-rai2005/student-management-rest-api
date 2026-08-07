package com.namanrai.sms.util;

import com.namanrai.sms.dto.AddressDTO;
import com.namanrai.sms.entity.Address;

public class AddressMapper {

    public static AddressDTO toDTO(Address address) {

        AddressDTO dto = new AddressDTO();

        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());

        return dto;
    }

    public static Address toEntity(AddressDTO dto) {

        Address address = new Address();

        address.setId(dto.getId());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());

        return address;
    }
}