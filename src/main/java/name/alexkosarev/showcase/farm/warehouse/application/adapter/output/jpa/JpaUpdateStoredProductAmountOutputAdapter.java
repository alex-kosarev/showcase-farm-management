/*
 * Copyright 2002-2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

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
