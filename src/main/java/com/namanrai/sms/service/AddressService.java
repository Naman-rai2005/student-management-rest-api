package com.namanrai.sms.service;

import com.namanrai.sms.dto.AddressDTO;
import com.namanrai.sms.entity.Address;
import com.namanrai.sms.exception.AddressNotFoundException;
import com.namanrai.sms.repository.AddressRepository;
import com.namanrai.sms.util.AddressMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AddressService {


    private final AddressRepository addressRepository;


    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }



    // CREATE
    public AddressDTO saveAddress(AddressDTO dto) {

        log.info("Creating address for city: {}", dto.getCity());


        Address address = AddressMapper.toEntity(dto);


        Address savedAddress = addressRepository.save(address);


        log.info("Address created successfully with id: {}", savedAddress.getId());


        return AddressMapper.toDTO(savedAddress);
    }




    // READ ALL
    public List<AddressDTO> getAllAddresses() {

        log.info("Fetching all addresses");


        return addressRepository.findAll()
                .stream()
                .map(AddressMapper::toDTO)
                .toList();
    }




    // READ BY ID
    public AddressDTO getAddressById(Long id) {

        log.info("Fetching address with id: {}", id);


        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new AddressNotFoundException(
                                "Address not found with id: " + id));


        return AddressMapper.toDTO(address);
    }




    // UPDATE
    public AddressDTO updateAddress(Long id, AddressDTO dto) {

        log.info("Updating address with id: {}", id);


        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() ->
                        new AddressNotFoundException(
                                "Address not found with id: " + id));


        existingAddress.setStreet(dto.getStreet());
        existingAddress.setCity(dto.getCity());
        existingAddress.setState(dto.getState());
        existingAddress.setPostalCode(dto.getPostalCode());
        existingAddress.setCountry(dto.getCountry());


        Address updatedAddress = addressRepository.save(existingAddress);


        log.info("Address updated successfully with id: {}", id);


        return AddressMapper.toDTO(updatedAddress);
    }




    // DELETE
    public void deleteAddress(Long id) {

        log.info("Deleting address with id: {}", id);


        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new AddressNotFoundException(
                                "Address not found with id: " + id));


        addressRepository.delete(address);


        log.info("Address deleted successfully with id: {}", id);
    }
}