package com.abhishek.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "wild_pet_table")
@Entity
public final class WildPet extends Pet {

    @Column(name = "place_of_birth", nullable = false)
    private String birthPlace;
}
