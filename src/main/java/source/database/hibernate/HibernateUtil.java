package source.database.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;
import source.exception.AccStorageException;

@Component
public class HibernateUtil {
    private SessionFactory sessionFactory;
    public HibernateUtil() throws AccStorageException {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new AccStorageException(e.getMessage(), e);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
