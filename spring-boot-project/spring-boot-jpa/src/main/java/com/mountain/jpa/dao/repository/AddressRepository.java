package com.mountain.jpa.dao.repository;


import com.mountain.jpa.dao.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}