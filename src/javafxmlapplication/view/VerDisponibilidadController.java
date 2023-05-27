/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.view;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import model.Booking;
import model.Club;
import model.ClubDAOException;
import model.Court;
import model.Member;

/**
 * FXML Controller class
 *
 * @author Javier
 */
public class VerDisponibilidadController implements Initializable {

    @FXML
    private ChoiceBox<?> pistas;
    @FXML
    private TextField buscadorNickname;
    @FXML
    private Button aplicarFiltros;
    @FXML
    private TableView<reserva> tableview;
    @FXML
    private TableColumn<reserva, String> pista;
    @FXML
    private TableColumn<reserva, String> hora;
    @FXML
    private TableColumn<reserva, String> member;
    
    private ObservableList<reserva> Reservas = FXCollections.observableList(new ArrayList<reserva>());
    private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    private final Duration slotLength = Duration.ofMinutes(60);
    private final LocalTime lastSlotStart = LocalTime.of(21, 0);
    private List<TimeSlot> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSolt>>
    
    Club club;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            club = Club.getInstance();
        } catch (ClubDAOException ex) {
            Logger.getLogger(VerDisponibilidadController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VerDisponibilidadController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Club club= Club.getInstance(); 
        //==================================
        //Clean the file club.db
        club.setInitialData();
        
        //===================================
        // club data:
        System.out.println("Club name: "+ club.getName());
        for (Court court : club.getCourts()) {
            System.out.println("court:" + court.getName());
        }
        //===================================
        // add simple data:
        club.addSimpleData();
        
        //===================================
        // users        
        for (Member member : club.getMembers()) {
            System.out.println("member:" + member.getName()+ ", "+ member.getNickName());
        }
        
        //===================================
        // bookings now + 2 days
        System.out.println("Bookings after 2 days");
        List<Booking> forDayBookings = club.getForDayBookings(LocalDate.now().plusDays(2));
        for (Booking booking : forDayBookings) {
              System.out.println("booking:" + booking.getMember().getNickName()+
                      ", " + booking.getCourt().getName()+ ", "+
                      booking.getMadeForDay().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) +
                      ", "+booking.getFromTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        }  
        
        timeSlots = new ArrayList<>();
        //club.addSimpleData();
        //----------------------------------------------------------------------------------
        // desde la hora de inicio y hasta la hora de fin creamos slotTime segun la duracion
        LocalDate date = LocalDate.now();
        int slotIndex = 1;
        for (LocalDateTime startTime = date.atTime(firstSlotStart);
                !startTime.isAfter(date.atTime(lastSlotStart));
                startTime = startTime.plus(slotLength)) {

            //---------------------------------------------------------------------------------------
            // creamos el SlotTime, lo anyadimos a la lista de la columna y asignamos sus manejadores
            TimeSlot timeSlot = new TimeSlot(startTime, slotLength);
            timeSlots.add(timeSlot);
            //registerHandlers(timeSlot);
            //-----------------------------------------------------------
            // lo anyadimos al grid en la posicion x= 1, y= slotIndex
            //grid.add(timeSlot.getView(), 1, slotIndex);
            //slotIndex++;
        }
        List<Booking> bo = club.getCourtBookings(club.getCourts().get(0).getName(), LocalDate.now());
        int i = 0;
        int n = 0;
        while(n<bo.size()){
            Booking b = bo.get(n);
            LocalTime t = timeSlots.get(i++).getTime();
            System.out.println("Booking> "+b.getFromTime().toString());
            System.out.println("Localtime> "+t.toString());
            if(b.getFromTime().compareTo(t)==0){
                Reservas.add(new reserva("Pista 1",t,b.getMember().getNickName(),"reservada"));
                n++;
            }else{
                Reservas.add(new reserva("Pista 1",t,"----","no reservada"));
            }
        }
        while(i<timeSlots.size()){
            LocalTime t = timeSlots.get(i++).getTime();
            Reservas.add(new reserva("Pista 1",t,"----","no reservada"));
        }
        tableview.setItems(Reservas);
        pista.setCellValueFactory(new PropertyValueFactory<reserva, String>("pista"));
        hora.setCellValueFactory(new PropertyValueFactory<reserva, String>("hora"));
        member.setCellValueFactory(new PropertyValueFactory<reserva, String>("miembro"));
    }    

    @FXML
    private void aplicarFiltrosAction(ActionEvent event) {
    }
    public class TimeSlot {

        private final LocalDateTime start;
        private final Duration duration;

        public TimeSlot(LocalDateTime start, Duration duration) {
            this.start = start;
            this.duration = duration;
        }

        public LocalDateTime getStart() {
            return start;
        }

        public LocalTime getTime() {
            return start.toLocalTime();
        }

        public LocalDate getDate() {
            return start.toLocalDate();
        }

        public DayOfWeek getDayOfWeek() {
            return start.getDayOfWeek();
        }

        public Duration getDuration() {
            return duration;
        }

    }
    
}
