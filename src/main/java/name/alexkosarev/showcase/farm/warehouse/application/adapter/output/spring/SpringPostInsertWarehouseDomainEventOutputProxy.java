package name.alexkosarev.showcase.farm.warehouse.application.adapter.output.spring;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.InsertWarehouseDomainEventOutputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.event.WarehouseDomainEvent;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public final class SpringPostInsertWarehouseDomainEventOutputProxy implements InsertWarehouseDomainEventOutputPort {

    @NonNull
    private final InsertWarehouseDomainEventOutputPort delegate;

    @NonNull
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void insertWarehouseDomainEvent(@NonNull WarehouseDomainEvent event) {
        this.delegate.insertWarehouseDomainEvent(event);
        this.applicationEventPublisher.publishEvent(event);
    }
}
