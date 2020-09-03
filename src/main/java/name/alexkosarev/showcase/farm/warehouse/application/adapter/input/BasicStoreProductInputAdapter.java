package name.alexkosarev.showcase.farm.warehouse.application.adapter.input;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.warehouse.application.port.input.StoreProductInputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.FindStoredProductByProductIdOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.InsertStoredProductOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.InsertWarehouseDomainEventOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.UpdateStoredProductAmountOutputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.aggregate.Warehouse;
import name.alexkosarev.showcase.farm.warehouse.domain.command.StoreProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;

import java.util.UUID;

@RequiredArgsConstructor
public final class BasicStoreProductInputAdapter implements StoreProductInputPort {

    @NonNull
    private final FindStoredProductByProductIdOutputPort findStoredProductByProductId;

    @NonNull
    private final InsertStoredProductOutputPort insertStoredProduct;

    @NonNull
    private final UpdateStoredProductAmountOutputPort updateStoredProductAmount;

    @NonNull
    private final InsertWarehouseDomainEventOutputPort insertWarehouseDomainEvent;

    @Override
    public void storeProduct(@NonNull StoreProduct command) {
        var warehouse = new Warehouse(() -> StoredProductId.of(UUID.randomUUID()));
        var storedProduct = this.findStoredProductByProductId.findStoredProductByProductId(command.getProductId());
        if (storedProduct != null) {
            warehouse.getStoredProducts().add(storedProduct);
        }

        storedProduct = warehouse.storeProduct(command);
        if (storedProduct.getVersion() == 1) {
            this.insertStoredProduct.insertStoredProduct(storedProduct);
        } else {
            this.updateStoredProductAmount
                    .updateStoredProductAmount(storedProduct.getId(), storedProduct.getAmount(),
                            storedProduct.getVersion() - 1, storedProduct.getVersion());
        }

        warehouse.getNewDomainEvents().forEach(this.insertWarehouseDomainEvent::insertWarehouseDomainEvent);
    }
}
