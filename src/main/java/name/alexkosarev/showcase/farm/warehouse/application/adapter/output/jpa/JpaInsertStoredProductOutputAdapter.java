package name.alexkosarev.showcase.farm.warehouse.application.adapter.output.jpa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.InsertStoredProductOutputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.entity.StoredProduct;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public final class JpaInsertStoredProductOutputAdapter implements InsertStoredProductOutputPort {

    @NonNull
    private final EntityManager entityManager;

    @Override
    public void insertStoredProduct(@NonNull StoredProduct storedProduct) {
        this.entityManager.persist(new StoredProductJpaEntity(storedProduct.getProductId().getValue(),
                storedProduct.getVersion(), storedProduct.getProductId().getValue(), storedProduct.getAmount()));
    }
}
