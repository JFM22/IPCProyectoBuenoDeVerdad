/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import model.Booking;
import model.Court;
import model.Member;
import utils.Usuario;
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

    private boolean estaPagada = false;

    Member miembro;
    
    
    @FXML
    private Label Label;
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
        miembro = Usuario.getInstancia().getUsuario();
    }    
    
    public boolean Pagada(){
        return estaPagada; 
    }

    @FXML
    private void pagarReserva(ActionEvent event) {
        //if(miembro.checkHasCreditInfo()== false){pagar.setDisable(true);}
        if(!estaPagada){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Hacer el pago de la pista");
            alert.setHeaderText("Tienes el pago de la reserva pendiente");
            alert.setContentText("¿Quieres pagar ahora? ");
            ButtonType buttonTypeOne = new ButtonType("Pagar Ahora");
            ButtonType buttonTypeTwo = new ButtonType("Pagar más tarde");
            alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()){
                if(result.get() == buttonTypeOne){
                    Alert alert3 = new Alert(AlertType.INFORMATION);
                    alert3.setTitle("Pago de la reserva");
                    alert3.setHeaderText(null);
                    alert3.setContentText("El pago de la reserva ha sido realizado correctamente");
                    alert3.showAndWait();
                }
                if(result.get() == buttonTypeTwo){
                    System.out.println("Pagar más tarde");
                }
            } 
        }
    }
    @FXML
    private void cancelarReserva(ActionEvent event) {
            Booking selectedItem = tableview.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Cancelar Reserva");
            alert.setHeaderText("Quieres cancelar esta reserva");
            alert.setContentText("¿Estas seguro de que lo quieres hacer? ");
            ButtonType buttonTypeOne = new ButtonType("Cancelar");
            ButtonType buttonTypeTwo = new ButtonType("Aceptar");
            alert.getButtonTypes().setAll(buttonTypeTwo,buttonTypeOne);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()){
                if(result.get() == buttonTypeTwo){
                    tableview.getItems().remove(selectedItem);
                    Alert alert2 = new Alert(AlertType.INFORMATION);
                    alert2.setTitle("Cancelar Reserva");
                    alert2.setHeaderText(null);
                    alert2.setContentText("La reserva se ha cancelado correctamente");
                    alert2.showAndWait();
                }
                if(result.get() == buttonTypeOne){
                    System.out.println("Cancelado");
                }
            }
            
    }

    @FXML
    private void LabelCondition(MouseEvent event) {
        if(miembro.checkHasCreditInfo()== false){Label.setVisible(false);}
        else{Label.setVisible(true); 
        }
    }
    
}
