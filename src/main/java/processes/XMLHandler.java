package processes;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import pojo.Company;
import pojo.Employee;

import java.util.ArrayList;
import java.util.List;

public class XMLHandler extends DefaultHandler {
    private StringBuilder currentValue = new StringBuilder();
    private List<Company> companies;
    private Company currentCompany;
    private Employee currentEmployee;

    public List<Company> getResult(){
        return companies;
    }

    @Override
    public void startDocument() {
        companies = new ArrayList<>();
    }

    @Override
    public void startElement(
            String uri,
            String localName,
            String qName,
            Attributes attributes) {

        currentValue.setLength(0);

        if(qName.equalsIgnoreCase("company")) {
            currentCompany = new Company();
        }
        if (qName.equalsIgnoreCase("employee")) {
            currentEmployee = new Employee();

            currentEmployee.setUuid(Long.parseLong(attributes.getValue("uuid")));
            currentEmployee.setFirstName(attributes.getValue("firstName"));
            currentEmployee.setLastName(attributes.getValue("lastName"));
            currentEmployee.setJob(attributes.getValue("job"));

            currentCompany.addEmployee(currentEmployee);
        }

    }

    @Override
    public void endElement(String uri,
                           String localName,
                           String qName) {

        if (qName.equalsIgnoreCase("name")) {
            currentCompany.setName(currentValue.toString());
        }
        if(qName.equalsIgnoreCase("company")) {
            companies.add(currentCompany);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        currentValue.append(ch, start, length);
    }
}
