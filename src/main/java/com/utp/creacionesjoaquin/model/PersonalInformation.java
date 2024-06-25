package com.utp.creacionesjoaquin.model;

import com.utp.creacionesjoaquin.security.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter @Setter
@Table(name = "personal_information")
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate birthdate;
    private String photoUrl;
    @OneToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToOne(fetch = FetchType.EAGER)
    private Address address;

    public String getFullName() {
        return this.firstName + " " + this.getLastName();
    }
}
