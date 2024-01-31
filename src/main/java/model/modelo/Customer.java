package model.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.modelHibernate.CustomersEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private LocalDate date_of_birth;
    @Transient
    private Credentials credentials;



    public Customer(int id, String first_name, String last_name, String email, String phone, LocalDate dob) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.date_of_birth = dob;
    }

    public Customer(String fileLine) {
        String[] elemArray = fileLine.split(";");
        this.id = Integer.parseInt(elemArray[0]);
        this.first_name = elemArray[1];
        this.last_name = elemArray[2];
        this.email = elemArray[3];
        this.phone = elemArray[4];
        this.date_of_birth = LocalDate.parse(elemArray[5]);

    }

    public CustomersEntity toCustomerEntity() {

        return new CustomersEntity(id, first_name, last_name, email, phone, Date.valueOf(date_of_birth.toString()));
    }
    public String toStringTextFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return id + ";" + first_name + ";" + last_name + ";" + email + ";" + phone + ";" + date_of_birth.format(formatter);
    }


}
