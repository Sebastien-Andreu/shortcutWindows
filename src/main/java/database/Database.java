package database;

import element.ShortcutApp;
import element.ShortcutElement;
import element.ShortcutElementMostUsed;
import element.ShortcutFolder;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import singleton.SingletonColor;
import singleton.SingletonPosScreen;
import singleton.SingletonShortcut;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {


    public static Connection connect() {
        String url = "jdbc:sqlite:BD.db";
        Connection conn = null;
        createDatabase(url);
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

    public List<ShortcutElementMostUsed> getListOfShortcutMostUsed () {
        String query = "select * from Shortcut order by Pos";
        List<ShortcutElementMostUsed> list = new ArrayList<>();

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()) {
                list.add(new ShortcutElementMostUsed(rs.getString("Text"),rs.getString("Url")));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
        return list;
    }

    public List<ShortcutFolder> getListOfShortcutFolder () {
        String query = "select * from ShortcutFolder order by Pos";
        List<ShortcutFolder> list = new ArrayList<>();

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()) {
                list.add(new ShortcutFolder(rs.getInt("ID"),rs.getInt("Pos"),rs.getString("Url"),rs.getString("Text")));
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
                System.out.println(SingletonShortcut.saveFolder + "\\shortcut\\application\\" + rs.getString("Url"));
                list.add(new ShortcutApp(rs.getInt("ID"),rs.getInt("Pos"),SingletonShortcut.saveFolder + "\\shortcut\\application\\" + rs.getString("Url"),rs.getString("Text")));
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

    public ShortcutFolder getLastShortcutFolderElement () {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from ShortcutFolder where ID = (select max(ID) from ShortcutFolder)")){
            return new ShortcutFolder(rs.getInt("ID"),rs.getInt("Pos"),rs.getString("Url"),rs.getString("Text"));
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
        return null;
    }

    public ShortcutApp getLastShortcutApp () {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from ShortcutApp where ID = (select max(ID) from ShortcutApp)")){
            return new ShortcutApp(rs.getInt("ID"),rs.getInt("Pos"), SingletonShortcut.saveFolder + "\\shortcut\\application\\" + rs.getString("Url"),rs.getString("Text"));
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

    private void updatePosOfShortcutFolder(ShortcutFolder shortcutFolder, int pos) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("update ShortcutFolder set Pos = '" + pos + "' " + "where ID = '" + shortcutFolder.id + "'")){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePosOfShortcutApp(ShortcutApp shortcutApp, int pos) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("update ShortcutApp set Pos = '" + pos + "' " + "where ID = '" + shortcutApp.id + "'")){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteShortcutElement (ShortcutElement shortcutElement, List<ShortcutElement> list) {
        setPositionOfShortcutAfterDelete(shortcutElement, list);
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE from Shortcut where ID = ?")){
            pstmt.setInt(1, shortcutElement.id);
            pstmt.executeUpdate();
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
    }

    public void deleteShortcutFolder(ShortcutFolder shortcutFolder, List<ShortcutFolder> list) {
        setPositionOfShortcutFolderAfterDelete(shortcutFolder, list);
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE from ShortcutFolder where ID = ?")){
            pstmt.setInt(1, shortcutFolder.id);
            pstmt.executeUpdate();
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
    }

    public void deleteShortcutApp(ShortcutApp shortcutApp, List<ShortcutApp> list) {
        setPositionOfShortcutAppAfterDelete(shortcutApp, list);
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("DELETE from ShortcutApp where ID = ?")){
            pstmt.setInt(1, shortcutApp.id);
            pstmt.executeUpdate();
        } catch (SQLException var59) {
            System.out.println(var59.getMessage());
        }
    }

    public void setPositionOfShortcut (ShortcutElement shortcutElementChoose, ShortcutElement shortcutElementTarget, List<ShortcutElement> list) {
        int posTemp = shortcutElementTarget.pos;
        if (shortcutElementChoose.pos > shortcutElementTarget.pos){
            for (int pos = shortcutElementTarget.pos; pos < shortcutElementChoose.pos; ++pos){
                list.get(pos).pos = pos + 1;
                updatePosOfShortcut(list.get(pos), pos + 1);
            }
        } else {
            for (int pos = shortcutElementTarget.pos; pos > shortcutElementChoose.pos; --pos){
                list.get(pos).pos = pos -1;
                updatePosOfShortcut(list.get(pos), pos - 1);
            }
        }
        list.get(shortcutElementChoose.pos).pos = posTemp;
        updatePosOfShortcut(shortcutElementChoose, posTemp);
    }

    public void setPositionOfShortcutFolder(ShortcutFolder shortcutElementChoose, ShortcutFolder shortcutElementTarget, ObservableList<ShortcutFolder> list) {
        int posTemp = shortcutElementTarget.pos;
        if (shortcutElementChoose.pos > shortcutElementTarget.pos){
            for (int pos = shortcutElementTarget.pos; pos < shortcutElementChoose.pos; ++pos){
                list.get(pos).pos = pos + 1;
                updatePosOfShortcutFolder(list.get(pos), pos + 1);
            }
        } else {
            for (int pos = shortcutElementTarget.pos; pos > shortcutElementChoose.pos; --pos){
                list.get(pos).pos = pos -1;
                updatePosOfShortcutFolder(list.get(pos), pos - 1);
            }
        }
        list.get(shortcutElementChoose.pos).pos = posTemp;
        updatePosOfShortcutFolder(shortcutElementChoose, posTemp);
    }

    public void setPositionOfShortcutApp(ShortcutApp shortcutElementChoose, ShortcutApp shortcutElementTarget, ObservableList<ShortcutApp> list) {
        int posTemp = shortcutElementTarget.pos;
        if (shortcutElementChoose.pos > shortcutElementTarget.pos){
            for (int pos = shortcutElementTarget.pos; pos < shortcutElementChoose.pos; ++pos){
                list.get(pos).pos = pos + 1;
                updatePosOfShortcutApp(list.get(pos), pos + 1);
            }
        } else {
            for (int pos = shortcutElementTarget.pos; pos > shortcutElementChoose.pos; --pos){
                list.get(pos).pos = pos -1;
                updatePosOfShortcutApp(list.get(pos), pos - 1);
            }
        }
        list.get(shortcutElementChoose.pos).pos = posTemp;
        updatePosOfShortcutApp(shortcutElementChoose, posTemp);
    }

    public void setPositionOfShortcutAfterDelete ( ShortcutElement shortcutElement, List<ShortcutElement> list ) {
        for (int pos = shortcutElement.pos + 1; pos < list.size(); ++pos){
            list.get(pos).pos = list.get(pos).pos - 1;
            updatePosOfShortcut(list.get(pos), list.get(pos).pos);
        }
    }

    public void setPositionOfShortcutFolderAfterDelete ( ShortcutFolder shortcutFolder, List<ShortcutFolder> list ) {
        for (int pos = shortcutFolder.pos + 1; pos < list.size(); ++pos){
            list.get(pos).pos = list.get(pos).pos - 1;
            updatePosOfShortcutFolder(list.get(pos), list.get(pos).pos);
        }
    }

    public void setPositionOfShortcutAppAfterDelete ( ShortcutApp shortcutApp, List<ShortcutApp> list ) {
        for (int pos = shortcutApp.pos + 1; pos < list.size(); ++pos){
            list.get(pos).pos = list.get(pos).pos - 1;
            updatePosOfShortcutApp(list.get(pos), list.get(pos).pos);
        }
    }

    public void updateColor(Color background, Color backgroundTitle, Color line, Color button) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("update Color set Background = '" + background.toString() + "' " + ", BackgroundTitle = '" + backgroundTitle.toString() + "' " + ", Line = '" + line.toString() + "' " + ", Button = '" + button.toString() + "' where ID = 2")){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        getColor();
    }

    public void getDefaultColor () {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from Color where ID = 1")){
            SingletonColor.singletonColor.setBackground(rs.getString("Background"));
            SingletonColor.singletonColor.setBackgroundTitle(rs.getString("BackgroundTitle"));
            SingletonColor.singletonColor.setLine(rs.getString("Line"));
            SingletonColor.singletonColor.setButton(rs.getString("Button"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        updateColor(Color.valueOf(SingletonColor.singletonColor.getBackground()), Color.valueOf(SingletonColor.singletonColor.getBackgroundTitle()),Color.valueOf(SingletonColor.singletonColor.getLine()),Color.valueOf(SingletonColor.singletonColor.getButton()));

    }

    public void getColor () {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from Color where ID = 2")){
            SingletonColor.singletonColor.setBackground(rs.getString("Background"));
            SingletonColor.singletonColor.setBackgroundTitle(rs.getString("BackgroundTitle"));
            SingletonColor.singletonColor.setLine(rs.getString("Line"));
            SingletonColor.singletonColor.setButton(rs.getString("Button"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFirstColor() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from Color")) {
            if (!rs.next()) {
                String query = "insert into Color (ID) VALUES (null)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)){
                    pstmt.executeUpdate();
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
    }

    public void addFirstScreenValue() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from ScreenPosition")) {
            if (!rs.next()) {
                String query = "insert into ScreenPosition (ID) VALUES (null)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)){
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
    }

    public void updatePosScreen ( int value ) {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement("update ScreenPosition set Value = " + value + " where ID = 1;")){
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getPosScreen () {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from ScreenPosition where ID = 1;")){
            SingletonPosScreen.SingletonPosScreen.setValue(rs.getInt("Value"));
            System.out.println(SingletonPosScreen.SingletonPosScreen.getValue());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createDatabase ( String url ) {
        String shortcut = "CREATE TABLE IF NOT EXISTS \"Shortcut\" (\"ID\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\"Text\" REAL NOT NULL,\"Url\" REAL NOT NULL,\"Pos\" REAL NOT NULL)";
        String shortcutFolder = "CREATE TABLE IF NOT EXISTS \"ShortcutFolder\" (\"ID\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\"Url\" REAL NOT NULL,\"Text\" REAL NOT NULL,\"Pos\" REAL NOT NULL)";
        String shortcutApp = "CREATE TABLE IF NOT EXISTS \"ShortcutApp\" (\"ID\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\"Url\" REAL NOT NULL,\"Text\" REAL NOT NULL,\"Pos\" REAL NOT NULL)";
        String color = "CREATE TABLE IF NOT EXISTS \"Color\" (\"ID\" INTEGER NOT NULL UNIQUE, \"Background\" REAL NOT NULL DEFAULT '0x2c2f33', \"BackgroundTitle\" INTEGER NOT NULL DEFAULT '0x23272a', \"Line\" REAL NOT NULL DEFAULT '0x7289da',\"Button\" REAL NOT NULL DEFAULT '0x7289da', PRIMARY KEY(\"ID\"))";
        String screenPos = "CREATE TABLE IF NOT EXISTS \"ScreenPosition\" (\"ID\" INTEGER NOT NULL UNIQUE, \"Value\" REAL NOT NULL DEFAULT '0', PRIMARY KEY(\"ID\"))";

        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(shortcut);
            stmt.execute(shortcutFolder);
            stmt.execute(shortcutApp);
            stmt.execute(color);
            stmt.execute(screenPos);
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        }
    }


}
