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
