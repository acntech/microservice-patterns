package no.acntech.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReservationService {

    private final RestTemplate restTemplate;

    public ReservationService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
