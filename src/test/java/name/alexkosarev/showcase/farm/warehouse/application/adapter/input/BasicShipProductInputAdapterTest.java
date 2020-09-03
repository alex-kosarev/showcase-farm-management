package name.alexkosarev.showcase.farm.warehouse.application.adapter.input;

import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.FindStoredProductByProductIdOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.InsertWarehouseDomainEventOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.UpdateStoredProductAmountOutputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.command.ShipProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.entity.StoredProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.event.ProductShipped;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicShipProductInputAdapterTest {

    @Mock
    FindStoredProductByProductIdOutputPort findStoredProductByProductId;

    @Mock
    UpdateStoredProductAmountOutputPort updateStoredProductAmount;

    @Mock
    InsertWarehouseDomainEventOutputPort insertWarehouseDomainEvent;

    @InjectMocks
    BasicShipProductInputAdapter adapter;

    @Test
    void shipProduct_UpdatesStoredProductAmountAndInsertsProductShippedEvent() {
        // given
        var storedProduct = new StoredProduct(StoredProductId.of(UUID.randomUUID()), ProductId.of(UUID.randomUUID()),
                1, 100);
        doReturn(storedProduct).when(this.findStoredProductByProductId).findStoredProductByProductId(any());
        var command = ShipProduct.of(Instant.now(), storedProduct.getProductId(), 57);

        // when
        this.adapter.shipProduct(command);

        // then
        verify(this.findStoredProductByProductId).findStoredProductByProductId(eq(storedProduct.getProductId()));
        verify(this.updateStoredProductAmount)
                .updateStoredProductAmount(eq(storedProduct.getId()), eq(storedProduct.getAmount()), eq(1), eq(2));
        verify(this.insertWarehouseDomainEvent)
                .insertWarehouseDomainEvent(eq(ProductShipped.of(command.getTimestamp(), storedProduct.getVersion(),
                        command.getProductId(), command.getAmount())));
        verifyNoMoreInteractions(this.findStoredProductByProductId, this.updateStoredProductAmount,
                this.insertWarehouseDomainEvent);
    }
}
