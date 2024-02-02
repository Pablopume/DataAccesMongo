package model.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import model.modelHibernate.CredentialsEntity;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Data
@Builder
public class Credentials {
    private ObjectId _id;
    private final String user;
    private final String password;


    public Credentials(String user, String password) {
        this.user = user;
        this.password = password;
    }
}
