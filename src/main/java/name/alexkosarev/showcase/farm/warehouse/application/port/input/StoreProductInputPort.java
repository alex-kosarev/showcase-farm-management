package name.alexkosarev.showcase.farm.warehouse.application.port.input;

import name.alexkosarev.showcase.farm.warehouse.domain.command.StoreProduct;

public interface StoreProductInputPort {

    void storeProduct(StoreProduct storeProduct);
}
