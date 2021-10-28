package db;

import db.entity.CompanyEntity;
import db.entity.EmployeeEntity;
import pojo.Company;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DBService {
    private static final Logger LOGGER = Logger.getLogger(DBService.class.getName());
    private static final EntityMapper mapper = new EntityMapper();

    private static final String dbURL = "jdbc:mysql://localhost:3306/";
    private static final String user = "root";
    private static final String pass = "root";
    private final Connection con;
    public DBService() {
        try {
            Path dir = Files.createDirectories(Paths.get("./log"));
            FileHandler fh = new FileHandler(dir + File.separator + "log.log");
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);

        } catch (IOException e) {
            e.printStackTrace();
        }
        con = getMySQLConnection();
    }
    private Connection getMySQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(dbURL, user, pass);

        } catch (ClassNotFoundException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't create SQL connection", ex);
        }
        return null;
    }

    /**
     * Создание таблиц - company и employee.
     */
    public void createTables() {
        try {
            Statement s = con.createStatement();
            s.executeUpdate("create database if not exists companies_db");
            s.executeUpdate("use companies_db");

            s.executeUpdate("create table if not exists company" +
                    "(company_id int not null auto_increment," +
                    "name varchar(50) not null, " +
                    "primary key (company_id), unique(name))");

            s.executeUpdate("create table if not exists employee" +
                    "(employee_id bigint not null," +
                    "first_name varchar(50) not null,"+
                    "last_name varchar(50) not null,"+
                    "job varchar(80),"+
                    "company_id int not null, " +
                    "primary key (employee_id)," +
                    "foreign key (company_id)" +
                    "references company(company_id))");
            s.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't create tables", ex);
        }
    }

    /**
     * Удаление таблиц - company и employee.
     */
    public void dropTables() {
        try {
            Statement s = con.createStatement();
            s.executeUpdate("use companies_db");
            s.executeUpdate("drop table employee");
            s.executeUpdate("drop table company");
            s.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't drop tables", ex);
        }
    }

    /**
     * Выгрузка объектов типа Company в базу данных.
     *
     * @param companies     массив компаний, не имеющих сотрудников
     */
    public void uploadEmptyCompanies(List<Company> companies) {
        List<CompanyEntity> companyEntities = mapper.mapToCompanyEntity(companies);
        try {
            PreparedStatement stmt = con.prepareStatement("insert ignore into company (name) values (?) ");
            for(CompanyEntity company: companyEntities) {
                stmt.setString(1, company.getName());
                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't upload companies.", ex);
        }
    }

    /**
     * Выгрузка объектов типа Company, содержащих Employee в БД.
     *
     * @param companies     массив компаний с сотрудниками
     */
    public void uploadCompanyAndEmployees(List<Company> companies) {
        try {
            PreparedStatement stmtE = con.prepareStatement("insert into employee " +
                    "(employee_id, first_name, last_name, job, company_id) values (?, ?, ?, ?, ?) " +
                    "on duplicate key update " +
                    "first_name = values(first_name), " +
                    "last_name = values(last_name), " +
                    "job = values(job), " +
                    "company_id = values(company_id)");
            for(Company company: companies) {
                addCompany(company);
                String query = "select company_id from company where name in(\'" + company.getName() + "\')";
                ResultSet result = con.createStatement().executeQuery(query);
                result.next();
                List<EmployeeEntity> employeeEntity =
                        mapper.mapToEmployeeEntityFromCompany(company, result.getInt(1));
                for(EmployeeEntity entity: employeeEntity) {
                    statementSettingEmployee(stmtE, entity);
                }
                stmtE.executeBatch();
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't upload employees from companies.", ex);
        }

    }

    /**
     * Добавление 1го пустого объекта Company.
     *
     * @param company       компания в единичном экземпляре, не содержащая сотрудников
     */
    public void addCompany(Company company) {
        try {
            PreparedStatement stmt = con.prepareStatement("insert ignore into company (name) values (?) ");
            CompanyEntity companyEntity = mapper.mapToCompanyEntity(company);
            stmt.setString(1, companyEntity.getName());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't upload company.", ex);
        }
    }
    private void statementSettingEmployee(PreparedStatement stmt, EmployeeEntity entity)
            throws SQLException {
        stmt.setLong(1, entity.getUuid());
        stmt.setString(2, entity.getFirstName());
        stmt.setString(3, entity.getLastName());
        stmt.setString(4, entity.getJob());
        stmt.setInt(5, entity.getCompanyId());
        stmt.addBatch();
    }
}
