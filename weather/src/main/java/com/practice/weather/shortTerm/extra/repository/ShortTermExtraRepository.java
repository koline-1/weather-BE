package com.practice.weather.shortTerm.extra.repository;

import com.practice.weather.shortTerm.extra.entity.ShortTermExtraEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShortTermExtraRepository extends JpaRepository<ShortTermExtraEntity, Long>, ShortTermExtraRepositoryCustom {

    @Query("SELECT e FROM ShortTermExtraEntity e ORDER BY e.id DESC")
    List<ShortTermExtraEntity> selectList(Pageable pageable);

    @Query("SELECT e FROM ShortTermExtraEntity e WHERE e.nxValue = :nxValue AND e.nyValue = :nyValue ORDER BY e.id DESC")
    List<ShortTermExtraEntity> selectListByLocation(Pageable pageable, @Param("nxValue") String nxValue, @Param("nyValue") String nyValue);

    @Query(value = "SELECT COUNT(1) FROM SHORT_TERM_EXTRA WHERE NX_VALUE = :nxValue AND NY_VALUE = :nyValue", nativeQuery = true)
    long countByLocation(@Param("nxValue") String nxValue, @Param("nyValue") String nyValue);
    
}
