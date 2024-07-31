package com.aps.grpc_service.service.impl;

import com.aps.employee.model.*;
import com.aps.grpc_service.model.Employee;
import com.aps.grpc_service.repository.EmployeeRepository;
import com.aps.grpc_service.service.EmployeeService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger logger = LogManager.getLogger(EmployeeServiceImpl.class);

    @Override
    public EmployeeResponse getEmployee(EmployeeRequest request) {
        try {
            Optional<Employee> employee = employeeRepository.findById(Long.parseLong(request.getId()));
            if(employee.isEmpty()) {
                throw new StatusRuntimeException(Status.NOT_FOUND.withDescription("Item not found."));
            }
            EmployeeResponse grpcRespone = EmployeeResponse.newBuilder()
                    .setId(String.valueOf(employee.get().getId()))
                    .setAddress(employee.get().getAddress())
                    .setPhone(employee.get().getPhone())
                    .setMail(employee.get().getMail()).build();
            return grpcRespone;

        }
        catch (StatusRuntimeException sre) {
            logger.error("getEmployee:StatusRuntimeException -> {}", sre.getMessage());
            throw sre;
        }
        catch (Exception e) {
            logger.error("getEmployee:Exeception -> {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request) {
        try {
            if(request.getUsername().isEmpty() || request.getPhone().isEmpty()) {
                throw new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription("Username and phone are required."));
            }
            Employee employee = new Employee();
            employee.setUsername(request.getUsername());
            employee.setAddress(request.getAddress());
            employee.setPhone(request.getPhone());
            employee.setMail(request.getMail());
            Employee saveResponse = employeeRepository.save(employee);
            CreateEmployeeResponse response = CreateEmployeeResponse.newBuilder()
                    .setCode("200")
                    .setStatus("Success")
                    .setMessage("Employee have been created successfully.").build();
            return response;
        }
        catch (StatusRuntimeException sre) {
            logger.error("createEmployee:StatusRuntimeException -> {}", sre.getMessage());
            throw sre;
        }
        catch (Exception e) {
            logger.error("createEmployee:Exeception -> {}", e.getMessage());
            throw e;
        }

    }

    @Override
    public EmployeeListResponse getEmployeeList(EmployeeListRequest request) {
        try {
            int page = request.getPage();
            int limit = request.getLimit();

            Pageable pageable = PageRequest.of(page, limit);
            Page<Employee> pageObj = employeeRepository.findAll(pageable);
            List<Employee> employees = pageObj.getContent();
            EmployeeListResponse.Builder listResponse = EmployeeListResponse.newBuilder();
            listResponse.setCurrentPage(page);
            listResponse.setTotalItems(pageObj.getTotalElements());
            listResponse.setTotalPages(pageObj.getTotalPages());

            for (Employee employee : employees) {
                EmployeeResponse employeeResponse = EmployeeResponse.newBuilder()
                        .setId(String.valueOf(employee.getId()))
                        .setAddress(employee.getAddress())
                        .setPhone(employee.getPhone())
                        .setMail(employee.getMail()).build();
                listResponse.addData(employeeResponse);
            }
            return listResponse.build();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
