package com.abhishek.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@Setter
@Getter
public class WildPetDTO extends PetDTO {

    private String birthPlace;

}