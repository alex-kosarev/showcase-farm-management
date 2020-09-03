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

package name.alexkosarev.showcase.farm.warehouse.application.driver.spring.mvc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.alexkosarev.showcase.farm.warehouse.application.port.input.StoreProductInputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.command.StoreProduct;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

@Slf4j
@RequestMapping("/warehouse/api/stored-products")
@RequiredArgsConstructor
public class StoreProductRestController {

    @NonNull
    private final StoreProductInputPort storeProduct;

    @NonNull
    private final TransactionOperations transactionOperations;

    @PostMapping
    public ResponseEntity<?> handleStoreProduct(@NonNull @RequestBody StoreProductRequest request) {
        ResponseEntity<?> responseEntity;
        try {
            this.transactionOperations.executeWithoutResult(status -> this.storeProduct
                    .storeProduct(StoreProduct.of(Instant.now(), request.getProductId(), request.getAmount())));
            responseEntity = ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            responseEntity = ResponseEntity.badRequest().build();
            log.error(e.getMessage(), e);
        }

        return responseEntity;
    }
}
