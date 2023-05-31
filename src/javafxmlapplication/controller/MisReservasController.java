/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import java.io.IOException;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Booking;
import model.Member;
import utils.Usuario;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Club;
import model.ClubDAOException;
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
    
    private LocalDate diaReservado;
    private LocalDate diaReserva2;

    Member miembro;
    Club club;
    
    long dato = 1;
    
    private ObservableList<Booking> Bookings = FXCollections.observableList(new ArrayList<Booking>());
    private List<Booking> listBooking = new ArrayList<>(); 
    @FXML
    private ImageView imagen1;
    @FXML
    private Button pagar;
    @FXML
    private Button eliminar;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        miembro = Usuario.getInstancia().getUsuario();
        pagar.disableProperty().bind(
        Bindings.equal(-1,
                tableview.getSelectionModel().selectedIndexProperty()));
        
        eliminar.disableProperty().bind(
        Bindings.equal(-1,
                tableview.getSelectionModel().selectedIndexProperty()));
        
        try {
            club = Club.getInstance();
        } catch (ClubDAOException ex) {
            Logger.getLogger(MisReservasController.class.getName()).log(Level.SEVERE, null, ex);    
        } catch (IOException ex) {
            Logger.getLogger(MisReservasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        listBooking = club.getUserBookings(miembro.getNickName());
        ActualizarTabla(listBooking);
        DayBooked.setCellValueFactory(cellData -> {
            Booking item = cellData.getValue();
            String day = item.getBookingDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return new SimpleStringProperty(day);
        });
        ReservedDay.setCellValueFactory(cellData -> {
            Booking item = cellData.getValue();
            String Rday = item.getMadeForDay().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return new SimpleStringProperty(Rday);
        });
        hora.setCellValueFactory(cellData -> {
            Booking item = cellData.getValue();
            String hora = item.getFromTime().toString()+"-"+item.getFromTime().plusHours(1).toString();
            return new SimpleStringProperty(hora);
        });
        pista.setCellValueFactory(cellData -> {
            Booking item = cellData.getValue();
            String court = item.getCourt().getName();
            return new SimpleStringProperty(court);
        });
         Paid.setCellValueFactory(cellData -> {
            Booking item = cellData.getValue();
            boolean pagado = item.getPaid();
            String res;
            if(pagado){res = "pagado";}
            else{res = "no pagado";}
            return new SimpleStringProperty(res);
        });
        
    }    
    
    public void ActualizarTabla(List<Booking> LB){
        Bookings.clear();
        diaReserva2 = LocalDate.now();
        int i = 0;
        int contador = 0;
        //Que salgan las 10 reservas más cercanas a terminar
        while(contador<10 && i<LB.size()){
            Booking b = LB.get(i++);
            int dato = b.getMadeForDay().compareTo(LocalDate.now());
            if(dato>0){ // SI es para un día mayor al actual
                Bookings.add(b);
                contador++;
            //Si es para hoy, la hora tiene que ser mayor o igual a la actual
            }else if(dato==0 && b.getFromTime().compareTo(LocalTime.now().minusHours(1))>=0){
                Bookings.add(b);
                contador++;
            }
        }
        
        tableview.setItems(Bookings);
    }
    
    @FXML
    private void pagarReserva(ActionEvent event) throws IOException {
       if(!tableview.getFocusModel().getFocusedItem().getPaid()){
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
                       
                        if(!miembro.checkHasCreditInfo()){
                        FXMLLoader miCargador = new FXMLLoader(getClass().getResource("/javafxmlapplication/view/tarjetaCredito.fxml"));
                        Stage stage = new Stage();
                        
                        Parent root = miCargador.load();
                        Scene scene = new Scene(root, 500, 300);
                        stage.setScene(scene);
                        TarjetaCreditoController t = miCargador.getController();
                        t.booking(tableview.getFocusModel().getFocusedItem());
                        stage.setTitle("Introducir Tarjeta");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(false);
                        stage.setHeight(262);
                        stage.setWidth(480);
                        stage.showAndWait();
                    } else {tableview.getFocusModel().getFocusedItem().setPaid(true);}
                listBooking = club.getUserBookings(miembro.getNickName());
                ActualizarTabla(listBooking);
                }
                if(result.get() == buttonTypeTwo){
                    System.out.println("Pagar más tarde");
                }
            } }else{
           Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error confirmacion de pago");
            alert.setHeaderText(null);
            alert.setContentText("la reserva ya esta pagada");
            alert.showAndWait();
       }
       }
    

    @FXML
    private void cancelarReserva(ActionEvent event) throws ClubDAOException {
            Booking selectedItem = tableview.getSelectionModel().getSelectedItem();
            diaReservado = selectedItem.getMadeForDay();
            diaReserva2 = LocalDate.now();
            long diferenciasEnDias = Math.abs(ChronoUnit.DAYS.between(diaReservado,diaReserva2));
            System.out.println(diaReservado.toString());
            System.out.println(diaReserva2.toString());
            System.out.println(diferenciasEnDias);
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
                  if(diferenciasEnDias > dato) {  
                    tableview.getItems().remove(selectedItem);
                    club.removeBooking(selectedItem);
                    ActualizarTabla(club.getUserBookings(miembro.getNickName()));
                    Alert alert2 = new Alert(AlertType.INFORMATION);
                    alert2.setTitle("Cancelar Reserva");
                    alert2.setHeaderText(null);
                    alert2.setContentText("La reserva se ha cancelado correctamente");
                    alert2.showAndWait();
                  } else {
                    Alert alert4 = new Alert(AlertType.WARNING);
                    alert4.setTitle("Advertencia");
                    alert4.setHeaderText(null);
                    alert4.setContentText("No se puede cancelar la reserva ya que quedan menos de 24 horas para su uso");
                    alert4.showAndWait();
                  }
                }
                if(result.get() == buttonTypeOne){
                    System.out.println("Cancelado");
                }
            }
        }  
    
}
