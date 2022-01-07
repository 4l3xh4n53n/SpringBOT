package Core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static String mariaDBpass = "";

    public static void setMariaDBpass(String pass){
        mariaDBpass = pass;
    }

    // MariaDB Databases

    public static Connection connect() {
        try {
            Class.forName ("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://192.168.1.124:3306/Settings", "alex", mariaDBpass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection warns() {
        try {
            Class.forName ("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://192.168.1.124:3306/Warns", "alex", mariaDBpass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection coins() {
        try {
            Class.forName ("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://192.168.1.124:3306/Coins", "alex", mariaDBpass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection invites() {
        try {
            Class.forName ("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://192.168.1.124:3306/Invites", "alex", mariaDBpass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection CreatedChannels() {
        try {
            Class.forName ("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://192.168.1.124:3306/CreatedChannels", "alex", mariaDBpass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection ReactionRoles(){
        try {
            Class.forName ("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://192.168.1.124:3306/ReactionRoles", "alex", mariaDBpass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection Tickets(){
        try {
            Class.forName ("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://192.168.1.124:3306/Tickets", "alex", mariaDBpass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* SQLITE Databases

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
    } */
    
}
