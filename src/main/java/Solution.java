import db.DBService;
import processes.Parser;
import processes.XMLParser;

import java.io.*;

public class Solution {
    private static final String FILENAME = "./src/main/java/source/company.xml";
    public static void main(String[] args) throws FileNotFoundException {
        DBService db = new DBService();
//        db.dropTables();
        db.createTables();

        Parser parser = new XMLParser();
        parser.process(new DataInputStream(new FileInputStream(FILENAME)));

        db.uploadCompanyAndEmployees(parser.getResult());
    }

}
