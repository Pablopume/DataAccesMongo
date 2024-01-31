package services.impl;

import dao.HibernateDao;
import jakarta.inject.Inject;

public class HibernateServicesImpl {
    private final HibernateDao hibernateDao;
    @Inject
    public HibernateServicesImpl(HibernateDao hibernateDao) {
        this.hibernateDao = hibernateDao;
    }
    public void fromSqlToMongo() {
        hibernateDao.fromSqlToMongo();
    }
}
