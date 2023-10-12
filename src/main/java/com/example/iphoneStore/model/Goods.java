package com.example.iphoneStore.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Goods {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private Double price;
    private Integer quantity;
    @ManyToMany(mappedBy = "goods")
    private Set<Order> orders = new HashSet<>();

    public Goods() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;

        return Objects.equals(name, goods.name) &&
                Objects.equals(price, goods.price) &&
                Objects.equals(quantity, goods.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, quantity);
    }
}
