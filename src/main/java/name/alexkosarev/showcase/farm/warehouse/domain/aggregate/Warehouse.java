package name.alexkosarev.showcase.farm.warehouse.domain.aggregate;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.warehouse.domain.command.ShipProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.command.StoreProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.entity.StoredProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.event.ProductShipped;
import name.alexkosarev.showcase.farm.warehouse.domain.event.ProductStored;
import name.alexkosarev.showcase.farm.warehouse.domain.event.WarehouseDomainEvent;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class Warehouse {

    @Getter
    private final transient List<WarehouseDomainEvent> newDomainEvents = new ArrayList<>();

    private final transient StoredProductId.Factory storedProductIdFactory;

    @NonNull
    @Getter
    private final List<StoredProduct> storedProducts = new ArrayList<>();

    public StoredProduct storeProduct(@NonNull StoreProduct command) {
        if (command.getAmount() < 1) {
            throw new IllegalArgumentException();
        }

        var storedProduct = this.storedProducts.stream()
                .filter(existingStoredProduct -> existingStoredProduct.getProductId().equals(command.getProductId()))
                .findFirst()
                .orElseGet(() -> {
                    var newStoredProduct = new StoredProduct(this.storedProductIdFactory.get(), command.getProductId());
                    this.storedProducts.add(newStoredProduct);

                    return newStoredProduct;
                });

        storedProduct.store(command);
        this.newDomainEvents.add(ProductStored.of(command.getTimestamp(), storedProduct.getVersion(),
                command.getProductId(), command.getAmount()));

        return storedProduct;
    }

    public StoredProduct shipProduct(@NonNull ShipProduct command) {
        if (command.getAmount() < 1) {
            throw new IllegalArgumentException();
        }

        var storedProduct = this.storedProducts.stream()
                .filter(existingStoredProduct -> existingStoredProduct.getProductId().equals(command.getProductId()))
                .findFirst().orElseThrow(IllegalStateException::new);

        if (storedProduct.getAmount() < command.getAmount()) {
            throw new IllegalStateException();
        }

        storedProduct.ship(command);
        this.newDomainEvents.add(ProductShipped.of(command.getTimestamp(), storedProduct.getVersion(),
                command.getProductId(), command.getAmount()));

        return storedProduct;
    }
}
