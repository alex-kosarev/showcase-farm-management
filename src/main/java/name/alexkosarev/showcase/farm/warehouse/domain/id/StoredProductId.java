package name.alexkosarev.showcase.farm.warehouse.domain.id;

import lombok.Value;

import java.util.UUID;
import java.util.function.Supplier;

@Value(staticConstructor = "of")
public final class StoredProductId {

    private final UUID value;

    public static StoredProductId of(String value) {
        return of(UUID.fromString(value));
    }

    public interface Factory extends Supplier<StoredProductId> {}
}
