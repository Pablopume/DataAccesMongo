package model.modelHibernate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.modelo.Credentials;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credentials", schema = "pabloserrano_restaurant")
public class CredentialsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;
    @Basic
    @Column(name = "password", nullable = false, length = 20)
    private String password;



    public Credentials toCredentials() {
        return new Credentials(id, userName, password);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CredentialsEntity that = (CredentialsEntity) o;
        return id == that.id && Objects.equals(userName, that.userName) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, password);
    }

}
