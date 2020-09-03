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
