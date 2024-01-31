package dao.imp;

import dao.JPAUtil;
import dao.LoginDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import model.modelo.Credentials;
import model.modelHibernate.CredentialsEntity;
import java.util.List;


public class LoginHibernateImpl implements LoginDAO {

private final JPAUtil jpautil;
private EntityManager em;
    @Inject
    public LoginHibernateImpl(JPAUtil jpautil) {
        this.jpautil = jpautil;
    }


    @Override
    public Either<String, List<Credentials>> getAll() {
        Either<String, List<Credentials>> response;


        try {
            em = jpautil.getEntityManager();
            List<CredentialsEntity> credentialsEntities = em.createQuery("from CredentialsEntity", CredentialsEntity.class).getResultList();

            List<Credentials> credentialsList = credentialsEntities.stream()
                    .map(CredentialsEntity::toCredentials)
                    .toList();

            if (credentialsList.isEmpty()) {
                response = Either.left("Error while retrieving credentials");
            } else {
                response = Either.right(credentialsList);
            }
        } finally {
            if (em != null) em.close();
        }

        return response;
    }



}
