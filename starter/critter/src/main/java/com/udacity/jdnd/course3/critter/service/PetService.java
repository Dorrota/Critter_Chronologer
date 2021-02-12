package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final CustomerService customerService;

    public PetService(PetRepository petRepository, CustomerService customerService) {
        this.petRepository = petRepository;
        this.customerService = customerService;
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        return petRepository.findAllByCustomer_Id(ownerId);
    }

    public Pet savePet(Pet pet) {
        Customer customer = pet.getCustomer();
        customerService.addPetToCustomer(pet, customer);
        petRepository.save(pet);
        return pet;
    }

    public Pet getPetById(Long id) {
        return petRepository.findById(id).get();
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }
}
