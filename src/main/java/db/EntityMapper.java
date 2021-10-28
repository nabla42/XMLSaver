package db;

import db.entity.CompanyEntity;
import db.entity.EmployeeEntity;
import pojo.Company;
import pojo.Employee;

import java.util.List;
import java.util.stream.Collectors;

public class EntityMapper {
    public EntityMapper(){}

    public List<CompanyEntity> mapToCompanyEntity(List<Company> companies) {
        return companies.stream()
                .map(this::mapToCompanyEntity)
                .collect(Collectors.toList());
    }
    public CompanyEntity mapToCompanyEntity(Company company) {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setName(company.getName());

        return companyEntity;
    }
    public List<EmployeeEntity> mapToEmployeeEntityFromCompany(Company company, Integer companyId) {
        return company.getEmployees()
                .stream()
                .map(this::mapToEmployeeEntity)
                .peek(c -> c.setCompanyId(companyId))
                .collect(Collectors.toList());
    }
    private EmployeeEntity mapToEmployeeEntity(Employee employee) {
        EmployeeEntity employeeEntity = new EmployeeEntity();

        employeeEntity.setUuid(employee.getUuid());
        employeeEntity.setFirstName(employee.getFirstName());
        employeeEntity.setLastName(employee.getLastName());
        employeeEntity.setJob(employee.getJob());

        return employeeEntity;
    }
}
