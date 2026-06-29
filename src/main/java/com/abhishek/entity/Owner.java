package com.abhishek.entity;

import com.abhishek.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "owner_table")
@Entity
public class Owner extends Base {

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "state", nullable = false)
    private String state;
    @Column(name = "mobile_number", nullable = false, length = 10)
    private String mobileNumber;
    @Column(name = "email_id", nullable = false, unique = true)
    private String emailId;
    @Column(name = "pet_id", nullable = false, unique = true)
    private Integer petId;

}
