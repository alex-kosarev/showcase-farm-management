package name.alexkosarev.showcase.farm.catalogue.domain.id;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public final class ProductId {

    private final UUID value;

    public static ProductId of(String value) {
        return of(UUID.fromString(value));
    }
}
