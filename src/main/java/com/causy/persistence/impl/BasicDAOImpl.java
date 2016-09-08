package com.causy.persistence.impl;

import com.causy.persistence.api.BasicDAO;

import java.util.List;

import static com.causy.persistence.impl.JpaUtils.executeTransactionalJpaOperation;

public class BasicDAOImpl implements BasicDAO {
    @Override
    public Object create(Object newEntity) {
        return executeTransactionalJpaOperation(session -> {
            session.persist(newEntity);
            return newEntity;
        }, "Could not create entity!");
    }

    @Override
    public Object get(Class entityClass, int entityId) {
        return executeTransactionalJpaOperation(em -> em.find(entityClass, entityId),
                String.format("Could not find entity with Id %s", entityId));
    }

    @Override
    public Object update(Object entity) {
        return executeTransactionalJpaOperation(em -> {
            em.merge(entity);
            return entity;
        }, String.format("Something went wrong when trying to update entity %s", entity));
    }

    @Override
    public List list(Class entityClass) {
        return (List) executeTransactionalJpaOperation(em ->
                        em.createQuery("from " + entityClass.getSimpleName()).getResultList(),
                String.format("Could not get list of entity %s", entityClass.getSimpleName()));
    }

    @Override
    public long count(Class entityClass) {
        return (long) executeTransactionalJpaOperation(
                em -> em.createQuery("select count(*) from " + entityClass.getSimpleName()).getSingleResult(),
                "Could not get count of entity");
    }

    @Override
    public void deleteAll(Class entityClass) {
        executeTransactionalJpaOperation(em -> {
            em.createQuery("delete from " + entityClass.getSimpleName()).executeUpdate();
            return null;
        }, "Could not delete all entries of" + entityClass.getSimpleName());
    }
}
