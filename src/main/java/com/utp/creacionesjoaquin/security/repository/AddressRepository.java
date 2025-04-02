package com.utp.creacionesjoaquin.security.repository;

import com.utp.creacionesjoaquin.profile.model.Address;
import com.utp.creacionesjoaquin.profile.model.PersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    Optional<Address> findByPersonalInformation(PersonalInformation personalInformation);
}
