package dao.imp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.Constants;
import dao.CustomerDAO;
import dao.JPAUtil;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import lombok.extern.log4j.Log4j2;
import model.LocalDateAdapter;
import model.LocalDateTimeAdapter;
import model.ObjectIdAdapter;
import model.converters.CustomerConverter;
import model.modelo.Credentials;
import model.modelo.Customer;
import model.errors.CustomerError;
import model.modelHibernate.CustomersEntity;
import model.modelHibernate.OrderItemsEntity;
import model.modelHibernate.OrdersEntity;
import io.vavr.control.Either;
import model.modelo.Order;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class CustomerHibernateImpl implements CustomerDAO {
    private final CustomerConverter customerConverter;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();
    @Inject
    public CustomerHibernateImpl(CustomerConverter customerConverter) {
        this.customerConverter = customerConverter;

    }

//    @Override
   public Either<CustomerError, List<Customer>> add(Customer c) {


           Either<CustomerError, List<Customer>> result;
           try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
               MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
               MongoCollection<Document> customers = db.getCollection("customers");
               MongoCollection<Document> credentials = db.getCollection("credentials");

               // Convert Customer to Document using Gson
               Document customerDocument = Document.parse(gson.toJson(c));
                customerDocument.remove("credentials");
               // Insert the document into the collection
               customers.insertOne(customerDocument);

               ObjectId customerId = (ObjectId) customerDocument.get("_id");

               // Establecer la id generada en la entidad Credential
               c.getCredentials().set_id(customerId);

               // Convert Credential to Document using Gson
               Document credentialDocument = Document.parse(gson.toJson(c.getCredentials()));

               // Insertar el documento del Credential en la colección
               credentials.insertOne(credentialDocument);

               result = Either.right(getAll().get());
           } catch (Exception e) {
               log.error(e.getMessage());
               result = Either.left(new CustomerError(1,"Error while inserting customer"));
           }
           return result;
       }



    @Override
    public Either<CustomerError, Integer> delete(Customer customer, boolean delete) {
        Either<CustomerError, Integer> result;

        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customersCollection = db.getCollection("customers");
            MongoCollection<Document> credentialsCollection = db.getCollection("credentials");

            if (delete) {
                customersCollection.deleteOne(new Document("_id", customer.getId()));
                credentialsCollection.deleteOne(new Document("_id", customer.getId()));
                result = Either.right(1);
            } else {
                result = Either.right(0);
            }


        } catch (Exception e) {
            log.error(e.getMessage());
            result = Either.left(new CustomerError(1, "Error while deleting customer"));
        }
        return result;
    }


    public Either<CustomerError, List<Customer>> update(Customer customer) {
        Either<CustomerError, List<Customer>> result;

        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customersCollection = db.getCollection("customers");
            Document customerDocument = Document.parse(gson.toJson(customer));
            customerDocument.remove("credentials");
            customersCollection.replaceOne(new Document("_id", customer.getId()), customerDocument);
            result = Either.right(getAll().get());
        } catch (Exception e) {
            log.error(e.getMessage());
            result = Either.left(new CustomerError(1, "Error while updating customer"));
        }

        return result;
    }

    public Either<CustomerError, List<Customer>> getAll() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customersCollection = db.getCollection("customers");

            List<Document> customersDocuments = customersCollection.find().into(new ArrayList<>());

            List<Customer> customersList = customersDocuments.stream()
                    .map(customerConverter::fromDocument)
                    .toList();

            if (customersList.isEmpty()) {
                return Either.left(new CustomerError(0, "Error while retrieving customers"));
            } else {
                return Either.right(customersList);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Manejo de la excepción (puedes personalizarlo según tus necesidades)
            return Either.left(new CustomerError(0, "Error while retrieving customers"));
        }
    }





}
