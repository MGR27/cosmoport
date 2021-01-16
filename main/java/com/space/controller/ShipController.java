package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.awt.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class ShipController {

    private ShipService shipService;
    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

//    @RequestMapping(value = "id",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<Ship> getShip(@PathVariable("id") Long id){
//        if (id == null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        Ship ship = this.shipService.getShipById(id);
//        if (ship == null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(ship,HttpStatus.OK);
//    }
@GetMapping("/rest/ships/{id}")
public @ResponseBody ResponseEntity<Ship> getShip(
        @PathVariable Long id
){
    try {
        if (!shipService.isIdValid(id)) {
            return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        }
        if (!shipService.existsById(id)) {
            return new ResponseEntity<Ship>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Ship>(shipService.getShipById(id), HttpStatus.OK);
        }
    } catch (NullPointerException | IllegalArgumentException e) {
        return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
    }
}


    @GetMapping("/rest/ships")
    public @ResponseBody ResponseEntity<List<Ship>> getShipsList(@RequestParam Map<String, String> params){
        if (params.isEmpty()) {
            return new ResponseEntity<List<Ship>>(shipService.findAll(PageRequest.of(0, 3)), HttpStatus.OK);
        } else {
            return new ResponseEntity<List<Ship>>(shipService.getByParams(params), HttpStatus.OK);
        }
    }

    @GetMapping("/rest/ships/count")
    public @ResponseBody Integer getShipsCount(@RequestParam Map<String, String> params){
        if (params.isEmpty()) {
            return shipService.count();
        } else {
            return shipService.countByParams(params);
        }
    }

    @RequestMapping("/rest/ships")
    @ResponseStatus(HttpStatus.OK)
    public  ResponseEntity<Ship> addShip(@RequestBody Map<String, String> params){
        if (!shipService.isAllParamsFound(params) || !shipService.isParamsValid(params)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ship ship = shipService.create(params);
        return ship == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping("/rest/ships/{id}")
    public @ResponseBody
    ResponseEntity<Ship> deleteShip(@PathVariable Long id){
        ResponseEntity<Ship> okResponse = new ResponseEntity<Ship>(HttpStatus.OK);
        ResponseEntity<Ship> badResponse = new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Ship> nfResponse = new ResponseEntity<Ship>(HttpStatus.NOT_FOUND);
        try {
            if (!shipService.isIdValid(id)) return badResponse;
            String result = shipService.delete(id);
            if (result == null) return badResponse;
            if ("404".equals(result)) return nfResponse;
            if ("200".equals(result)) return okResponse;
        } catch (NullPointerException | IllegalArgumentException e) {
            return null;
        }
        return okResponse;
    }

    @PostMapping("/rest/ships/{id}")
    public ResponseEntity<Ship> updateShipById(@RequestBody Ship newShip, @PathVariable Long id) {

        if (!shipService.isIdValid(id)){
            return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        }
        if (newShip == null){
            return new ResponseEntity<>(shipService.getShipById(id),HttpStatus.OK);
        }
        Ship ship = shipService.updateById(id,newShip);
        ResponseEntity<Ship> entity = new ResponseEntity<Ship>(ship,HttpStatus.OK);
        if (ship == null){
            entity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return entity;

    }
}
