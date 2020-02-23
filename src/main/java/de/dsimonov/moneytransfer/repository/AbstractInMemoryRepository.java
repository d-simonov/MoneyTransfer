package de.dsimonov.moneytransfer.repository;

import de.dsimonov.moneytransfer.model.AbstractEntity;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractInMemoryRepository<T extends AbstractEntity> {
    private Map<Long, T> store = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    public T getOne(Long id) {
        return store.get(id);
    }

    public Collection<T> getAll() {
        return store.values();
    }

    public T create(T entity) {
        long newId = idGenerator.getAndIncrement();
        entity.setId(newId);
        store.putIfAbsent(newId, entity);
        return entity;
    }

    public void delete(Long id) {
        store.remove(id);
    }
}
