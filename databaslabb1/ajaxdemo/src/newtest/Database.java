package newtest;
	
import java.io.IOException;
import java.sql.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;

/**
 * Database is a class that specifies the interface to the
 * movie database. Uses JDBC and the MySQL Connector/J driver.
 */

public class Database {
    private Connection conn;        

    public Database() {
    	
    }        

    public boolean openConnection(String username, String password, String hostname) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection
                ("jdbc:mysql://" + hostname + "/" + username,
                 username, password);
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }      

    /**
    * Close the connection to the database.
    */
    public void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {}
        conn = null;
    }        

    /**
    * Check if the connection to the database has been established
    *
    * @return true if the connection has been established
    */
    public boolean isConnected() {
        return conn != null;
    }
    
    public Connection getConn() {
    	return conn;
    }
    
    public Connection conn() {
    	if(!isConnected()) {
    		String arr[] = getDbSettings();
    		openConnection(arr[0], arr[1], arr[2]);
    	}
    	return conn;
    }
    
    private String[] getDbSettings() {
        Document dom;
        
        String arr[] = new String[3], username = null, password = null, hostname = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("H:\\aegis_git\\Aegis\\databaslabb1\\ajaxdemo\\src\\settings.xml");

            Element doc = dom.getDocumentElement();

            arr[0] = getTextValue(username, doc, "username");
            arr[1] = getTextValue(password, doc, "password");
            arr[2] = getTextValue(hostname, doc, "hostname");
        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

        return arr;
    }
    
    private String getTextValue(String def, Element doc, String tag) {
        String value = def;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }



}
