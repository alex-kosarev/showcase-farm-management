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

package name.alexkosarev.showcase.farm;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import name.alexkosarev.showcase.farm.catalogue.application.jackson.mixins.ProductIdMixIn;
import name.alexkosarev.showcase.farm.catalogue.domain.id.ProductId;
import name.alexkosarev.showcase.farm.warehouse.application.adapter.input.BasicStoreProductInputAdapter;
import name.alexkosarev.showcase.farm.warehouse.application.adapter.output.jpa.JpaFindStoredProductByProductIdOutputAdapter;
import name.alexkosarev.showcase.farm.warehouse.application.adapter.output.jpa.JpaInsertStoredProductOutputAdapter;
import name.alexkosarev.showcase.farm.warehouse.application.adapter.output.jpa.JpaInsertWarehouseDomainEventOutputAdapter;
import name.alexkosarev.showcase.farm.warehouse.application.adapter.output.jpa.JpaUpdateStoredProductAmountOutputAdapter;
import name.alexkosarev.showcase.farm.warehouse.application.adapter.output.spring.SpringPostInsertWarehouseDomainEventOutputProxy;
import name.alexkosarev.showcase.farm.warehouse.application.driver.spring.mvc.StoreProductRestController;
import name.alexkosarev.showcase.farm.warehouse.application.jackson.mixins.ProductStoredMixIn;
import name.alexkosarev.showcase.farm.warehouse.application.jackson.mixins.WarehouseDomainEventMixIn;
import name.alexkosarev.showcase.farm.warehouse.domain.event.ProductStored;
import name.alexkosarev.showcase.farm.warehouse.domain.event.WarehouseDomainEvent;
import name.alexkosarev.showcase.farm.warehouse.domain.id.StoredProductId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionOperations;

import javax.persistence.EntityManager;

@SpringBootApplication
public class FarmApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmApplication.class, args);
    }

    @Bean
    public StoreProductRestController storeProductRestController(@NonNull EntityManager entityManager,
                                                                 @NonNull ObjectMapper objectMapper,
                                                                 @NonNull ApplicationEventPublisher
                                                                         applicationEventPublisher,
                                                                 @NonNull TransactionOperations transactionOperations) {
        return new StoreProductRestController(new BasicStoreProductInputAdapter(
                new JpaFindStoredProductByProductIdOutputAdapter(entityManager),
                new JpaInsertStoredProductOutputAdapter(entityManager),
                new JpaUpdateStoredProductAmountOutputAdapter(entityManager),
                new SpringPostInsertWarehouseDomainEventOutputProxy(
                        new JpaInsertWarehouseDomainEventOutputAdapter(entityManager, objectMapper),
                        applicationEventPublisher
                )
        ), transactionOperations);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder.mixIn(ProductId.class, ProductIdMixIn.class)
                .mixIn(StoredProductId.class, StoredProductId.class)
                .mixIn(WarehouseDomainEvent.class, WarehouseDomainEventMixIn.class)
                .mixIn(ProductStored.class, ProductStoredMixIn.class);
    }
}
