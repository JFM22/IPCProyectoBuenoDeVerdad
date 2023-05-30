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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import model.Booking;
import model.Club;
import model.ClubDAOException;
import model.Court;

/**
 * FXML Controller class
 *
 * @author Javier
 */
public class VerDisponibilidadControllerR2 implements Initializable {

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
    @FXML
    private TableColumn<reserva, String> disponibilidad;
    
    private List<String> pistas = new ArrayList<>();
    
    private ObservableList<reserva> Reservas = FXCollections.observableList(new ArrayList<reserva>());
    private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    private final Duration slotLength = Duration.ofMinutes(60);
    private final LocalTime lastSlotStart = LocalTime.of(21, 0);
    private List<TimeSlot> timeSlots; //Para varias columnas List<List<TimeSolt>>
    
    Club club;
    @FXML
    private ComboBox<String> combo;
    @FXML
    private ImageView imagen1;
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
        combo.getItems().add("TODAS");
        for(Court c : club.getCourts()){pistas.add(c.getName());}
        combo.getItems().addAll(pistas);
        combo.setValue("TODAS");
        
        buscadorNickname.textProperty().addListener((a,oldV,newV)->{
            if(newV.contains(" ")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error en formato");
                alert.setHeaderText("El nickname no puede contener espacios");
                alert.showAndWait();
                buscadorNickname.setText(oldV);
            }
        });
        //Club club= Club.getInstance(); 
        //==================================
        //Clean the file club.db
        //club.setInitialData();
        
        //===================================
        // club data:
//        System.out.println("Club name: "+ club.getName());
//        for (Court court : club.getCourts()) {
//            System.out.println("court:" + court.getName());
//        }
        //===================================
        // add simple data:
        //club.addSimpleData();
        
        //===================================
        // users        
//        for (Member member : club.getMembers()) {
//            System.out.println("member:" + member.getName()+ ", "+ member.getNickName());
//        }
        
        //===================================
        // bookings now + 2 days
//        System.out.println("Bookings after 2 days");
//        List<Booking> forDayBookings = club.getForDayBookings(LocalDate.now().plusDays(0));
//        for (Booking booking : forDayBookings) {
//              System.out.println("booking:" + booking.getMember().getNickName()+
//                      ", " + booking.getCourt().getName()+ ", "+
//                      booking.getMadeForDay().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) +
//                      ", "+booking.getFromTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
//        }  
//        
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
            
        }
        updateTable(pistas, buscadorNickname.getText());
        pista.setCellValueFactory(new PropertyValueFactory<reserva, String>("pista"));
        hora.setCellValueFactory(new PropertyValueFactory<reserva, String>("hora"));
        member.setCellValueFactory(new PropertyValueFactory<reserva, String>("miembro"));
        disponibilidad.setCellValueFactory(new PropertyValueFactory<reserva, String>("disponibilidad"));
    }  
    public void updateTable(List<String> pistas, String nombre){
        Reservas.clear();
        for(String c : pistas){
            int i = 0;
            int n = 0;
            List<Booking> bo = club.getCourtBookings(c, LocalDate.now());
            if(nombre.equals("")){
                while(n<bo.size()){
                    Booking b = bo.get(n);
                    LocalTime t = timeSlots.get(i++).getTime();
                    if(b.getFromTime().compareTo(t)==0){
                        Reservas.add(new reserva(c,t,b.getMember().getNickName(),"reservada",b));
                        n++;
                    }else{
                        Reservas.add(new reserva(c,t,"----","libre",null));
                    }
                }
                while(i<timeSlots.size()){
                    LocalTime t = timeSlots.get(i++).getTime();
                    Reservas.add(new reserva(c,t,"----","libre",null));
                }
            }else{
                while(n<bo.size() && i<timeSlots.size()){
                    Booking b = bo.get(n++);
                    LocalTime t = timeSlots.get(i++).getTime();
                    if(b.getMember().getNickName().equals(nombre)){
                        Reservas.add(new reserva(c,t,b.getMember().getNickName(),"reservada",b));
                    }
                }
            }
        }
        
        tableview.setItems(Reservas);
    }

    @FXML
    private void aplicarFiltrosAction(ActionEvent event) {
        String value = combo.getValue();
        if(value.equals("TODAS")){
            updateTable(pistas, buscadorNickname.getText());
        }else{
            updateTable(Arrays.asList(combo.getValue()),buscadorNickname.getText());
        }
    }

    @FXML
    private void combo_action(ActionEvent event) {
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