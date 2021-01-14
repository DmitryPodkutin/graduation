package com.gmail.podkutin.dmitry.votingsystem.repository;

import com.gmail.podkutin.dmitry.votingsystem.model.restaurant.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Transactional
    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.date=:date AND v.user.id=:userId")
    Optional<Vote> getForeDate(@Param("date") LocalDate date, @Param("userId") int userId);
}
