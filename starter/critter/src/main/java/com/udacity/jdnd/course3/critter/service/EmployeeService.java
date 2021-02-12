package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            throw new RuntimeException("There is no such employee!");
        }
        return optionalEmployee.get();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee setEmployeeAvailability(Long id, Set<DayOfWeek> daysOfWeek) {
        Employee employee = employeeRepository.findById(id).get();
        employee.setDaysAvailable(daysOfWeek);
        return employeeRepository.save(employee);
    }

    public List<Employee> findEmployeeForService(LocalDate date, Set<EmployeeSkill> skills){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(dayOfWeek);
        List<Employee> employees = employeeRepository.findByDaysAvailableInAndSkillsIn(daysOfWeek, skills);
        List<Employee> availableEmployees = employees.stream()
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
        return availableEmployees;
    }
}
