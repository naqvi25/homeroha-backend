package com.homeroha.repository;

import com.homeroha.model.SharedContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SharedContactRepository extends JpaRepository<SharedContact, Long> {

    @Query("SELECT c FROM SharedContact c WHERE c.home.id = :homeId")
    Page<SharedContact> findByHomeId(@Param("homeId") Long homeId, Pageable pageable);

    List<SharedContact> findByHomeId(Long homeId);
}