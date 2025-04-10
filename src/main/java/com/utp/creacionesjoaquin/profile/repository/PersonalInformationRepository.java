package com.utp.creacionesjoaquin.profile.repository;

import com.utp.creacionesjoaquin.profile.model.PersonalInformation;
import com.utp.creacionesjoaquin.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalInformationRepository extends JpaRepository<PersonalInformation, String> {
    Optional<PersonalInformation> findByUser(User user);
}
