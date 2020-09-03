package name.alexkosarev.showcase.farm.warehouse.application.adapter.output.jpa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.UpdateStoredProductAmountOutputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public final class JpaUpdateStoredProductAmountOutputAdapter implements UpdateStoredProductAmountOutputPort {

    @NonNull
    private final EntityManager entityManager;

    @Override
    public int updateStoredProductAmount(@NonNull StoredProductId storedProductId, int amount, int previousVersion,
                                         int nextVersion) {
        return this.entityManager.createNamedQuery("StoredProductJpaEntity.updateAmount")
                .setParameter("amount", amount)
                .setParameter("nextVersion", nextVersion)
                .setParameter("id", storedProductId.getValue())
                .setParameter("previousVersion", previousVersion)
                .executeUpdate();
    }
}
