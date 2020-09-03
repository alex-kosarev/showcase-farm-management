package name.alexkosarev.showcase.farm.warehouse.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;
import name.alexkosarev.showcase.farm.warehouse.domain.command.ShipProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.command.StoreProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public final class StoredProduct {

    @NonNull
    private final StoredProductId id;

    @NonNull
    private final ProductId productId;

    private int version;

    private int amount;

    public void store(@NonNull StoreProduct command) {
        this.amount += command.getAmount();
        this.version++;
    }

    public void ship(@NonNull ShipProduct command) {
        this.amount -= command.getAmount();
        this.version++;
    }
}
