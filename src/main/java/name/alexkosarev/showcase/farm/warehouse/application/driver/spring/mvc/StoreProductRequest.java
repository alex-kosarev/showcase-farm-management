package name.alexkosarev.showcase.farm.warehouse.application.driver.spring.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class StoreProductRequest {

    private ProductId productId;

    private int amount;
}
