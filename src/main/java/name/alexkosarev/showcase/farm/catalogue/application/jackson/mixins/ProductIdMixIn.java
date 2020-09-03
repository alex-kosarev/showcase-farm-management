package name.alexkosarev.showcase.farm.catalogue.application.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;

import java.util.UUID;

public abstract class ProductIdMixIn {

    @JsonValue
    public abstract UUID getValue();

    @JsonCreator
    public static ProductId of(String value) {
        return ProductId.of(value);
    }
}
