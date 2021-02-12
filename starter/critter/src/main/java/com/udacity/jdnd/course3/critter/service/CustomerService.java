package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Customer findCustomerById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new RuntimeException("There is no such man!");
        }
        return optionalCustomer.get();
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findCustomerByPetId(Long petId){
        Customer customer = customerRepository.findByPetsId(petId);
        if (customer == null){
            throw new RuntimeException("Oo, no owner present!");
        }
        return customer;
    }

    public void addPetToCustomer(Pet pet, Customer customer) {
        List<Pet> petList = customer.getPets();
        if (petList == null) {
            petList = new ArrayList<>();
        }
        petList.add(pet);
    }
}
