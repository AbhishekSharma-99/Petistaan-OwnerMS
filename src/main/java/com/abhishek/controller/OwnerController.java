package com.abhishek.controller;

import com.abhishek.dto.OwnerDTO;
import com.abhishek.dto.UpdatePetDTO;
import com.abhishek.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RequestMapping("/owners")
@RestController
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<Integer> saveOwner(@RequestBody OwnerDTO ownerDTO) {
        Integer ownerId = ownerService.saveOwner(ownerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerId);
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<OwnerDTO> findOwner(@PathVariable int ownerId) {
        OwnerDTO ownerDTO = ownerService.findOwner(ownerId);
        return ResponseEntity.status(HttpStatus.OK).body(ownerDTO);
    }

    @PatchMapping("/{ownerId}")
    public ResponseEntity<Void> updatePetDetails(@PathVariable int ownerId, @RequestBody UpdatePetDTO updatePetDTO) {
        ownerService.updatePetDetails(ownerId,
                updatePetDTO.name());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{ownerId}")
    public ResponseEntity<Void> deleteOwner(@PathVariable int ownerId) {
        ownerService.deleteOwner(ownerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Page<OwnerDTO>> findAllOwners(Pageable pageable) {
        Page<OwnerDTO> ownerDTOPage = ownerService.findAllOwners(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ownerDTOPage);
    }
}
