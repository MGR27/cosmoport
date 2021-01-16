package com.space.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

@Entity

public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String name;

   private String planet;
    @Enumerated(value = EnumType.STRING)
    @Column
   private ShipType shipType;

   private Date prodDate;

   private Boolean isUsed;

   private Double speed;

   private Integer crewSize;

   private Double rating;

    public Ship(String name, String planet, ShipType shipType, Date prodDate, Boolean isUsed, Double speed, Integer crewSize) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
    }

    public Ship(String name, String planet, ShipType shipType, Date prodDate, Double speed, Integer crewSize) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.speed = speed;
        this.crewSize = crewSize;
        this.isUsed = false;
    }

    public Ship() {

    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
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

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void updateRating() {
        Calendar date = Calendar.getInstance();
        date.setTime(getProdDate());
        int year = date.get(Calendar.YEAR);
        Double ratingR = ((80 * getSpeed() * (getUsed() ? 0.5 : 1))) / (3019 - year + 1);
        BigDecimal rating = new BigDecimal(ratingR);
        rating = rating.setScale(2, RoundingMode.HALF_UP);
        setRating(rating.doubleValue());
    }


}
