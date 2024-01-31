package model.modelHibernate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Objects;
@Data
@NoArgsConstructor
@Entity
@Table(name = "restaurant_tables", schema = "pabloserrano_restaurant")
public class RestaurantTablesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "table_number_id", nullable = false)
    private int tableNumberId;
    @Basic
    @Column(name = "number_of_seats", nullable = false)
    private int numberOfSeats;

    @OneToMany(mappedBy = "restaurantTablesByTableId")
    private Collection<OrdersEntity> ordersByTableNumberId;

    public RestaurantTablesEntity(int tableNumberId, int numberOfSeats) {
        this.tableNumberId = tableNumberId;
        this.numberOfSeats = numberOfSeats;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTablesEntity that = (RestaurantTablesEntity) o;
        return tableNumberId == that.tableNumberId && numberOfSeats == that.numberOfSeats;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableNumberId, numberOfSeats);
    }

}
