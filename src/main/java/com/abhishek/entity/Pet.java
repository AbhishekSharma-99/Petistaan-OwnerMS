package com.abhishek.entity;

import com.abhishek.enums.Gender;
import com.abhishek.enums.PetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "pet_table")
@Entity
public abstract class Pet extends Base{

    @Column(name = "name", nullable = false)
    private String name;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PetType petType;
}