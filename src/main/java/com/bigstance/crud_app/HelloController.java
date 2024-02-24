package com.bigstance.crud_app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.IOException;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField yearField;
    @FXML
    private TableView<Books> viewTable;
    @FXML
    private TableColumn<Books,Integer> colId;
    @FXML
    private TableColumn<Books,String> colName;
    @FXML
    private TableColumn<Books, Integer> colYear;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;


    @FXML
    private void HandleButtonAction(ActionEvent event) {
        Books book = viewTable.getSelectionModel().getSelectedItem();
        if(event.getSource() == btnAdd) {
            insertRecord();
        } else if(event.getSource() == btnDelete) {
            deleteRecord(book.getId());
        } else if(event.getSource() == btnUpdate) {
            System.out.println(book.getId());
            updateRecord(book.getId());
        }
     }



    @FXML
    private void HandleMouseAction(MouseEvent event) {
        Books book = viewTable.getSelectionModel().getSelectedItem();
        nameField.setText(book.getName());
        yearField.setText(Integer.toString(book.getYear()));
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showBooks();
    }

    public Connection getConnection(String user, String password) {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", user, password);

            if (conn != null) {
                System.out.println("Connection established");

            } else {
                System.out.println("Connection is not established");
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;

        }
        return conn;
    }
    public ObservableList<Books> getBooksList(){
        ObservableList<Books> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection("root", "root");
        String query = "SELECT * FROM books";
        Statement st;
        ResultSet rs;

        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Books books;
            while(rs.next()) {
                books = new Books(rs.getInt("id"), rs.getString("name"), rs.getInt("year"));
                bookList.add(books);
            }
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            return bookList;
         }

         public void showBooks(){
            ObservableList<Books> list = getBooksList();
             colId.setCellValueFactory(new PropertyValueFactory<Books, Integer>("id"));
             colName.setCellValueFactory(new PropertyValueFactory<Books, String>("name"));
             colYear.setCellValueFactory(new PropertyValueFactory<Books, Integer>("year"));

             viewTable.setItems(list);

    }

    public void insertRecord() {
        String query = "INSERT INTO books(name, year) VALUES ('" + nameField.getText() + "','" + yearField.getText() + "')";
        executeQuery(query);
        showBooks();
        clearRecord("insert");
    }

    public void executeQuery(String query) {
        Connection conn = getConnection("root", "root");
        Statement st;

        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }


    public void updateRecord(Integer id) {
        String query = "UPDATE books SET name='" + nameField.getText() + "', year= '" + yearField.getText() + "' WHERE id= '" + id + "'";
        executeQuery(query);
        showBooks();
        clearRecord("insert");
    }

    public void deleteRecord(Integer id) {
        String query = "DELETE from books WHERE id = '" + id + "'";
        executeQuery(query);
        showBooks();
        clearRecord("insert");
    }

    public void clearRecord(String fieldName) {

        if (Objects.equals(fieldName, "insert")) {
            nameField.clear();
            yearField.clear();
        }
    }


}