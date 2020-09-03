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

package name.alexkosarev.showcase.farm.warehouse.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;

import java.time.temporal.Temporal;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class ProductStored extends WarehouseDomainEvent {

    private final int version;

    private final ProductId productId;

    private final int amount;

    public static ProductStored of(@NonNull Temporal timestamp, int version, @NonNull ProductId productId,
                                        int amount) {
        return new ProductStored(timestamp, version, productId, amount);
    }

    private ProductStored(Temporal timestamp, int version, ProductId productId, int amount) {
        super(timestamp);
        this.version = version;
        this.productId = productId;
        this.amount = amount;
    }
}
