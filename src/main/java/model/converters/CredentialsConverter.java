package model.converters;

import model.modelo.Credentials;
import org.bson.Document;

public class CredentialsConverter {
    public Credentials fromDocument(Document document) {
        Credentials credentials = new Credentials();
        credentials.set_id(document.getObjectId("_id"));
        credentials.setUser(document.getString("user"));
        credentials.setPassword(document.getString("password"));
        return credentials;
    }
}
