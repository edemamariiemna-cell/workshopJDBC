package tn.edu.esprit.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private static Connection cnx;
    private static DataSource instance;
    
    private final String URL = "jdbc:mysql://localhost:3306/esprit?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String USER = "root";
    private final String PASSWORD = "";
    
    private DataSource() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Connexion à la base de données établie!");
        } catch (SQLException ex) {
            System.out.println(" Erreur de connexion: " + ex.getMessage());
        }
    }
    
    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }
    
    public static Connection getConnection() {
        if (cnx == null) {
            getInstance();
        }
        return cnx;
    }
}