package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exceptions.BadRequestException;
import com.space.exceptions.ShipNotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShipServiceImpl implements ShipService{

    @Autowired
    ShipRepository shipRepository;

//    @Autowired
//    public ShipServiceImpl(ShipRepository shipRepository) {
//        this.shipRepository = shipRepository;
//    }

    @Override
    public List<Ship> findAll(Pageable pageable) {
        return shipRepository.findAll(pageable).stream().collect(Collectors.toList());
    }

    @Override
    public Ship create(Map<String,String> params){
        Boolean isUsed;
        if (!params.containsKey("isUsed")){
            isUsed = false;
        }
        String name = params.get("name");
        String planet = params.get("planet");
        ShipType shipType = ShipType.valueOf(params.get("shipType"));
        Date prodDate = new Date(Long.parseLong(params.get("prodDate")));
        Double speed = Double.parseDouble(params.get("speed"));
        isUsed = Boolean.valueOf(params.get("isUsed"));
        Integer crewSize = Integer.parseInt(params.get("crewSize"));
        Ship ship = new Ship(name,planet,shipType,prodDate,isUsed,speed,crewSize);
        ship.updateRating();
        shipRepository.save(ship);
        return ship;
    }
    @Override
    public Ship updateById(Long id, Ship shipRequired) {
        paramsChecker(shipRequired);

        boolean isExist = shipRepository.existsById(id);
        if (!isExist){
            return null;
        }
            Ship changedShip = shipRepository.findById(id).get();
        if (id != 0 && changedShip != null) {
            if (shipRequired.getName() != null)
                changedShip.setName(shipRequired.getName());

            if (shipRequired.getPlanet() != null)
                changedShip.setPlanet(shipRequired.getPlanet());

            if (shipRequired.getShipType() != null)
                changedShip.setShipType(shipRequired.getShipType());

            if (shipRequired.getProdDate() != null)
                changedShip.setProdDate(shipRequired.getProdDate());

            if (shipRequired.getSpeed() != null)
                changedShip.setSpeed(shipRequired.getSpeed());

            if (shipRequired.getUsed() != null)
                changedShip.setUsed(shipRequired.getUsed());

            if (shipRequired.getCrewSize() != null)
                changedShip.setCrewSize(shipRequired.getCrewSize());
            changedShip.updateRating();
        }
        shipRepository.save(changedShip);
        return changedShip;
    }
    private void paramsChecker(Ship shipRequired) {

        if (shipRequired.getName() != null && (shipRequired.getName().length() < 1 || shipRequired.getName().length() > 50)) {
            throw new BadRequestException("The ship name is too long or absent");
        }

        if (shipRequired.getPlanet() != null && shipRequired.getPlanet().length() > 50) {
            throw new BadRequestException("The planet name is too long or absent");
        }

        if (shipRequired.getSpeed() != null && (shipRequired.getSpeed() < 0.01D || shipRequired.getSpeed() > 9999.99D)) {
            throw new BadRequestException("The ship speed is out of range");
        }

        if (shipRequired.getCrewSize() != null && (shipRequired.getCrewSize() < 1 || shipRequired.getCrewSize() > 9999)) {
            throw new BadRequestException("The crew size is out of range");
        }

        if (shipRequired.getProdDate() != null) {
            Calendar date = Calendar.getInstance();
            date.setTime(shipRequired.getProdDate());
            if (date.get(Calendar.YEAR) < 2800 || date.get(Calendar.YEAR) > 3019) {
                throw new BadRequestException("The date of ship manufacture is out of range");
            }
        }
    }

    @Override
    public Long idChecker(String id) {
        if (id == null || id.equals("0") || id.equals("")) {
            throw new BadRequestException("ID is incorrect");
        }
        try {
            Long iD = Long.parseLong(id);
            return iD;
        } catch (NumberFormatException e) {
            throw new BadRequestException("ID is not a number", e);
        }
    }

    @Override
    public String delete(Long id) {
        boolean isShipExists = shipRepository.existsById(id);
        if (isShipExists) {
            shipRepository.deleteById(id);
            return "200";
        } else {
            return "404";
        }
    }

    @Override
    public Ship getShipById(Long id) {

        return shipRepository.findById(id).get();
    }

    @Override
    public List<Ship> getByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String planet = (String) params.getOrDefault("planet", null);
        ShipType shipType = params.containsKey("shipType") ? ShipType.valueOf((String) params.get("shipType")) : null;
        Calendar calendar = Calendar.getInstance();
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean isUsed = params.containsKey("isUsed") ? Boolean.parseBoolean(params.get("isUsed")) : null;
        Double minSpeed = params.containsKey("minSpeed") ? Double.parseDouble(params.get("minSpeed")) : null;
        Double maxSpeed = params.containsKey("maxSpeed") ? Double.parseDouble(params.get("maxSpeed")) : null;
        Integer minCrewSize = params.containsKey("mincrewSize") ? Integer.parseInt(params.get("minCrewSize")) : null;
        Integer maxCrewSize = params.containsKey("maxCrewSize") ? Integer.parseInt(params.get("maxCrewSize")) : null;
        Double minRating = params.containsKey("minRating") ? Double.parseDouble(params.get("minRating")) : null;
        Double maxRating = params.containsKey("maxRating") ? Double.parseDouble(params.get("maxRating")) : null;
        Pageable pageable;
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));
        if (params.containsKey("order")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, (ShipOrder.valueOf(params.get("order"))).getFieldName());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        return shipRepository.findAllByParams(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, pageable).stream().collect(Collectors.toList());
    }

    @Override
    public Integer countByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String planet = (String) params.getOrDefault("planet", null);
        ShipType shipType = params.containsKey("shipType") ? ShipType.valueOf((String) params.get("shipType")) : null;
        Calendar calendar = Calendar.getInstance();
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean isUsed = params.containsKey("isUsed") ? Boolean.parseBoolean(params.get("isUsed")) : null;
        Double minSpeed = params.containsKey("minSpeed") ? Double.parseDouble(params.get("minSpeed")) : null;
        Double maxSpeed = params.containsKey("maxSpeed") ? Double.parseDouble(params.get("maxSpeed")) : null;
        Integer minCrewSize = params.containsKey("mincrewSize") ? Integer.parseInt(params.get("minCrewSize")) : null;
        Integer maxCrewSize = params.containsKey("maxCrewSize") ? Integer.parseInt(params.get("maxCrewSize")) : null;
        Double minRating = params.containsKey("minRating") ? Double.parseDouble(params.get("minRating")) : null;
        Double maxRating = params.containsKey("maxRating") ? Double.parseDouble(params.get("maxRating")) : null;
        return shipRepository.countByParams(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }

    @Override
    public Integer count() {
        try {
            return Math.toIntExact(shipRepository.count());
        } catch (ArithmeticException e) {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean existsById(Long id) {
        return shipRepository.existsById(id);
    }


    public boolean isIdValid(Long id) {
        return id > 0;
    }
    @Override
    public boolean isParamsValid(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String planet = params.getOrDefault("planet", null);
        Integer prodDate = null;
        if (params.containsKey("prodDate")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(params.get("prodDate")));
            prodDate = calendar.get(Calendar.YEAR);
        }
        Double speed = params.containsKey("speed") ? Double.parseDouble(params.get("speed")) : null;
        Integer crewSize = params.containsKey("crewSize") ? Integer.parseInt(params.get("crewSize")) : null;
        boolean result =
                (name == null || planet == null || name.length() <= 50 && name.length() > 0 && planet.length() <= 50 && planet.length() > 0)
                        && (prodDate == null || prodDate >= 2800 && prodDate <= 3019)
                        && (speed == null || speed > 0 && speed < 1)
                        && (crewSize == null || crewSize >= 1 && crewSize <= 9999);
        try {
            if (params.containsKey("shipType")) {
                ShipType shipType = ShipType.valueOf(params.get("shipType"));
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            result = false;
        }
        return result;
    }


    public boolean isAllParamsFound(Map<String, String> params) {
        return params.containsKey("name")
                && params.containsKey("planet")
                && params.containsKey("shipType")
                && params.containsKey("prodDate")
                && params.containsKey("speed")
                && params.containsKey("crewSize");
    }
}
