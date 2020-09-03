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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.InsertWarehouseDomainEventOutputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.event.WarehouseDomainEvent;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@RequiredArgsConstructor
public final class JpaInsertWarehouseDomainEventOutputAdapter implements InsertWarehouseDomainEventOutputPort {

    @NonNull
    private final EntityManager entityManager;

    @NonNull
    private final ObjectMapper objectMapper;

    @Override
    public void insertWarehouseDomainEvent(@NonNull WarehouseDomainEvent event) {
        try {
            this.entityManager.createNamedQuery("WarehouseDomainEventJpaEntity.insert")
                    .setParameter("timestamp", Timestamp.from(Instant.from(event.getTimestamp())))
                    .setParameter("payload", this.objectMapper.writeValueAsString(event))
                    .executeUpdate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
