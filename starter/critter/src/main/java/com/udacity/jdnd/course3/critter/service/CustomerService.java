package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    public final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findCustomerByPetId(Long petId){
        //Long customerId = customerRepository.findByPetId(petId);
        Optional<Customer> optionalCustomer = customerRepository.findById(petId);
        if (!optionalCustomer.isPresent()){
            throw new RuntimeException("Oo, no owner present!");
        }
        return optionalCustomer.get();
    }
}
