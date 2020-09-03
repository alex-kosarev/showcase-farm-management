package name.alexkosarev.showcase.farm.warehouse.application.adapter.output.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_domain_event", schema = "warehouse")
@NamedNativeQueries({
        @NamedNativeQuery(name = "WarehouseDomainEventJpaEntity.insert",
                query = "insert into warehouse.t_domain_event (c_timestamp, c_payload) " +
                        "values (:timestamp, :payload\\:\\:jsonb)")
})
public class WarehouseDomainEventJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "c_timestamp")
    private Timestamp timestamp;

    @Column(name = "c_payload")
    private String payload;
}
