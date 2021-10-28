package processes;

import db.DBService;
import org.xml.sax.SAXException;
import pojo.Company;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class XMLParser implements Parser {
    private static final Logger LOGGER = Logger.getLogger(DBService.class.getName());
    private List<Company> companies;
    public XMLParser() {
        try {
            Path dir = Files.createDirectories(Paths.get("./log"));
            FileHandler fh = new FileHandler(dir + File.separator + "log.log");
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void process(InputStream is) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();

            XMLHandler handler = new XMLHandler();
            saxParser.parse(is, handler);

            companies = handler.getResult();

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Can't parse xml to objects.", ex);
        }
    }

    @Override
    public List<Company> getResult() {
        return companies;
    }

}
