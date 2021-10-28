package pojo;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "employee")
@XmlType(propOrder = {"uuid", "firstName","lastName", "job"})
public class Employee implements Serializable {
    private Long uuid;
    private String firstName;
    private String lastName;
    private String job;

    public Long getUuid() {
        return uuid;
    }
    @XmlAttribute(name = "uuid")
    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }
    @XmlAttribute(name = "firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    @XmlAttribute(name = "lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJob() {
        return job;
    }
    @XmlAttribute(name = "job")
    public void setJob(String job) {
        this.job = job;
    }

}
