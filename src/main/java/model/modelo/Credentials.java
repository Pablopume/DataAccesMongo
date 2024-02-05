package model.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.modelHibernate.CredentialsEntity;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class Credentials {
    private ObjectId _id;
    private  String user;
    private  String password;


    public Credentials(String user, String password) {
        this.user = user;
        this.password = password;
    }
}
