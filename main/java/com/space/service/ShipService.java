package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface ShipService {
    List<Ship> findAll(Pageable pageable);
    Ship create(Map<String,String> params);
    Ship updateById(Long id, Ship ship);
    Long idChecker(String id);
    String delete(Long id);
    Ship getShipById(Long id);
    Integer countByParams(Map<String, String> params);
    Integer count();
    List<Ship> getByParams(Map<String,String> params);
    boolean existsById(Long id);
    boolean isIdValid(Long id);
    boolean isParamsValid(Map<String, String> params);
    boolean isAllParamsFound(Map<String, String> params);
}
