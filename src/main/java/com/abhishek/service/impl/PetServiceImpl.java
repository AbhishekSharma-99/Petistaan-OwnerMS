package com.abhishek.service.impl;

import com.abhishek.dto.PetDTO;
import com.abhishek.dto.UpdatePetDTO;
import com.abhishek.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

    private final RestClient restClient;

    @Value("${pet.service.url}")
    private String petServiceUrl;

    @Override
    public Integer savePet(PetDTO petDTO) {
        ResponseEntity<Integer> response = restClient.post()
                .uri(petServiceUrl)
                .body(petDTO)
                .retrieve()
                .toEntity(Integer.class);
        return response.getBody();
    }

    @Override
    public PetDTO findPet(Integer petId) {
        ResponseEntity<PetDTO> response = restClient.get()
                .uri(petServiceUrl + "/{petId}", petId)
                .retrieve()
                .toEntity(PetDTO.class);
        return response.getBody();
    }

    @Override
    public void updatePetDetails(Integer petId, String petName) {

        UpdatePetDTO updatePetDTO = new UpdatePetDTO(petName);

        restClient.patch()
                .uri(petServiceUrl + "/{petId}", petId)
                .body(updatePetDTO)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void deletePet(Integer petId) {

        restClient.delete()
                .uri(petServiceUrl + "/{petId}", petId)
                .retrieve()
                .toBodilessEntity();
    }
}
