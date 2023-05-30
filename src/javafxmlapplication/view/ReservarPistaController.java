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
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Booking;
import model.Club;
import model.ClubDAOException;
import model.Court;
import model.Member;
import utils.Usuario;

/**
 * FXML Controller class
 *
 * @author Usuario
 */public class ReservarPistaController implements Initializable {

    @FXML
    private DatePicker dpBooking;
    @FXML
    private ComboBox<String> Combo;
    @FXML
    private Button ReservarButton;
    @FXML
    private Label slotSelected;
   
    private Club club;
    String[] Horas = {"09:00-10:00","10:00-11:00","11:00-12:00","12:00-13:00","13:00-14:00","14:00-15:00","15:00-16:00"
    ,"16:00-17:00","17:00-18:00","18:00-19:00","19:00-20:00","20:00-21:00","21:00-22:00"};
    String horario;
    Booking reserva;
    Member member;
    Usuario user;
    Court pistaSeleccionada=new Court();
    LocalDateTime fechaHoy;
    LocalTime horaInicio;
    boolean paid=false;
    TimeSlot aux;
    private ObservableList<String> datosLista=null;
    private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    private final Duration slotLength = Duration.ofMinutes(60);
    private final LocalTime lastSlotStart = LocalTime.of(21, 0);
    @FXML
    private TableView<reserva> tableview;
    @FXML
    private TableColumn<reserva, String> Hora;
    @FXML
    private TableColumn<reserva, String> Disponibilidad;
    private List<String> pistas = new ArrayList<>();
    private List<TimeSlot> timeSlots;
        private ObservableList<reserva> Reservas = FXCollections.observableList(new ArrayList<reserva>());
    @FXML
    private TableColumn<reserva, String> NickName;
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        //user = Usuario.getInstancia();
        try {
            
            club = Club.getInstance();
        } catch (ClubDAOException ex) {
            Logger.getLogger(ReservarPistaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReservarPistaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        member=Usuario.getInstancia().getUsuario();

        //Member u = club.getMemberByCredentials("vege", "7777777");
        //user.setUsuario(member);
        //member = user.getUsuario();
        // TODO
         for(Court c : club.getCourts()){pistas.add(c.getName());}
        Combo.getItems().addAll(pistas);
        Combo.setValue(pistas.get(0));
        pistaSeleccionada.setName(Combo.getValue());
        
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
        //ListView.getItems().addAll(Horas);
         
         //dpBooking.setOnAction(event -> updateListView());
         dpBooking.valueProperty().addListener((a, b, c)->{
             updateTableView(c,pistaSeleccionada);
              
         });
         Combo.valueProperty().addListener((a, b, c)->{
             pistaSeleccionada.setName(c);
             updateTableView(dpBooking.getValue(),pistaSeleccionada);
         });
          timeSlots = new ArrayList<>();
        //club.addSimpleData();
        //----------------------------------------------------------------------------------
        // desde la hora de inicio y hasta la hora de fin creamos slotTime segun la duracion
        LocalDate date = dpBooking.getValue();
        int slotIndex = 1;
        for (LocalDateTime startTime = date.atTime(firstSlotStart);
                !startTime.isAfter(date.atTime(lastSlotStart));
                startTime = startTime.plus(slotLength)) {

            //---------------------------------------------------------------------------------------
            // creamos el SlotTime, lo anyadimos a la lista de la columna y asignamos sus manejadores
        TimeSlot timeSlot = new TimeSlot(startTime, slotLength);
        timeSlots.add(timeSlot);
  
        }
        updateTableView(dpBooking.getValue(),pistaSeleccionada);
        Hora.setCellValueFactory(new PropertyValueFactory<reserva, String>("hora"));
        Disponibilidad.setCellValueFactory(new PropertyValueFactory<reserva, String>("disponibilidad"));
         NickName.setCellValueFactory(new PropertyValueFactory<reserva, String>("miembro"));

    }    
        private void updateTableView(LocalDate date,Court pista){
            Reservas.clear();
            List<Booking> bookOcupadas = club.getCourtBookings(pista.getName(), date);
            int n = 0;
            int i=0;
            while(n<bookOcupadas.size()){
                    Booking b = bookOcupadas.get(n);
                    LocalTime t = timeSlots.get(i++).getTime();
                    if(b.getFromTime().compareTo(t)==0){
                       if(b.getMember().getNickName().equals(member.getName())){
                       Reservas.add(new reserva(pista.getName(),t,b.getMember().getNickName(),"reservada por ti",b));

                       } 
                       else{Reservas.add(new reserva(pista.getName(),t,b.getMember().getNickName(),"reservada",b));}
                        n++;
                    }else{
                        Reservas.add(new reserva(pista.getName(),t,"----","libre",null));
                    }
                    
            }
             while(i<timeSlots.size()){
                    LocalTime t = timeSlots.get(i++).getTime();
                    Reservas.add(new reserva(pista.getName(),t,"----","libre",null));
                }
           tableview.setItems(Reservas); 
 
            
        }
         

    @FXML
    private void ReservarClicked(ActionEvent event) {
        
        reserva reservaSeleccionada =tableview.getSelectionModel().getSelectedItem();
        int indiceReserva =tableview.getSelectionModel().getSelectedIndex();
        tableview.getItems().get(indiceReserva);
        Booking bookSeleccionada= reservaSeleccionada.getBooking();
        if(bookSeleccionada==null){
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("SlotTime");
                alerta.setHeaderText("Confirma la selección");
                alerta.setContentText("Has seleccionado: "
                        + dpBooking.getValue().toString() + ", "
                        +reservaSeleccionada.getHora());//fin de mensaje
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                //-------------------------------------------------------------------------------------
                LocalDate fechaSeleccionada =dpBooking.getValue();
                horaInicio=LocalTime.parse(reservaSeleccionada.getHora());
                pistaSeleccionada.setName(Combo.getValue());
                try{
                
                club.registerBooking(LocalDateTime.now(),fechaSeleccionada,horaInicio,paid,pistaSeleccionada,member); 
                updateTableView(dpBooking.getValue(),pistaSeleccionada);
                System.out.println("libre y registrada crack");
                

                }catch(ClubDAOException e){
                System.out.println("esta reservado maquina");
                
                    }
                    } else {//ya esta pillada
                        System.out.println("reservada");
                        
                    }
        }   else{System.out.println("Hola");}
        
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