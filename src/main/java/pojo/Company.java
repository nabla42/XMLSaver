package pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "company")
@XmlType(propOrder = {"name", "employees"})
public class Company implements Serializable {
    private String name;
    private List<Employee> employees;

    public Company() {
        employees = new ArrayList<>();
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    @XmlElement(name = "employee")
    public void setEmployees(List<Employee> offices) {
        this.employees = offices;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
}
