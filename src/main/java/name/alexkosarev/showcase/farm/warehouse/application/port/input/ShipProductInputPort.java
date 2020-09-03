package name.alexkosarev.showcase.farm.warehouse.application.port.input;

import name.alexkosarev.showcase.farm.warehouse.domain.command.ShipProduct;

public interface ShipProductInputPort {

    void shipProduct(ShipProduct command);
}
