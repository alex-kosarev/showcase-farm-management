package name.alexkosarev.showcase.farm.warehouse.application.adapter.output.jpa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.FindStoredProductByProductIdOutputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.entity.StoredProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public final class JpaFindStoredProductByProductIdOutputAdapter implements FindStoredProductByProductIdOutputPort {

    @NonNull
    private final EntityManager entityManager;

    @Override
    public StoredProduct findStoredProductByProductId(@NonNull ProductId productId) {
        var entities = this.entityManager
                .createNamedQuery("StoredProductJpaEntity.findOneByProductId", StoredProductJpaEntity.class)
                .setParameter("productId", productId.getValue())
                .getResultList();

        if (entities.isEmpty()) {
            return null;
        }

        var entity = entities.get(0);
        return new StoredProduct(StoredProductId.of(entity.getId()),
                ProductId.of(entity.getProductId()), entity.getVersion(),
                entity.getAmount());
    }
}
