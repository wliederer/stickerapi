package com.free.sticker.repository;

import com.free.sticker.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByLine1AndCityAndStateAndZipcode(String line1, String city, String state, String zipcode);

}
