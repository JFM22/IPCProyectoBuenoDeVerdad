/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.view;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import model.Booking;
import model.Club;

/**
 * FXML Controller class
 *
 * @author Cristian
 */
public class Fecha2Controller implements Initializable {

    @FXML
    private ComboBox<String> Combo;
    @FXML
    private DatePicker dpBooking;
    @FXML
    private ListView<String> ListView;
    private Club club;
    String[] Horas = {"9:00-10:00","10:00-11:00","11:00-12:00","12:00-13:00","13:00-14:00","14:00-15:00","15:00-16:00"
    ,"16:00-17:00","17:00-18:00","18:00-19:00","19:00-20:00","20:00-21:00","21:00-22:00"};
    String horario;
    @FXML
    private Button ReservarButton;
    
    /**
     * Initializes the controller class.
     */
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Combo.getItems().addAll("Pista 1","Pista 2", "Pista 3","Pista 4", "Pista 5", "Pista 6");
        Combo.setValue("Pista 1");
        dpBooking.setDayCellFactory((DatePicker picker)->{
        return new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        LocalDate today = LocalDate.now();
        setDisable(empty || date.compareTo(today) < 0 );
        }
       };
    });
       
        
        dpBooking.setValue(LocalDate.now());
        
        ListView.getItems().addAll(Horas);
        
         dpBooking.setOnAction(event -> updateListView());
        Combo.setOnAction(event -> updateListView());
         //ListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Habilitar o deshabilitar el botón de reserva según si la hora está reservada
            //boolean horaReservada = estaReservada(newValue);});
    //}  ;  
    }
    private void updateListView(){
         String pistaSeleccionada = Combo.getValue();
        LocalDate fechaSeleccionada =dpBooking.getValue();

        if (pistaSeleccionada != null &&  fechaSeleccionada!= null) {
            // Obtener las horas reservadas para la pista y fecha seleccionadas
            List<Booking> reservadas=club.getCourtBookings(pistaSeleccionada, fechaSeleccionada);
            
            // Llama a tu método existente o consulta la base de datos aquí para obtener las horas reservadas
            // Por ejemplo, puedes tener un método "getReservedHours" en tu clase Club
            // que reciba el número de pista y la fecha y devuelva las horas reservadas.
            // ArrayList<String> reservedHours = club.getReservedHours(selectedCourtNumber, selectedDate);
            // Aquí, reemplaza "reservedHours" con el ArrayList real obtenido

            // Limpiar la lista actual
            ListView.getItems().clear();

            // Agregar todas las horas disponibles a la lista
            for (String hour : Horas) {
                // Verificar si la hora está reservada
                // Agregar la hora a la lista y establecer el estilo si está reservada
                for(Booking booking: reservadas){
                if(hour.equals(booking.getFromTime().toString() + " - " + booking.getFromTime().plusHours(1).toString())){
                    
                }else{
                ListView.getItems().add(hour);
                }
                    
                //if(booking)){
                   
               // }
                
                }
                
            }
        }
    
    }
        
    
    @FXML
    private void ReservarClicked(ActionEvent event) {
    }
}