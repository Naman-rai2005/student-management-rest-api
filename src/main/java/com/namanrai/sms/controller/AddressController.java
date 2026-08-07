package com.namanrai.sms.controller;

import com.namanrai.sms.dto.AddressDTO;
import com.namanrai.sms.service.AddressService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class AddressController {


    private final AddressService addressService;


    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }




    // CREATE
    @PostMapping("/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDTO addAddress(@Valid @RequestBody AddressDTO dto) {

        log.info("POST request received to create address");

        return addressService.saveAddress(dto);
    }




    // READ ALL
    @GetMapping("/addresses")
    public List<AddressDTO> getAllAddresses() {

        log.info("GET request received to fetch all addresses");

        return addressService.getAllAddresses();
    }




    // READ BY ID
    @GetMapping("/addresses/{id}")
    public AddressDTO getAddressById(@PathVariable Long id) {

        log.info("GET request received to fetch address with id: {}", id);

        return addressService.getAddressById(id);
    }




    // UPDATE
    @PutMapping("/addresses/{id}")
    public AddressDTO updateAddress(@PathVariable Long id,
                                    @Valid @RequestBody AddressDTO dto) {

        log.info("PUT request received to update address with id: {}", id);

        return addressService.updateAddress(id, dto);
    }




    // DELETE
    @DeleteMapping("/addresses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long id) {

        log.info("DELETE request received for address with id: {}", id);

        addressService.deleteAddress(id);
    }
}