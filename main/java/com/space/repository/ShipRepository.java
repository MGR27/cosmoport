package com.space.repository;


import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ShipRepository extends JpaRepository<Ship,Long> {
    @Query("select u from Ship u where "
            + "(:name is not null and u.name like concat('%', :name, '%') or :name is null) "
            + "and (:planet is not null and u.planet like concat('%', :planet, '%') or :planet is null) "
            + "and (:shipType is not null and u.shipType like :shipType or :shipType is null) "
            + "and (:after is not null and u.prodDate >= :after or :after is null) "
            + "and (:before is not null and u.prodDate <= :before or :before is null) "
            + "and (:isUsed is not null and u.isUsed = :isUsed or :isUsed is null) "
            + "and (:minSpeed is not null and u.speed >= :minSpeed or :minSpeed is null) "
            + "and (:maxSpeed is not null and u.speed <= :maxSpeed or :maxSpeed is null) "
            + "and (:minCrewSize is not null and u.crewSize >= :minCrewSize or :minCrewSize is null) "
            + "and (:maxCrewSize is not null and u.crewSize <= :maxCrewSize or :maxCrewSize is null) "
            + "and (:minRating is not null and u.rating >= :minRating or :minRating is null) "
            + "and (:maxRating is not null and u.rating <= :maxRating or :maxRating is null)")
    Page<Ship> findAllByParams(
            @Param("name") String name,
            @Param("planet") String planet,
            @Param("shipType") ShipType shipType,
            @Param("after") Date after,
            @Param("before") Date before,
            @Param("isUsed") Boolean isUsed,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed,
            @Param("minCrewSize") Integer minCrewSize,
            @Param("maxCrewSize") Integer maxCrewSize,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating,
            Pageable pageable);

    @Override
    Page<Ship> findAll(Pageable pageable);

    @Query("select count(u) from Ship u where "
            + "(:name is not null and u.name like concat('%', :name, '%') or :name is null) "
            + "and (:planet is not null and u.planet like concat('%', :planet, '%') or :planet is null) "
            + "and (:shipType is not null and u.shipType like :shipType or :shipType is null) "
            + "and (:after is not null and u.prodDate >= :after or :after is null) "
            + "and (:before is not null and u.prodDate <= :before or :before is null) "
            + "and (:isUsed is not null and u.isUsed = :isUsed or :isUsed is null) "
            + "and (:minSpeed is not null and u.speed >= :minSpeed or :minSpeed is null) "
            + "and (:maxSpeed is not null and u.speed <= :maxSpeed or :maxSpeed is null) "
            + "and (:minCrewSize is not null and u.crewSize >= :minCrewSize or :minCrewSize is null) "
            + "and (:maxCrewSize is not null and u.crewSize <= :maxCrewSize or :maxCrewSize is null) "
            + "and (:minRating is not null and u.rating >= :minRating or :minRating is null) "
            + "and (:maxRating is not null and u.rating <= :maxRating or :maxRating is null)")
    int countByParams(
            @Param("name") String name,
            @Param("planet") String planet,
            @Param("shipType") ShipType shipType,
            @Param("after") Date after,
            @Param("before") Date before,
            @Param("isUsed") Boolean isUsed,
            @Param("minSpeed") Double minSpeed,
            @Param("maxSpeed") Double maxSpeed,
            @Param("minCrewSize") Integer minCrewSize,
            @Param("maxCrewSize") Integer maxCrewSize,
            @Param("minRating") Double minRating,
            @Param("maxRating") Double maxRating
    );
}
