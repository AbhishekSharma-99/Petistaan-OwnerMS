package com.abhishek.service;

import com.abhishek.dto.OwnerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OwnerService {

    Integer saveOwner(OwnerDTO ownerDTO);

    OwnerDTO findOwner(int ownerId);

    void updatePetDetails(int ownerId, String petName);

    void deleteOwner(int ownerId);

    Page<OwnerDTO> findAllOwners(Pageable pageable);
}
