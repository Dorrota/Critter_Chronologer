package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;
    private final CustomerService customerService;

    public PetController(PetService petService, CustomerService customerService) {
        this.petService = petService;
        this.customerService = customerService;
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = petService.savePet(convertPetDTOToPet(petDTO));
        return convertPetToPetDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertPetToPetDTO(petService.getPetById(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> petList = petService.getAllPets();
        return petList.stream().map(this::convertPetToPetDTO).collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        if (customerService.findCustomerById(ownerId) == null){
            throw new UnsupportedOperationException();
        }
        List<Pet> petList = petService.getPetsByOwner(ownerId);
        List<PetDTO> petDTOList = new ArrayList<>();
        for (Pet p: petList) {
            petDTOList.add(convertPetToPetDTO(p));
        }
        return petDTOList;
    }

    private Pet convertPetDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setName(petDTO.getName());
        pet.setNotes(petDTO.getNotes());
        pet.setType(petDTO.getType());
        pet.setCustomer(customerService.findCustomerById(petDTO.getOwnerId()));
        return pet;
    }

    private PetDTO convertPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        copyProperties(pet, petDTO);
        petDTO.setId(pet.getId());
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }

}
