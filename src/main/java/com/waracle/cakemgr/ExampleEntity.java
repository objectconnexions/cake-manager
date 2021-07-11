package com.waracle.cakemgr;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

@Entity()
//@org.hibernate.annotations.(dynamicUpdate = true)
@Table(name = "EXAMPLE", uniqueConstraints = {@UniqueConstraint(columnNames = "ID")})
public class ExampleEntity implements Serializable {

    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @Column(name = "NAME", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "DATE", unique = false, nullable = false, length = 100)
    private LocalDate date;

    @Column(name = "CAKE", unique = false, nullable = false)
    private CakeEntity cake;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CakeEntity getCake() {
        return cake;
    }

    public void setCake(CakeEntity cake) {
        this.cake = cake;
    }

    @Override
    public String toString() {
    	return name;
    }
}