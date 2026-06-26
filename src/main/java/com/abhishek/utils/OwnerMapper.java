package com.abhishek.utils;

import com.abhishek.dto.DomesticPetDTO;
import com.abhishek.dto.OwnerDTO;
import com.abhishek.dto.PetDTO;
import com.abhishek.dto.WildPetDTO;
import com.abhishek.entity.DomesticPet;
import com.abhishek.entity.Owner;
import com.abhishek.entity.Pet;
import com.abhishek.entity.WildPet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OwnerMapper {

    String UNSUPPORTED_PET_INSTANCE = "Unsupported pet instance: %s";

    @Mapping(source = "petDTO", target = "pet")
    Owner ownerDTOToOwner(OwnerDTO ownerDTO);

    default Pet petDTOToPet(PetDTO petDTO) {
        return switch (petDTO) {
            case DomesticPetDTO domesticPetDTO -> domesticPetDTOToDomesticPet(domesticPetDTO);
            case WildPetDTO wildPetDTO -> wildPetDTOToWildPet(wildPetDTO);
            default -> throw new IllegalArgumentException(String.format(UNSUPPORTED_PET_INSTANCE, petDTO.getClass()));
        };
    }

    DomesticPet domesticPetDTOToDomesticPet(DomesticPetDTO domesticPetDTO);

    WildPet wildPetDTOToWildPet(WildPetDTO wildPetDTO);

    @Mapping(source = "pet", target = "petDTO")
    OwnerDTO ownerToOwnerDTO(Owner owner);

    default PetDTO petToPetDTO(Pet pet){

        return switch (pet) {
            case DomesticPet domesticPet -> domesticPetToDomesticPetDTO(domesticPet);
            case WildPet wildPet -> wildPetToWildPetDTO(wildPet);
            default -> throw new IllegalArgumentException(String.format(UNSUPPORTED_PET_INSTANCE, pet.getClass()));
        };
    }

    DomesticPetDTO domesticPetToDomesticPetDTO(DomesticPet domesticPet);

    WildPetDTO wildPetToWildPetDTO(WildPet wildPet);

}
