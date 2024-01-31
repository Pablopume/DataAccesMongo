package dao.imp;

import common.Constants;
import dao.JPAUtil;
import dao.MenuItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.modelo.MenuItem;
import model.errors.OrderError;
import model.modelHibernate.MenuItemsEntity;

import java.util.List;

public class MenuItemHibernateImpl implements MenuItemDAO {



    private final JPAUtil jpautil;
    private EntityManager em;
    @Inject
    public MenuItemHibernateImpl(JPAUtil jpautil) {
        this.jpautil = jpautil;
    }


    @Override
    public Either<OrderError, List<MenuItem>> getAll() {
        Either<OrderError, List<MenuItem>> result;


        try {
            em = jpautil.getEntityManager();
            List<MenuItemsEntity> menuItemsEntities = em.createQuery("from MenuItemsEntity", MenuItemsEntity.class).getResultList();

            List<MenuItem> menuItems = menuItemsEntities.stream()
                    .map(MenuItemsEntity::toMenuItem)
                    .toList();

            if (menuItems.isEmpty()) {
                result = Either.left(new OrderError(Constants.THERE_ARE_NO_MENU_ITEMS));
            } else {
                result = Either.right(menuItems);
            }
        } finally {
            if (em != null) em.close();
        }

        return result;
    }



}
