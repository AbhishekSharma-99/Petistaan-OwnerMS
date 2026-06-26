package com.abhishek.service.impl;

import com.abhishek.dto.MailDTO;
import com.abhishek.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private final RestClient restClient;

    @Value("${mail.service.url}")
    private String mailServiceUrl;

    @Override
    public String sendEmail(MailDTO mailDTO) {
        ResponseEntity<String> response = restClient.post()
                .uri(mailServiceUrl)
                .body(mailDTO)
                .retrieve()
                .toEntity(String.class);
        return response.getBody();
    }

}
