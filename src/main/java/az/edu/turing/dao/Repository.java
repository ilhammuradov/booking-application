package az.edu.turing.dao;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public interface Repository<T> {
    boolean save(Collection<T> t);

    Collection<T> getAll();

    void delete(long Id);

    Optional<T> findOneBy(Predicate<T> predicate);

    Collection<T> findAllBy(Predicate<T> predicate);
}
