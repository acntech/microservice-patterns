package no.acntech.order.resource;

import brave.ScopedSpan;
import brave.Tracer;
import no.acntech.order.model.ItemDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.UpdateItemDto;
import no.acntech.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping(path = "items")
@RestController
public class ItemsResource {

    private final Tracer tracer;
    private final OrderService orderService;

    public ItemsResource(final Tracer tracer,
                         final OrderService orderService) {
        this.tracer = tracer;
        this.orderService = orderService;
    }

    @GetMapping(path = "{itemId}")
    public ResponseEntity<ItemDto> get(@PathVariable("itemId") final UUID itemId) {
        ScopedSpan span = tracer.startScopedSpan("ItemsResource#get");
        try {
            ItemDto item = orderService.getItem(itemId);
            return ResponseEntity.ok(item);
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @PutMapping(path = "{itemId}")
    public ResponseEntity<OrderDto> put(@PathVariable("itemId") final UUID itemId,
                                        @Valid @RequestBody final UpdateItemDto updateItem) {
        ScopedSpan span = tracer.startScopedSpan("ItemsResource#put");
        try {
            orderService.updateItem(itemId, updateItem);
            return ResponseEntity.ok().build();
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }

    @DeleteMapping(path = "{itemId}")
    public ResponseEntity delete(@PathVariable("itemId") final UUID itemId) {
        ScopedSpan span = tracer.startScopedSpan("ItemsResource#delete");
        try {
            orderService.deleteItem(itemId);
            return ResponseEntity.ok().build();
        } catch (Throwable t) {
            span.error(t);
            throw t;
        } finally {
            span.finish();
        }
    }
}
