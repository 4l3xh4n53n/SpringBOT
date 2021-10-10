package Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/Settings.db");
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection warns() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/Warns.db");
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection coins() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/Coins.db");
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection invites() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/Invites.db");
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection CreatedChannels() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/CreatedChannels.db");
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection ReactionRoles(){
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/ReactionRoles.db");
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }

    public static Connection Tickets(){
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Databases/Tickets.db");
        } catch (ClassNotFoundException | SQLException e ) {
            System.out.println(e+"");
        }

        return con;
    }
    
}
