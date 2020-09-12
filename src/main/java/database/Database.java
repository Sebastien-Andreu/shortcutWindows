package database;

import element.ShortcutApp;
import element.ShortcutElement;
import element.ShortcutFileFolder;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static Connection connect() {
        String url = "jdbc:sqlite:BD.db";
        Connection conn = null;
        File directory = new File("BD.db");
        if (!directory.exists()) {
            createDatabase(url);
        }
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }

        return conn;
    }

    public List<ShortcutElement> getListOfShortcut () {
        String query = "select * from Shortcut order by Pos";
        List<ShortcutElement> list = new ArrayList<>();

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()) {
                list.add(new ShortcutElement(rs.getInt("ID"),rs.getInt("Pos"),rs.getString("Text"),rs.getString("Url")));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return list;
    }

    public List<ShortcutFileFolder> getListOfShortcutFolder () {
        String query = "select * from ShortcutFolder order by Pos";
        List<ShortcutFileFolder> list = new ArrayList<>();

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()) {
                list.add(new ShortcutFileFolder(rs.getInt("ID"),rs.getInt("Pos"),rs.getString("Url"),rs.getString("Text")));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return list;
    }

    public List<ShortcutApp> getListOfShortcutApp () {
        String query = "select * from ShortcutApp order by Pos";
        List<ShortcutApp> list = new ArrayList<>();

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()) {
                list.add(new ShortcutApp(rs.getInt("ID"),rs.getInt("Pos"),rs.getString("Url"),rs.getString("Text")));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return list;
    }

    public void addNewShortcut (int pos, String text, String url) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Shortcut(Text, Url, Pos) VALUES(?,?,?)");Statement stmt = conn.createStatement()){
            pstmt.setString(1, text);
            pstmt.setString(2, url);
            pstmt.setInt(3, pos);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNewShortcutFolder(String url, String text, int pos) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ShortcutFolder(Url, Text, Pos) VALUES(?,?,?)");Statement stmt = conn.createStatement()){
            pstmt.setString(1, url);
            pstmt.setString(2, text);
            pstmt.setInt(3, pos);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNewShortcutApp(String url, String text, int pos) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ShortcutApp(Url, Text, Pos) VALUES(?,?,?)");Statement stmt = conn.createStatement()){
            pstmt.setString(1, url);
            pstmt.setString(2, text);
            pstmt.setInt(3, pos);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ShortcutElement getLastShortcutElement () {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from Shortcut where ID = (select max(ID) from Shortcut)")){
            return new ShortcutElement(rs.getInt("ID"),rs.getInt("Pos"),rs.getString("Text"),rs.getString("Url"));
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
        return null;
    }

    public ShortcutFileFolder getLastShortcutFolderElement () {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from ShortcutFolder where ID = (select max(ID) from ShortcutFolder)")){
            return new ShortcutFileFolder(rs.getInt("ID"),rs.getInt("Pos"),rs.getString("Url"),rs.getString("Text"));
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
        return null;
    }

    public ShortcutApp getLastShortcutApp () {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from ShortcutApp where ID = (select max(ID) from ShortcutApp)")){
            return new ShortcutApp(rs.getInt("ID"),rs.getInt("Pos"),rs.getString("Url"),rs.getString("Text"));
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
        return null;
    }

    public void updateShortcutElement (ShortcutElement shortcutElement, String text, String url) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(
                "update Shortcut set " +
                "Text = '" + text + "', " +
                "Url = '" + url + "' " +
                "where ID = '" + shortcutElement.id + "'")){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePosOfShortcut (ShortcutElement shortcutElement, int pos) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("update Shortcut set Pos = '" + pos + "' " + "where ID = '" + shortcutElement.id + "'")){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePosOfShortcutFolder(ShortcutFileFolder shortcutFileFolder, int pos) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("update ShortcutFolder set Pos = '" + pos + "' " + "where ID = '" + shortcutFileFolder.id + "'")){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePosOfShortcutFolder(ShortcutApp shortcutApp, int pos) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("update ShortcutApp set Pos = '" + pos + "' " + "where ID = '" + shortcutApp.id + "'")){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteShortcutElement (ShortcutElement shortcutElement) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE from Shortcut where ID = ?")){
            pstmt.setInt(1, shortcutElement.id);
            pstmt.executeUpdate();
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
    }

    public void deleteShortcutFolder(ShortcutFileFolder shortcutFileFolder) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE from ShortcutFolder where ID = ?")){
            pstmt.setInt(1, shortcutFileFolder.id);
            pstmt.executeUpdate();
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
    }

    public void deleteShortcutApp(ShortcutApp shortcutApp) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE from ShortcutApp where ID = ?")){
            pstmt.setInt(1, shortcutApp.id);
            pstmt.executeUpdate();
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
    }

    public void setPositionOfShortcut (ShortcutElement shortcutElement, List<ShortcutElement> list) {
        if (shortcutElement.pos != 0) {
            updatePosOfShortcut(shortcutElement, 0);
            int pos = 0;
            for (ShortcutElement element: list) {
                if (!element.equals(shortcutElement)) {
                    element.pos = ++pos;
                    updatePosOfShortcut(element, pos);
                }
            }
        }
    }

    public void setPositionOfShortcutFolder(ShortcutFileFolder shortcutFileFolder, ObservableList<ShortcutFileFolder> list) {
        if (shortcutFileFolder.pos != 0) {
            updatePosOfShortcutFolder(shortcutFileFolder, 0);
            int pos = 0;
            for (ShortcutFileFolder element: list) {
                if (!element.equals(shortcutFileFolder)) {
                    element.pos = ++pos;
                    updatePosOfShortcutFolder(element, pos);
                }
            }
        }
    }

    public void setPositionOfShortcutApp(ShortcutApp shortcutApp, ObservableList<ShortcutApp> list) {
        if (shortcutApp.pos != 0) {
            updatePosOfShortcutFolder(shortcutApp, 0);
            int pos = 0;
            for (ShortcutApp element: list) {
                if (!element.equals(shortcutApp)) {
                    element.pos = ++pos;
                    updatePosOfShortcutFolder(element, pos);
                }
            }
        }
    }


    private static void createDatabase (String url) {
        String shortcut = "CREATE TABLE IF NOT EXISTS \"Shortcut\" (\"ID\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\"Text\" REAL NOT NULL,\"Url\" REAL NOT NULL,\"Pos\" REAL NOT NULL)";
        String shortcutFolder = "CREATE TABLE IF NOT EXISTS \"ShortcutFolder\" (\"ID\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\"Url\" REAL NOT NULL,\"Text\" REAL NOT NULL,\"Pos\" REAL NOT NULL)";
        String shortcutApp = "CREATE TABLE IF NOT EXISTS \"ShortcutApp\" (\"ID\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\"Url\" REAL NOT NULL,\"Text\" REAL NOT NULL,\"Pos\" REAL NOT NULL)";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(shortcut);
            stmt.execute(shortcutFolder);
            stmt.execute(shortcutApp);
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
    }
}
