package com.abhishek.dto;

import com.abhishek.enums.Gender;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
public class OwnerDTO {

    @EqualsAndHashCode.Include
    private Integer id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String city;

    private String state;

    @EqualsAndHashCode.Include
    private String mobileNumber;

    @EqualsAndHashCode.Include
    private String emailId;

    private PetDTO petDTO;

}