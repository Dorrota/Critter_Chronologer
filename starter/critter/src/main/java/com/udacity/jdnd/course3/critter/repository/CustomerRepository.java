package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

//    @Query("select c from Customer c join Pet p on p.id = :petId")
//    Customer findByPetId(Long petId);

    Customer findByPetsId(Long petId);
}
