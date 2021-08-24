package Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/Settings.db"); // connecting to our database
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection warns() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/Warns.db"); // connecting to our database
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection coins() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/Coins.db"); // connecting to our database
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection invites() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/Invites.db"); // connecting to our database
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection CreatedChannels() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/CreatedChannels.db"); // connecting to our database
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }
    
}
