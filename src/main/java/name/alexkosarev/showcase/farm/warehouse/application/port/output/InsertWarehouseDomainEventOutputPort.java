package name.alexkosarev.showcase.farm.warehouse.application.port.output;

import name.alexkosarev.showcase.farm.warehouse.domain.event.WarehouseDomainEvent;

public interface InsertWarehouseDomainEventOutputPort {

    void insertWarehouseDomainEvent(WarehouseDomainEvent event);
}
