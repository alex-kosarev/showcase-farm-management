package name.alexkosarev.showcase.farm.warehouse.application.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;

import java.util.UUID;

public abstract class StoredProductIdMixIn {

    @JsonValue
    public abstract UUID getValue();

    @JsonCreator
    public static StoredProductId of(String value) {
        return StoredProductId.of(value);
    }
}
