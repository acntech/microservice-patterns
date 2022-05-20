package no.acntech.shipment.service;

import no.acntech.shipment.exception.ShipmentNotFoundException;
import no.acntech.shipment.model.CreateShipmentDto;
import no.acntech.shipment.model.ShipmentDto;
import no.acntech.shipment.model.ShipmentEntity;
import no.acntech.shipment.model.ShipmentQuery;
import no.acntech.shipment.repository.ShipmentRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@Service
public class ShipmentService {

    private static final Sort SORT_BY_ID = Sort.by("id");
    private final ConversionService conversionService;
    private final ShipmentRepository shipmentRepository;

    public ShipmentService(final ConversionService conversionService,
                           final ShipmentRepository shipmentRepository) {
        this.conversionService = conversionService;
        this.shipmentRepository = shipmentRepository;
    }

    @SuppressWarnings("Duplicates")
    public List<ShipmentDto> findShipments(@NotNull @Valid final ShipmentQuery shipmentQuery) {
        final var customerId = shipmentQuery.getCustomerId();
        final var orderId = shipmentQuery.getOrderId();
        final var status = shipmentQuery.getStatus();
        if (customerId != null && orderId != null && status != null) {
            return shipmentRepository.findAllByCustomerIdAndOrderIdAndStatus(customerId, orderId, status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (customerId != null && status != null) {
            return shipmentRepository.findAllByCustomerIdAndStatus(customerId, status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (orderId != null && status != null) {
            return shipmentRepository.findAllByOrderIdAndStatus(orderId, status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (customerId != null) {
            return shipmentRepository.findAllByCustomerId(customerId)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (orderId != null) {
            return shipmentRepository.findAllByOrderId(orderId)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else if (status != null) {
            return shipmentRepository.findAllByStatus(status)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        } else {
            return shipmentRepository.findAll(SORT_BY_ID)
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    public ShipmentDto getShipment(@NotNull final UUID shipmentId) {
        return shipmentRepository.findByShipmentId(shipmentId)
                .map(this::convert)
                .orElseThrow(() -> new ShipmentNotFoundException(shipmentId));
    }

    @SuppressWarnings("UnusedReturnValue")
    @Transactional
    public ShipmentDto createShipment(@NotNull @Valid final CreateShipmentDto createShipment) {
        ShipmentEntity shipment = conversionService.convert(createShipment, ShipmentEntity.class);
        Assert.notNull(shipment, "Failed to convert CreateShipmentDto to ShipmentEntity");
        ShipmentEntity savedShipmentEntity = shipmentRepository.save(shipment);
        return convert(savedShipmentEntity);
    }

    private ShipmentDto convert(ShipmentEntity source) {
        final var shipmentDto = conversionService.convert(source, ShipmentDto.class);
        Assert.notNull(source, "Failed to convert ShipmentEntity to ShipmentDto");
        return shipmentDto;
    }
}
