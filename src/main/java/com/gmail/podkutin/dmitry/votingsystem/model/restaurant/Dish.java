package com.gmail.podkutin.dmitry.votingsystem.model.restaurant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmail.podkutin.dmitry.votingsystem.model.AbstractNamedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "dish")
public class Dish extends AbstractNamedEntity {

    @Column(name = "date", nullable = false)
    @NotNull(message = "The date field cannot be empty")
    private LocalDate date;

    @Column(name = "price", nullable = false)
    @Range(min = 1, max = 1000000, message = "The price should be in the range from 1 to 1000000")
    private int price;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(String name, LocalDate date, int price) {
        this(null, name, date, price);
    }

    public Dish(Integer id, String name, LocalDate date, int price) {
        super(id, name);
        this.date = date;
        this.price = price;
    }

    public Dish(Integer id, String name, LocalDate date, int price, Restaurant restaurant) {
        super(id, name);
        this.date = date;
        this.price = price;
        this.restaurant = restaurant;
    }
}
