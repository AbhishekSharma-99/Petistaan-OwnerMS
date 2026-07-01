package com.abhishek.utils;

import com.abhishek.dto.OwnerDTO;
import com.abhishek.entity.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OwnerMapper {

    @Mapping(source = "petDTO.id", target = "petId")
    Owner ownerDTOToOwner(OwnerDTO ownerDTO);

    @Mapping(target = "petDTO", ignore = true)
    OwnerDTO ownerToOwnerDTO(Owner owner);

}
