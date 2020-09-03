package name.alexkosarev.showcase.farm.warehouse.application.port.output;

import name.alexkosarev.showcase.farm.warehouse.domain.entity.StoredProduct;

public interface InsertStoredProductOutputPort {

    void insertStoredProduct(StoredProduct storedProduct);
}
