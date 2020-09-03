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

package name.alexkosarev.showcase.farm.warehouse.domain.aggregate;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import name.alexkosarev.showcase.farm.warehouse.domain.command.ShipProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.command.StoreProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.entity.StoredProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.event.ProductShipped;
import name.alexkosarev.showcase.farm.warehouse.domain.event.ProductStored;
import name.alexkosarev.showcase.farm.warehouse.domain.event.WarehouseDomainEvent;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class Warehouse {

    @Getter
    private final transient List<WarehouseDomainEvent> newDomainEvents = new ArrayList<>();

    private final transient StoredProductId.Factory storedProductIdFactory;

    @NonNull
    @Getter
    private final List<StoredProduct> storedProducts = new ArrayList<>();

    public StoredProduct storeProduct(@NonNull StoreProduct command) {
        if (command.getAmount() < 1) {
            throw new IllegalArgumentException();
        }

        var storedProduct = this.storedProducts.stream()
                .filter(existingStoredProduct -> existingStoredProduct.getProductId().equals(command.getProductId()))
                .findFirst()
                .orElseGet(() -> {
                    var newStoredProduct = new StoredProduct(this.storedProductIdFactory.get(), command.getProductId());
                    this.storedProducts.add(newStoredProduct);

                    return newStoredProduct;
                });

        storedProduct.store(command);
        this.newDomainEvents.add(ProductStored.of(command.getTimestamp(), storedProduct.getVersion(),
                command.getProductId(), command.getAmount()));

        return storedProduct;
    }

    public StoredProduct shipProduct(@NonNull ShipProduct command) {
        if (command.getAmount() < 1) {
            throw new IllegalArgumentException();
        }

        var storedProduct = this.storedProducts.stream()
                .filter(existingStoredProduct -> existingStoredProduct.getProductId().equals(command.getProductId()))
                .findFirst().orElseThrow(IllegalStateException::new);

        if (storedProduct.getAmount() < command.getAmount()) {
            throw new IllegalStateException();
        }

        storedProduct.ship(command);
        this.newDomainEvents.add(ProductShipped.of(command.getTimestamp(), storedProduct.getVersion(),
                command.getProductId(), command.getAmount()));

        return storedProduct;
    }
}
