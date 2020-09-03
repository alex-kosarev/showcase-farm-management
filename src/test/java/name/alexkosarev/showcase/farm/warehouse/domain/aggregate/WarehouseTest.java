package name.alexkosarev.showcase.farm.warehouse.domain.aggregate;

import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;
import name.alexkosarev.showcase.farm.warehouse.domain.command.ShipProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.command.StoreProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.entity.StoredProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.event.ProductShipped;
import name.alexkosarev.showcase.farm.warehouse.domain.event.ProductStored;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Warehouse (unit tests)")
class WarehouseTest {

    @Test
    @DisplayName("can store the product if already contains it")
    void storeProduct_WarehouseContainsProduct_UpdatesAmountOfStoredProductAndCreatesDomainEvent() {
        // given
        var warehouse = new Warehouse(() -> StoredProductId.of(UUID.randomUUID()));
        var command = StoreProduct.of(Instant.now(), ProductId.of(UUID.randomUUID()), 100);
        warehouse.getStoredProducts().add(new StoredProduct(StoredProductId.of(UUID.randomUUID()),
                command.getProductId(), 1, 127));

        // when
        var storedProduct = warehouse.storeProduct(command);

        // then
        assertEquals(command.getProductId(), storedProduct.getProductId());
        assertEquals(227, storedProduct.getAmount());
        assertEquals(2, storedProduct.getVersion());
        assertEquals(List.of(ProductStored.of(command.getTimestamp(), storedProduct.getVersion(),
                command.getProductId(), command.getAmount())), warehouse.getNewDomainEvents());
    }

    @Test
    @DisplayName("can store the product if doesn't contain it")
    void storeProduct_WarehouseDoesNotContainProduct_CreatesStoredProductAndDomainEvent() {
        // given
        var warehouse = new Warehouse(() -> StoredProductId.of(UUID.randomUUID()));
        var command = StoreProduct.of(Instant.now(), ProductId.of(UUID.randomUUID()), 100);

        // when
        var storedProduct = warehouse.storeProduct(command);

        // then
        assertEquals(command.getProductId(), storedProduct.getProductId());
        assertEquals(100, storedProduct.getAmount());
        assertEquals(1, storedProduct.getVersion());
        assertEquals(List.of(ProductStored.of(command.getTimestamp(), storedProduct.getVersion(),
                command.getProductId(), command.getAmount())), warehouse.getNewDomainEvents());
    }

    @Test
    @DisplayName("cannot store negative amount of the product")
    void storeProduct_AttemptToStoreNegativeAmountOfProduct_ThrowsIllegalArgumentException() {
        // given
        var warehouse = new Warehouse(() -> StoredProductId.of(UUID.randomUUID()));

        // when
        assertThrows(IllegalArgumentException.class, () -> warehouse
                .storeProduct(StoreProduct.of(Instant.now(), ProductId.of(UUID.randomUUID()), -100)));
    }

    @Test
    @DisplayName("can ship the product if contains enough amount of it")
    void shipProduct_WarehouseContainsEnoughAmountOfProduct_UpdatesAmountOfStoredProductAndCreatesDomainEvent() {
        // given
        var warehouse = new Warehouse(() -> StoredProductId.of(UUID.randomUUID()));
        var command = ShipProduct.of(Instant.now(), ProductId.of(UUID.randomUUID()), 100);
        warehouse.getStoredProducts().add(new StoredProduct(StoredProductId.of(UUID.randomUUID()),
                command.getProductId(), 1, 127));

        // when
        var storedProduct = warehouse.shipProduct(command);

        // then
        assertEquals(command.getProductId(), storedProduct.getProductId());
        assertEquals(27, storedProduct.getAmount());
        assertEquals(2, storedProduct.getVersion());
        assertEquals(List.of(ProductShipped.of(command.getTimestamp(), storedProduct.getVersion(),
                command.getProductId(), command.getAmount())), warehouse.getNewDomainEvents());
    }

    @Test
    @DisplayName("cannot ship negative amount of the product")
    void shipProduct_AttemptToShipNegativeAmountOfProduct_ThrowsIllegalArgumentException() {
        // given
        var warehouse = new Warehouse(() -> StoredProductId.of(UUID.randomUUID()));
        var command = ShipProduct.of(Instant.now(), ProductId.of(UUID.randomUUID()), -100);
        warehouse.getStoredProducts().add(new StoredProduct(StoredProductId.of(UUID.randomUUID()),
                command.getProductId(), 1, 127));

        // when
        assertThrows(IllegalArgumentException.class, () -> warehouse.shipProduct(command));
    }

    @Test
    @DisplayName("cannot ship the product if does not contain enough amount of it")
    void shipProduct_WarehouseDoesNotContainEnoughAmountOfProduct_ThrowsIllegalStateException() {
        // given
        var warehouse = new Warehouse(() -> StoredProductId.of(UUID.randomUUID()));
        var command = ShipProduct.of(Instant.now(), ProductId.of(UUID.randomUUID()), 100);
        warehouse.getStoredProducts().add(new StoredProduct(StoredProductId.of(UUID.randomUUID()),
                command.getProductId(), 2, 27));

        // when
        assertThrows(IllegalStateException.class, () -> warehouse.shipProduct(command));
    }

    @Test
    @DisplayName("cannot ship the product if does not contain product at all")
    void shipProduct_WarehouseDoesNotContainProduct_ThrowsIllegalStateException() {
        // given
        var warehouse = new Warehouse(() -> StoredProductId.of(UUID.randomUUID()));
        var command = ShipProduct.of(Instant.now(), ProductId.of(UUID.randomUUID()), 100);

        // when
        assertThrows(IllegalStateException.class, () -> warehouse.shipProduct(command));
    }
}
