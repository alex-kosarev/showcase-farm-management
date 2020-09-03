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

package name.alexkosarev.showcase.farm.warehouse.application.adapter.output.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_stored_product", schema = "warehouse")
@NamedQueries({
        @NamedQuery(name = "StoredProductJpaEntity.findOneByProductId",
                query = "select sp from StoredProductJpaEntity sp where sp.productId = :productId"),
        @NamedQuery(name = "StoredProductJpaEntity.updateAmount",
                query = "update StoredProductJpaEntity set amount = :amount, version = :nextVersion " +
                        "where id = :id and version = :previousVersion")
})
public class StoredProductJpaEntity {

    @Id
    private UUID id;

    @Version
    @Column(name = "c_version")
    private int version;

    @Column(name = "id_product")
    private UUID productId;

    @Column(name = "c_amount")
    private int amount;
}
