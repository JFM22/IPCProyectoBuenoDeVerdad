/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import utils.reserva;
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
import javafx.scene.image.ImageView;
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
    @FXML
    private ImageView imagen1;
    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        user = Usuario.getInstancia();
        try {
            
            club = Club.getInstance();
        } catch (ClubDAOException ex) {
            Logger.getLogger(ReservarPistaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReservarPistaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        member=Usuario.getInstancia().getUsuario();
        
        //Member u = club.getMemberByCredentials("vege", "7777777");
        //user.setUsuario(u);
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
             updateTableView();
              
         });
         //Combo.valueProperty().addListener((a, b, c)->{
            

        // });
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
        updateTableView();
        Hora.setCellValueFactory(new PropertyValueFactory<reserva, String>("hora"));
        Disponibilidad.setCellValueFactory(new PropertyValueFactory<reserva, String>("disponibilidad"));
         NickName.setCellValueFactory(new PropertyValueFactory<reserva, String>("miembro"));

    }    
        private void updateTableView(){
            LocalDate date=dpBooking.getValue();
            Reservas.clear();
           
            List<Booking> bookOcupadas = club.getCourtBookings(Combo.getValue(), date);
            int n = 0;
            int i=0;
            while(n<bookOcupadas.size()){
                    Booking b = bookOcupadas.get(n);
                    LocalTime t = timeSlots.get(i++).getTime();
                    if(b.getFromTime().compareTo(t)==0){
                       if(b.getMember().getNickName().equals(member.getName())){
                       Reservas.add(new reserva(Combo.getValue(),t,b.getMember().getNickName(),"reservada por ti",b));

                       } 
                       else{Reservas.add(new reserva(Combo.getValue(),t,b.getMember().getNickName(),"reservada",b));}
                        n++;
                    }else{
                        Reservas.add(new reserva(Combo.getValue(),t,"----","libre",null));
                    }
                    
            }
             while(i<timeSlots.size()){
                    LocalTime t = timeSlots.get(i++).getTime();
                    Reservas.add(new reserva(Combo.getValue(),t,"----","libre",null));
                }
           tableview.setItems(Reservas); 
 
            
        }
         

    @FXML
    private void ReservarClicked(ActionEvent event) {
        
        reserva reservaSeleccionada =tableview.getSelectionModel().getSelectedItem();
        int indiceReserva =tableview.getSelectionModel().getSelectedIndex();
        
        //tableview.getItems().get(indiceReserva);
        Booking bookSeleccionada= reservaSeleccionada.getBooking();
        //Booking bookSeleccionada= reservaSeleccionada.getBooking();//esto me indica si está disponible
        String miNombre=member.getNickName();
        int caso=0;//caso=1 no puedes reservar mas de dos horas seguidas
                    
            
        Boolean esPosible=true;

        if(indiceReserva>=2 && indiceReserva<=Reservas.size()-3){
        
        reserva previo1=Reservas.get(indiceReserva-1);
        reserva previo2=Reservas.get(indiceReserva-2);
        Booking bookPre1= previo1.getBooking();
        Booking bookPre2= previo2.getBooking();
        reserva posterior1=Reservas.get(indiceReserva+1);
        reserva posterior2=Reservas.get(indiceReserva+2);
        Booking bookPos1= posterior1.getBooking();
        Booking bookPos2= posterior2.getBooking();
        //te comprueba con los anteriores esta bien
        if(bookPre1!=null && miNombre.equals(previo1.getMiembro())){
            if(bookPre2!=null && miNombre.equals(previo2.getMiembro())){
            esPosible=false;caso=1;
            }else if (bookPos1!=null && miNombre.equals(posterior1.getMiembro())){esPosible=false;caso=1;}else{esPosible=true;}
        }
        //te comprueba con los siguientes esta bien
        if(bookPos1!=null && miNombre.equals(posterior1.getMiembro())){
            if(bookPos2!=null && miNombre.equals(posterior2.getMiembro())){
            esPosible=false;caso=1;
            }else if (bookPre1!=null && miNombre.equals(previo1.getMiembro())){esPosible=false;caso=1;}else{esPosible=true;}
        }
        }
        else {
            if(indiceReserva<2){
                reserva posterior1=Reservas.get(indiceReserva+1);
            reserva posterior2=Reservas.get(indiceReserva+2);
            Booking bookPos1= posterior1.getBooking();
            Booking bookPos2= posterior2.getBooking();
            if (indiceReserva==0){
            if(bookPos1!=null && miNombre.equals(posterior1.getMiembro())){
            
            if(bookPos2!=null && miNombre.equals(posterior2.getMiembro())){
            esPosible=false;caso=1;
            }else{
            esPosible=true;
            }}}
            if(indiceReserva==1){
            reserva previo1=Reservas.get(indiceReserva-1);
            Booking bookPre1= previo1.getBooking();
            if(bookPos1!=null && miNombre.equals(posterior1.getMiembro())){
            if(bookPos2!=null && miNombre.equals(posterior2.getMiembro())){
            esPosible=false;caso=1;
            }else if (bookPre1!=null && miNombre.equals(previo1.getMiembro())){
            esPosible=false;caso=1;
            }else{
            esPosible=true;
            }}}
            }
            if(indiceReserva>Reservas.size()-3){
            reserva previo1=Reservas.get(indiceReserva-1);
        reserva previo2=Reservas.get(indiceReserva-2);
        Booking bookPre1= previo1.getBooking();
        Booking bookPre2= previo2.getBooking();
        
        if(indiceReserva==Reservas.size()-2){
            
            reserva posterior1=Reservas.get(indiceReserva+1);
            Booking bookPos1= posterior1.getBooking();
                if(bookPre1!=null && miNombre.equals(previo1.getMiembro())){
            if(bookPre2!=null && miNombre.equals(previo2.getMiembro())){
            esPosible=false;caso=1;
            }else if (bookPos1!=null && miNombre.equals(posterior1.getMiembro())){esPosible=false;caso=1;}else{esPosible=true;}
        }
            }
            if(indiceReserva==Reservas.size()-1){
        
        if(bookPre1!=null && miNombre.equals(previo1.getMiembro())){
            if(bookPre2!=null && miNombre.equals(previo2.getMiembro())){
            esPosible=false;caso=1;
            }else{esPosible=true;}
        }}}

        }
        //----------------------------------------------------------------------
        if(bookSeleccionada==null&&esPosible){
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("SlotTime");
                alerta.setHeaderText("Confirma la selección");
                alerta.setContentText("Has seleccionado: "
                        + dpBooking.getValue().toString() + ", "
                        +reservaSeleccionada.getHora());//fin de mensaje
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if(member.checkHasCreditInfo()==true){
                    //-------------------------------------------------------------------------------------
                LocalDate fechaSeleccionada =dpBooking.getValue();
                paid=true;
                horaInicio=LocalTime.parse(reservaSeleccionada.getHora().substring(0,5));
                
                //pistaSeleccionada.setName(Combo.getValue());
                try{
                 
                    
                club.registerBooking(LocalDateTime.now(),fechaSeleccionada,horaInicio,member.checkHasCreditInfo(),club.getCourt(Combo.getValue()),member); 
                updateTableView();
                Alert alert4 = new Alert(Alert.AlertType.WARNING);
                    alert4.setTitle("Información");
                    alert4.setHeaderText(null);
                    alert4.setContentText("Pista reservada y pagada");
                    alert4.showAndWait();
                

                }catch(ClubDAOException e){
                
                }}else{
                   LocalDate fechaSeleccionada =dpBooking.getValue();
                paid=false;
                horaInicio=LocalTime.parse(reservaSeleccionada.getHora().substring(0,5));
                
                //pistaSeleccionada.setName(Combo.getValue());
                try{
                 
                    
                club.registerBooking(LocalDateTime.now(),fechaSeleccionada,horaInicio,member.checkHasCreditInfo(),club.getCourt(Combo.getValue()),member); 
                updateTableView();
                Alert alert4 = new Alert(Alert.AlertType.WARNING);
                    alert4.setTitle("Información");
                    alert4.setHeaderText(null);
                    alert4.setContentText("Pista reservada, para pagarla accede al apartado mis reservas ");
                    alert4.showAndWait();
                

                }catch(ClubDAOException e){}}} 
        }   else{
            if(bookSeleccionada!=null||caso!=1 && !miNombre.equals(reservaSeleccionada.getMiembro())){
                Alert alert4 = new Alert(Alert.AlertType.WARNING);
                    alert4.setTitle("Advertencia");
                    alert4.setHeaderText(null);
                    alert4.setContentText("La pista ya está ocupada");
                    alert4.showAndWait();
                  
            }
            if(!esPosible&& caso==1){
            Alert alert4 = new Alert(Alert.AlertType.WARNING);
                    alert4.setTitle("Advertencia");
                    alert4.setHeaderText(null);
                    alert4.setContentText("No se reservar más de dos horas seguidas");
                    alert4.showAndWait();
            }
            
        }
        
}

    @FXML
    private void ComboAction(ActionEvent event) {
      
             updateTableView();
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