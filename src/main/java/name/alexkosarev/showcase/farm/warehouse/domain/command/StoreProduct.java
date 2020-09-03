package name.alexkosarev.showcase.farm.warehouse.domain.command;

import lombok.Value;
import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;

import java.time.temporal.Temporal;

@Value(staticConstructor = "of")
public final class StoreProduct {

    private final Temporal timestamp;

    private final ProductId productId;

    private final int amount;
}
