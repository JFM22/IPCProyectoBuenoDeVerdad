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
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
 */
public class Fecha2NuevoController1 implements Initializable {

    @FXML
    private DatePicker dpBooking;
    @FXML
    private ComboBox<String> Combo;
    private GridPane grid;
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
    boolean alertaAceptar=false;
    boolean esLibre=false;
    private Club club;
    List<Booking> reservas;
    //es del ejemplo
     private final LocalTime firstSlotStart = LocalTime.of(9, 0);
    private final Duration slotLength = Duration.ofMinutes(60);
    private final LocalTime lastSlotStart = LocalTime.of(21, 0);

    // se puede cambiar por codigo la pseudoclase activa de un nodo    
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private List<TimeSlot> timeSlots = new ArrayList<>(); //Para varias columnas List<List<TimeSolt>>
    
    private ObjectProperty<TimeSlot> timeSlotSelected;
    
    private LocalDate daySelected;
    
    @FXML
    private Label slotSelected;
    TimeSlot aux;
    private Label label1;
    private Label label2;
    private Label label3;
    private Label label4;
    private Label label5;
    private Label label6;
    private Label label7;
    private Label label8;
    private Label label9;
    private Label label10;
    private Label label11;
    private Label label12;
    private Label label13;
    private Label []etiquetas;
    int LabelIndex;
    @FXML
    private ListView<?> ListaReservas;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Combo.getItems().addAll("Pista 1","Pista 2", "Pista 3","Pista 4", "Pista 5", "Pista 6");
        Combo.setValue("Pista 1");
        etiquetas= new Label[]{label1,label2,label3,label4,label5,label6,label7,label8,label9,label10,label11,label12,label13};
        pista.setName(Combo.getValue());
        //versionFinal-------
        //user = Usuario.getInstancia();
       //member = user.getUsuario();
        //pruebas---------
        try {
            
            club = Club.getInstance();
        } catch (ClubDAOException ex) {
            Logger.getLogger(Fecha2NuevoController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Fecha2NuevoController1.class.getName()).log(Level.SEVERE, null, ex);
        }
        timeSlotSelected = new SimpleObjectProperty<>();
        //user = Usuario.getInstancia();
         //member = club.getMemberByCredentials("vege", "7777777");
          //user.setUsuario(member);
        //---------------------------------------------------------------------
        //cambia los SlotTime al cambiar de dia
        dpBooking.valueProperty().addListener((a, b, c) -> {
            setTimeSlotsGrid(c,pista);
            labelCol.setText(c.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()));
            
        });
         Combo.valueProperty().addListener((a, b, c) -> {
             pista.setName(c);
            setTimeSlotsGrid(dpBooking.getValue(),pista);
            
        });
         
        
        //---------------------------------------------------------------------
        //inicializa el DatePicker al dia actual
        dpBooking.setValue(LocalDate.now());        

        //---------------------------------------------------------------------
        // pinta los SlotTime en el grid
        setTimeSlotsGrid(dpBooking.getValue(),pista);
        
      
        //---------------------------------------------------------------------
        // enlazamos timeSlotSelected con el label para mostrar la seleccion
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E MMM d");
        timeSlotSelected.addListener((a, b, c) -> {
            if (c == null) {
                slotSelected.setText("");
            } else {
                slotSelected.setText(c.getDate().format(dayFormatter)
                        + "-"
                        + c.getStart().format(timeFormatter));
            }
        });
        
        
    }
 private void setTimeSlotsGrid(LocalDate date,Court pista) {
        //actualizamos la seleccion
        timeSlotSelected.setValue(null);
       reservas = club.getCourtBookings(pista.getName(),  date);
        //--------------------------------------------        
        //borramos los SlotTime del grid
        ObservableList<Node> children = grid.getChildren();//te lo guarda todo en una ObservableList (los guarda como Node)
        for (TimeSlot timeSlot : timeSlots) {
           //children.remove(timeSlot.getView());
        }
        timeSlots = new ArrayList<>();
        //----------------------------------------------------------------------------------
        // desde la hora de inicio y hasta la hora de fin creamos slotTime segun la duracion
        int slotIndex = 1;
        LabelIndex=0;
        for (LocalDateTime startTime = date.atTime(firstSlotStart);!startTime.isAfter(date.atTime(lastSlotStart));startTime = startTime.plus(slotLength)) {

            //---------------------------------------------------------------------------------------
            // creamos el SlotTime, lo anyadimos a la lista de la columna y asignamos sus manejadores
            for(Booking aux : reservas){
             //LocalDateTime bookingEndTime = aux.getBookingDate().plusMinutes(120);
             aux.getFromTime();
             if(aux.getBookingDate().equals(startTime)){
                   esLibre=false;
                    break;
            } 
                else {
                    esLibre=true;
                }
            }
            //--------------------------------------------------------------
            
            TimeSlot timeSlot = new TimeSlot(startTime, slotLength,etiquetas[LabelIndex],esLibre);
            
            timeSlots.add(timeSlot);
            registerHandlers(timeSlot,esLibre,LabelIndex);
            //-----------------------------------------------------------
            // lo anyadimos al grid en la posicion x= 1, y= slotIndex
            
            grid.add(timeSlot.getView(), 1, slotIndex);
           
            LabelIndex++;
            slotIndex++;
        }
        
        
    }
 private void registerHandlers(TimeSlot timeSlot,boolean esLibre,int numLabel) {
        
        timeSlot.getView().setOnMousePressed((MouseEvent event) -> {
            //---------------------------------------------slot----------------------------
            //solamente puede estar seleccionado un slot dentro de la lista de slot
            timeSlots.forEach(slot -> {
                slot.setSelected(slot == timeSlot);
             System.out.println(timeSlot.etiquetaDeTimeSlot().getText());
            //mio    
                aux=timeSlot;
                               

            });

            //----------------------------------------------------------------
            //actualizamos el label Dia-Hora, esto es ad hoc,  para mi diseño
            timeSlotSelected.setValue(timeSlot);
        
       
        });
        
    }

    @FXML
    private void ReservarClicked(ActionEvent event) {
        
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("SlotTime");
                alerta.setHeaderText("Confirma la selección");
                alerta.setContentText("Has seleccionado: "
                        + aux.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ", "
                        + aux.getTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));//fin de mensaje
                Optional<ButtonType> result = alerta.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    
                    ObservableList<String> styles = aux.getView().getStyleClass();
                    if (styles.contains("time-slot")) {//que si que esta disponible
                        styles.remove("time-slot");
                        styles.add("time-slot-libre");
                //-------------------------------------------------------------------------------------
                LocalDate fechaSeleccionada =dpBooking.getValue();
                horaInicio=aux.getTime();
                pista.setName(Combo.getValue());
                try{
                
                club.registerBooking(LocalDateTime.now(),fechaSeleccionada,horaInicio,paid,pista,member);  
                System.out.println("libre y registrada crack");

                }catch(ClubDAOException e){
                System.out.println("esta reservado maquina");
                
                    }
                    } else {//ya esta pillada
                        System.out.println("reservada");
                        styles.remove("time-slot-libre");
                        styles.add("time-slot");
                    }
                }
    }
     public class TimeSlot {

        private final LocalDateTime start;
        private final Duration duration;
        protected final Pane view;
        private Label label;
        private final BooleanProperty selected = new SimpleBooleanProperty();

        public final BooleanProperty selectedProperty() {
            return selected;
        }

        public final boolean isSelected() {
            return selectedProperty().get();
        }

        public final void setSelected(boolean selected) {
            selectedProperty().set(selected);
        }

        public TimeSlot(LocalDateTime start, Duration duration,Label label,boolean esLibre) {
            this.start = start;
            this.label=label;
            this.duration = duration;
           
            view =  new Pane();
           
            view.getStyleClass().add("time-slot");
            // ---------------------------------------------------------------
            // de esta manera cambiamos la apariencia del TimeSlot cuando los seleccionamos
            selectedProperty().addListener((obs, wasSelected, isSelected)
                    -> view.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, isSelected));
         

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
        public Label etiquetaDeTimeSlot(){
            return label;  
        }

        public Duration getDuration() {
            return duration;
        }

        public Node getView() {
            return view;
        }
        public void setStyle(boolean ocupado){
        if(ocupado){
            
            view.getStyleClass().add("time-slot-ocupado");
        }else{
                view.getStyleClass().add("time-slot-Libre");

        }
        }
     
     }
    
}
