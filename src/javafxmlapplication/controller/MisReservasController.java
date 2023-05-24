/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Cristian
 */
public class MisReservasController implements Initializable {

    @FXML
    private TableView<?> tableview;
    @FXML
    private TableColumn<?, ?> ReservedDay;
    @FXML
    private TableColumn<?, ?> DayBooked;
    @FXML
    private TableColumn<?, ?> hora;
    @FXML
    private TableColumn<?, ?> Paid;
    @FXML
    private TableColumn<?, ?> pista;
    @FXML
    private TableColumn<?, ?> member;
    @FXML
    private Button pagar;
    @FXML
    private Button eliminar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
