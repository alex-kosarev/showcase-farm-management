package name.alexkosarev.showcase.farm.warehouse.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;

import java.time.temporal.Temporal;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class ProductShipped extends WarehouseDomainEvent {

    private final int version;

    private final ProductId productId;

    private final int amount;

    public static ProductShipped of(@NonNull Temporal timestamp, int version, @NonNull ProductId productId,
                                    int amount) {
        return new ProductShipped(timestamp, version, productId, amount);
    }

    private ProductShipped(Temporal timestamp, int version, ProductId productId, int amount) {
        super(timestamp);
        this.version = version;
        this.productId = productId;
        this.amount = amount;
    }
}
