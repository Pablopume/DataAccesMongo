package dao.imp;

import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Named;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.log4j.Log4j2;
import model.modelo.Order;
import model.errors.OrderError;
import model.xml.OrderItemXML;
import model.xml.OrderXML;
import model.xml.OrdersXML;
import org.bson.types.ObjectId;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Log4j2
@Named("OrderXMLImpl")
public class OrderXMLImpl implements OrdersDAO {
    @Override
    public Either<OrderError, List<Order>> getAll() {
        return null;
    }

    @Override
    public Either<OrderError, List<Order>> getAll(ObjectId id) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> delete(Order order) {
        return null;
    }

    @Override
    public Either<OrderError, Order> add(Order order) {
        return null;
    }

    @Override
    public Either<OrderError, Integer> update(Order c) {
        return null;
    }


}
