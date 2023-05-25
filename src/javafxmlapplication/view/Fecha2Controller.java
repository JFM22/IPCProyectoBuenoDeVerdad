/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.view;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Booking;
import model.Court;
import model.Member;
import utils.Usuario;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class Fecha2Controller implements Initializable {

    @FXML
    private DatePicker dpBooking;
    @FXML
    private ComboBox<String> Combo;
    @FXML
    private GridPane grid;
    @FXML
    private Label labelCol;
    @FXML
    private Button ReservarButton;
    Booking reserva;
    Member member;
    Usuario user;
    Court pista = new Court();
    LocalDateTime fechaHoy;
    LocalTime horaInicio;
    boolean paid=false;
    //es del ejemplo
    
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Combo.getItems().addAll("Pista 1","Pista 2", "Pista 3","Pista 4", "Pista 5", "Pista 6");
        Combo.setValue("Pista 1");
        
        
    }    

    @FXML
    private void ReservarClicked(ActionEvent event) {
    
    }
    
}
