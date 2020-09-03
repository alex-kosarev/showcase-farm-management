package name.alexkosarev.showcase.farm.warehouse.application.port.output;

import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;

public interface UpdateStoredProductAmountOutputPort {

    int updateStoredProductAmount(StoredProductId storedProductId, int amount, int previousVersion, int nextVersion);
}
