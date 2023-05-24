/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Booking;
import model.Court;

/**
 * FXML Controller class
 *
 * @author Cristian
 */
public class MisReservasController implements Initializable {

    @FXML
    private TableView<Booking> tableview;
    @FXML
    private TableColumn<Booking, String> DayBooked;
    @FXML
    private TableColumn<Booking, String> ReservedDay;
    @FXML
    private TableColumn<Booking, String> hora;
    @FXML
    private TableColumn<Booking, String> Paid;
    @FXML
    private TableColumn<Booking, String> pista;
    @FXML
    private TableColumn<Booking, String> member;
    @FXML
    private Button pagar;
    @FXML
    private Button eliminar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DayBooked.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getBookingDate().toString()));
        hora.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getFromTime().toString()));
        ReservedDay.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getMadeForDay().toString()));
        member.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getMember().toString()));
        pista.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getCourt().toString()));
        Paid.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getPaid().toString()));
        ReservedDay.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getMadeForDay().toString()));
    }    
    
}
