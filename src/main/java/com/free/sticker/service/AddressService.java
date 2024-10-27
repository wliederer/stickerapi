package com.free.sticker.service;

import com.free.sticker.models.Address;
import com.free.sticker.models.AddressDTO;
import com.free.sticker.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

}
