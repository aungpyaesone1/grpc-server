package com.aps.grpc_service.service;

import com.aps.employee.model.*;

public interface EmployeeService {
    public EmployeeResponse getEmployee(EmployeeRequest request);
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request);
    public EmployeeListResponse getEmployeeList(EmployeeListRequest request);
}
