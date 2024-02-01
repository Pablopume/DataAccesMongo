package model.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.modelHibernate.CustomersEntity;
import org.bson.types.ObjectId;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    private ObjectId id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private LocalDate date_of_birth;
    @Transient
    private Credentials credentials;
    private List<Order> orders;


    public Customer(String first_name, String last_name, String email, String phone, LocalDate date_of_birth, Credentials credentials) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.date_of_birth = date_of_birth;
        this.credentials = credentials;

    }


    public Customer(String first_name, String last_name, String email, String phone, LocalDate date_of_birth) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.date_of_birth = date_of_birth;
    }

    public Customer(ObjectId id, String first_name, String last_name, String email, String phone, LocalDate dob) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.date_of_birth = dob;
    }


    public Customer(String first_name, String last_name, String email, String phone, LocalDate date_of_birth, Credentials credentials, List<Order> orders) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.date_of_birth = date_of_birth;
        this.credentials = credentials;
        this.orders = orders;
    }
}
