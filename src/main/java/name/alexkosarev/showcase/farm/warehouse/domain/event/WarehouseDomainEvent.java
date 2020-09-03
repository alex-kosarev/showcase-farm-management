package name.alexkosarev.showcase.farm.warehouse.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.temporal.Temporal;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class WarehouseDomainEvent {

    private final Temporal timestamp;
}
