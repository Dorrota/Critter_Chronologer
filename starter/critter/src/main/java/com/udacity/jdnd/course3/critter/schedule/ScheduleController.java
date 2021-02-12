package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final PetService petService;
    private final EmployeeService employeeService;
    private final CustomerService customerService;

    public ScheduleController(ScheduleService scheduleService, PetService petService, EmployeeService employeeService, CustomerService customerService) {
        this.scheduleService = scheduleService;
        this.petService = petService;
        this.employeeService = employeeService;
        this.customerService = customerService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertScheduleDTOToSchedule(scheduleDTO);
        scheduleService.saveSchedule(schedule);
        return convertScheduleToScheduleDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        List<Schedule> scheduleForPet = scheduleService.getScheduleForPet(pet);
        return scheduleForPet.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        List<Schedule> scheduleForEmployee = scheduleService.getScheduleForEmployee(employee);
        return scheduleForEmployee.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {

        List<Pet> pets = petService.getPetsByOwner(customerId);
        List<Schedule> schedules = new ArrayList<>();
        for (Pet p : pets){
            schedules.addAll(scheduleService.getScheduleForPet(p));
            System.out.println("Petttttttttttt" + p.getId());
        }
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule s: schedules){
            scheduleDTOS.add(convertScheduleToScheduleDTO(s));
        }
        return scheduleDTOS;
        //return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
        //throw new UnsupportedOperationException();
    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        List<Pet> pets = schedule.getPets();
        List<Long> petIds = new ArrayList<>();
        if (pets != null) {
            for (Pet p: pets) {
                petIds.add(p.getId());
            }
            scheduleDTO.setPetIds(petIds);
        }
        List<Employee> employees = schedule.getEmployees();
        List<Long> employeesIds = employees.stream().map(Employee::getId).collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeesIds);
        return scheduleDTO;
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        List<Pet> pets = new ArrayList<>();
        List<Long> petIds = scheduleDTO.getPetIds();
        for (Long id: petIds) {
            pets.add(petService.getPetById(id));
        }
        schedule.setPets(pets);
        List<Employee> employees = new ArrayList<>();
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        for (Long id: employeeIds) {
            employees.add(employeeService.getEmployeeById(id));
        }
        schedule.setEmployees(employees);
        return schedule;
    }

}
