package services.impl;

import dao.AggregationsDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.errors.OrderError;

public class AggregationsServices {
    private final AggregationsDAO aggregationsDAO;

    @Inject
    public AggregationsServices(AggregationsDAO aggregationsDAO) {
        this.aggregationsDAO = aggregationsDAO;
    }

    public Either<OrderError, String> a() {
        return aggregationsDAO.a();
    }

    public Either<OrderError, String> b() {
        return aggregationsDAO.b();
    }

    public Either<OrderError, String> c() {
        return aggregationsDAO.c();
    }

    public Either<OrderError, String> d() {
        return aggregationsDAO.d();
    }

    public Either<OrderError, String> e() {
        return aggregationsDAO.e();
    }

    public Either<OrderError, String> f() {
        return aggregationsDAO.f();
    }

    public Either<OrderError, String> g() {
        return aggregationsDAO.g();
    }

    public Either<OrderError, String> h() {
        return aggregationsDAO.h();
    }

    public Either<OrderError, String> i() {
        return aggregationsDAO.i();
    }

    public Either<OrderError, String> j() {
        return aggregationsDAO.j();
    }

    public Either<OrderError, String> k() {
        return aggregationsDAO.k();
    }

    public Either<OrderError, String> l() {
        return aggregationsDAO.l();
    }

    public Either<OrderError, String> m() {
        return aggregationsDAO.m();
    }

    public Either<OrderError, String> ex2a() {
        return aggregationsDAO.ex2a();
    }

    public Either<OrderError, String> ex2b() {
        return aggregationsDAO.ex2b();
    }

    public Either<OrderError, String> ex2c() {
        return aggregationsDAO.ex2c();
    }

    public Either<OrderError, String> ex2d() {
        return aggregationsDAO.ex2d();
    }
    public Either<OrderError, String> ex2e() {
        return aggregationsDAO.ex2e();
    }

}
