package com.aps.grpc_service.controller;

import com.aps.employee.model.*;
import com.aps.grpc_service.service.EmployeeService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
public class EmployeeGrpcController extends EmployeeServiceGrpc.EmployeeServiceImplBase {
    @Autowired
    private EmployeeService employeeService;

    @Override
    public void getEmployee(EmployeeRequest request, StreamObserver<EmployeeResponse> streamObserver) {
        try {
            EmployeeResponse employeeResponse = employeeService.getEmployee(request);
            streamObserver.onNext(employeeResponse);
        }
        catch (StatusRuntimeException sre) {
            streamObserver.onError(sre);
        }
        catch (Exception e) {
            streamObserver.onError(Status.UNKNOWN.asRuntimeException());
        }
        finally {
            streamObserver.onCompleted();
        }
    }

    @Override
    public void createEmployee(CreateEmployeeRequest request, StreamObserver<CreateEmployeeResponse> streamObserver) {
        try {
            CreateEmployeeResponse createEmployeeResponse = employeeService.createEmployee(request);
            streamObserver.onNext(createEmployeeResponse);
        }
        catch (StatusRuntimeException sre) {
            streamObserver.onError(sre);
        }
        catch (Exception e) {
            streamObserver.onError(Status.UNKNOWN.asRuntimeException());
        }
        finally {
            streamObserver.onCompleted();
        }
    }

    @Override
    public void getEmployeeList(EmployeeListRequest request, StreamObserver<EmployeeListResponse> streamObserver) {
        try {
            EmployeeListResponse employeeList = employeeService.getEmployeeList(request);
            streamObserver.onNext(employeeList);
        }
        catch (StatusRuntimeException sre) {
            streamObserver.onError(sre);
        }
        catch (Exception e) {
            streamObserver.onError(Status.UNKNOWN.asRuntimeException());
        }
        finally {
            streamObserver.onCompleted();
        }
    }
}
