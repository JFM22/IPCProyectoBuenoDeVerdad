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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private TableColumn<Booking, String> member;
    @FXML
    private Button pagar;
    @FXML
    private Button eliminar;

    private boolean estaPagada = false;
    
    private LocalDateTime diaReserva;
    
    private LocalDate diaReservado;
    
    private LocalDate diaReserva2;

    Member miembro;
    Club club;
    
    long dato = 1;
    
    private ObservableList<Booking> Bookings = FXCollections.observableList(new ArrayList<Booking>());
    private List<Booking> listBooking = new ArrayList<>(); 
    
    @FXML
    private Label Label;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        tableview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        miembro = Usuario.getInstancia().getUsuario();
        Label.visibleProperty().bind(Bindings.not(new SimpleBooleanProperty(miembro.checkHasCreditInfo())));
        
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
            String hora = item.getFromTime().toString();
            return new SimpleStringProperty(hora);
        });
        pista.setCellValueFactory(cellData -> {
            Booking item = cellData.getValue();
            String court = item.getCourt().getName().substring(5);
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
          member.setCellValueFactory(cellData -> {
            Booking item = cellData.getValue();
            String mem1 = item.getMember().getNickName();
            return new SimpleStringProperty(mem1);
        });


//        hora.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getFromTime().toString()));
//        ReservedDay.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getMadeForDay().toString()));
//        member.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getMember().toString()));
//        pista.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getCourt().toString()));
//        Paid.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getPaid().toString()));
//        ReservedDay.setCellValueFactory(Booking->new SimpleStringProperty(Booking.getValue().getMadeForDay().toString()));
//       
    }    
    
    public void ActualizarTabla(List<Booking> LB){
        Bookings.clear();
        diaReserva2 = LocalDate.now();
        int i = LB.size();
        if (i>10){
            i = i-10;
        }else{
            i=0;
        }
        while(i<LB.size()){
            Booking b = LB.get(i++);
            if(!(b.getMadeForDay().compareTo(LocalDate.now())<0)){
                Bookings.add(b);
            }
            //if(b.getMadeForDay().compareTo(diaReserva2)>=0){Bookings.add(b);}
        }
        
        tableview.setItems(Bookings);
    }
    
    @FXML
    private void pagarReserva(ActionEvent event) throws IOException {
       //if(miembro.checkHasCreditInfo()==false){
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
                        //VistaPersonaController controlPersona = miCargador.getController();
                        //Persona persona = personasListView.getSelectionModel().getSelectedItem();
                        //controlPersona.initPersona(persona);
                        Scene scene = new Scene(root, 500, 300);
                        stage.setScene(scene);
                        TarjetaCreditoController t = miCargador.getController();
                        t.booking(tableview.getFocusModel().getFocusedItem());
                        stage.setTitle("Introducir Tarjeta");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();
                    } else {tableview.getFocusModel().getFocusedItem().setPaid(true);}
                listBooking = club.getUserBookings(miembro.getNickName());
                ActualizarTabla(listBooking);
                   // Alert alert3 = new Alert(AlertType.INFORMATION);
                   // alert3.setTitle("Pago de la reserva");
                   // alert3.setHeaderText(null);
                   // alert3.setContentText("El pago de la reserva ha sido realizado correctamente");
                   // alert3.showAndWait();
                }
                if(result.get() == buttonTypeTwo){
                    System.out.println("Pagar más tarde");
                }
            } 
       }
    

    @FXML
    private void cancelarReserva(ActionEvent event) throws ClubDAOException {
            Booking selectedItem = tableview.getSelectionModel().getSelectedItem();
            diaReserva = selectedItem.getBookingDate();
            diaReservado = selectedItem.getMadeForDay();
            diaReserva2 = LocalDate.now();
            //int DifH = diaReservado.compareTo(HoraReserva);
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
