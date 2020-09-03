package name.alexkosarev.showcase.farm.warehouse.application.port.output;

import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;
import name.alexkosarev.showcase.farm.warehouse.domain.entity.StoredProduct;

public interface FindStoredProductByProductIdOutputPort {

    StoredProduct findStoredProductByProductId(ProductId productId);
}
