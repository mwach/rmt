package com.mobica.repository;

import com.mobica.domain.Msu;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Msu entity.
 */
public interface MsuRepository extends JpaRepository<Msu,Long> {

}
