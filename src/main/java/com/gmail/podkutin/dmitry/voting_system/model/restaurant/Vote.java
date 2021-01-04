package com.gmail.podkutin.dmitry.voting_system.model.restaurant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmail.podkutin.dmitry.voting_system.model.AbstractBaseEntity;
import lombok.Getter;
import lombok.Setter;
import com.gmail.podkutin.dmitry.voting_system.model.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "vote")
public class Vote extends AbstractBaseEntity {

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    public Vote() {
    }

    public Vote(@NotNull User user, @NotNull Restaurant restaurant) {
        this(null, user, restaurant,LocalDate.now());
    }

    public Vote(Integer id, @NotNull User user, @NotNull Restaurant restaurant) {
        this(id, user, restaurant, LocalDate.now());
    }

    public Vote(Integer id, @NotNull User user, @NotNull Restaurant restaurant, LocalDate date) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", date=" + date +
                ", restaurant=" + restaurant +
                '}';
    }
}