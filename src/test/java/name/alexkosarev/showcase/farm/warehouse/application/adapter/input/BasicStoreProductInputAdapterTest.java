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

package name.alexkosarev.showcase.farm.warehouse.application.adapter.input;

import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.FindStoredProductByProductIdOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.InsertStoredProductOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.InsertWarehouseDomainEventOutputPort;
import name.alexkosarev.showcase.farm.warehouse.application.port.output.UpdateStoredProductAmountOutputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.command.StoreProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.entity.StoredProduct;
import name.alexkosarev.showcase.farm.warehouse.domain.event.ProductStored;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicStoreProductInputAdapterTest {

    @Mock
    FindStoredProductByProductIdOutputPort findStoredProductByProductId;

    @Mock
    InsertStoredProductOutputPort insertStoredProduct;

    @Mock
    UpdateStoredProductAmountOutputPort updateStoredProductAmount;

    @Mock
    InsertWarehouseDomainEventOutputPort insertWarehouseDomainEvent;

    @InjectMocks
    BasicStoreProductInputAdapter adapter;

    @Test
    void storeProduct_StoredProductExists_UpdatesStoredProductAmountAndInsertsProductStoredEvent() {
        // given
        var storedProduct = new StoredProduct(StoredProductId.of(UUID.randomUUID()), ProductId.of(UUID.randomUUID()),
                1, 100);
        doReturn(storedProduct).when(this.findStoredProductByProductId).findStoredProductByProductId(any());
        var command = StoreProduct.of(Instant.now(), storedProduct.getProductId(), 57);

        // when
        this.adapter.storeProduct(command);

        // then
        verify(this.updateStoredProductAmount)
                .updateStoredProductAmount(eq(storedProduct.getId()), eq(storedProduct.getAmount()),
                        eq(storedProduct.getVersion() - 1), eq(storedProduct.getVersion()));
        verify(this.insertWarehouseDomainEvent)
                .insertWarehouseDomainEvent(eq(ProductStored.of(command.getTimestamp(), storedProduct.getVersion(),
                        command.getProductId(), command.getAmount())));
        verifyNoMoreInteractions(this.insertStoredProduct, this.updateStoredProductAmount,
                this.insertWarehouseDomainEvent);
    }

    @Test
    void storeProduct_StoredProductDoesNotExist_CreatesStoredProductAndInsertsProductStoredEvent() {
        // given
        var command = StoreProduct.of(Instant.now(), ProductId.of(UUID.randomUUID()), 57);

        // when
        this.adapter.storeProduct(command);

        // then
        verify(this.insertStoredProduct).insertStoredProduct(argThat(storedProduct -> storedProduct.getId() != null &&
                storedProduct.getVersion() == 1 && storedProduct.getAmount() == command.getAmount() &&
                storedProduct.getProductId().equals(command.getProductId())));
        verify(this.insertWarehouseDomainEvent)
                .insertWarehouseDomainEvent(eq(ProductStored.of(command.getTimestamp(), 1,
                        command.getProductId(), command.getAmount())));
        verifyNoMoreInteractions(this.insertStoredProduct, this.updateStoredProductAmount,
                this.insertWarehouseDomainEvent);
    }
}
