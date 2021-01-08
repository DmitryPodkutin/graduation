package com.gmail.podkutin.dmitry.voting_system.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import com.gmail.podkutin.dmitry.voting_system.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r  WHERE r.id=:id")
    @CacheEvict(value = "queryCacheServices", allEntries = true)
    int delete(@Param("id") int id);

    default List<Restaurant> getAll() {
        return findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.menu d WHERE d.date =?1 ORDER BY r.name ASC ")
    @Cacheable("queryCacheServices")
    List<Restaurant> getAllWithMenuDay(LocalDate date);

    @Query("SELECT r FROM Restaurant r  JOIN FETCH r.menu d WHERE r.id=?1 AND d.date =?2")
    @Cacheable("queryCacheServices")
    Optional<Restaurant> getWithMenuDay(int id, LocalDate date);

    @Transactional
    @Override
    @CacheEvict(value = "queryCacheServices", allEntries = true)
    <S extends Restaurant> S save(S entity);
}