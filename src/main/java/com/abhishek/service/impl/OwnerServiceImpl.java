package com.abhishek.service.impl;

import com.abhishek.dto.MailDTO;
import com.abhishek.dto.OwnerDTO;
import com.abhishek.entity.Owner;
import com.abhishek.exception.OwnerNotFoundException;
import com.abhishek.repository.OwnerRepository;
import com.abhishek.service.MailService;
import com.abhishek.service.OwnerService;
import com.abhishek.utils.OwnerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.abhishek.enums.MailType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final MailService mailService;
    @Value("${owner.not.found}")
    private String ownerNotFound;

    @Override
    public Integer saveOwner(OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.ownerDTOToOwner(ownerDTO);
        ownerRepository.save(owner);
        MailDTO mailDTO = new MailDTO(ownerDTO.getEmailId(), ownerDTO.getFirstName(), ownerDTO.getLastName(), WELCOME);
        log.info(mailService.sendEmail(mailDTO));
        return owner.getId();
    }

    @Override
    public OwnerDTO findOwner(int ownerId) {
        return ownerRepository.findById(ownerId)
                .map(ownerMapper::ownerToOwnerDTO)
                .orElseThrow(() -> new OwnerNotFoundException(String.format(ownerNotFound, ownerId)));
    }

    @Override
    public void updatePetDetails(int ownerId, String petName){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException(String.format(ownerNotFound, ownerId)));
        owner.getPet().setName(petName);
        ownerRepository.save(owner);
        MailDTO mailDTO = new MailDTO(owner.getEmailId(), owner.getFirstName(), owner.getLastName(), MODIFY);
        log.info(mailService.sendEmail(mailDTO));
    }

    @Override
    public void deleteOwner(int ownerId){
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException(String.format(ownerNotFound, ownerId)));
        ownerRepository.deleteById(ownerId);
        MailDTO mailDTO = new MailDTO(owner.getEmailId(), owner.getFirstName(), owner.getLastName(), EXIT);
        log.info(mailService.sendEmail(mailDTO));
    }

    @Override
    public Page<OwnerDTO> findAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable)
                .map(ownerMapper::ownerToOwnerDTO);
    }

}