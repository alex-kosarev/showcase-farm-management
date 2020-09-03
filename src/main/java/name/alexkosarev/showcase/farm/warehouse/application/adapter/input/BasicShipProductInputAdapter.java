package name.alexkosarev.showcase.farm.warehouse.application.adapter.input;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.warehouse.application.port.input.ShipProductInputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.FindStoredProductByProductIdOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.InsertWarehouseDomainEventOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.UpdateStoredProductAmountOutputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.aggregate.Warehouse;
import name.alexkosarev.showcase.farm.warehouse.domain.command.ShipProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;

import java.util.UUID;

@RequiredArgsConstructor
public final class BasicShipProductInputAdapter implements ShipProductInputPort {

    @NonNull
    private final FindStoredProductByProductIdOutputPort findStoredProductByProductId;

    @NonNull
    private final UpdateStoredProductAmountOutputPort updateStoredProductAmount;

    @NonNull
    private final InsertWarehouseDomainEventOutputPort insertWarehouseDomainEvent;

    @Override
    public void shipProduct(@NonNull ShipProduct command) {
        var warehouse = new Warehouse(() -> StoredProductId.of(UUID.randomUUID()));
        var storedProduct = this.findStoredProductByProductId.findStoredProductByProductId(command.getProductId());
        if (storedProduct != null) {
            warehouse.getStoredProducts().add(storedProduct);
        }

        storedProduct = warehouse.shipProduct(command);
        this.updateStoredProductAmount
                .updateStoredProductAmount(storedProduct.getId(), storedProduct.getAmount(),
                        storedProduct.getVersion() - 1, storedProduct.getVersion());

        warehouse.getNewDomainEvents().forEach(this.insertWarehouseDomainEvent::insertWarehouseDomainEvent);
    }
}
