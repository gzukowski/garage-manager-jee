package garagemanager.carparts.repository.persistence;

import garagemanager.carparts.entity.Car;
import garagemanager.carparts.entity.Part;
import garagemanager.carparts.repository.api.PartRepository;
import garagemanager.user.entity.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Part entity. Repositories should be used in business layer (e.g.: in services). The request scope
 * is a result of the fact that {@link EntityManager} objects cannot be used in multiple threads (are not thread safe).
 * Because services are CDI application scoped beans (technically singletons) then repositories must be thread scoped in
 * order to ensure single entity manager for single thread.
 */
@RequestScoped
public class PartPersistenceRepository implements PartRepository {

    /**
     * Connection with the database (not thread safe).
     */
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Part> find(UUID id) {
        return Optional.ofNullable(em.find(Part.class, id));
    }

    @Override
    public List<Part> findAll() {
        return em.createQuery("select p from Part p", Part.class).getResultList();
    }

    @Override
    public void create(Part entity) {
        em.persist(entity);
    }

    @Override
    public void delete(Part entity) {
        em.remove(em.find(Part.class, entity.getId()));
    }

    @Override
    public void update(Part entity) {
        em.merge(entity);
    }

    @Override
    public Optional<Part> findByIdAndUser(UUID id, User user) {
        try {
            return Optional.of(em.createQuery("select p from Part p where p.id = :id and p.user = :user", Part.class)
                    .setParameter("user", user)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Part> findAllByUser(User user) {
        return em.createQuery("select p from Part p where p.user = :user", Part.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Part> findAllByCar(Car car) {
        return em.createQuery("select p from Part p where p.car = :car", Part.class)
                .setParameter("car", car)
                .getResultList();
    }

}
