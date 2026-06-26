package com.abhishek.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString(callSuper = true)
@Setter
@Getter
public final class DomesticPetDTO extends PetDTO {

    private LocalDate birthDate;

}
