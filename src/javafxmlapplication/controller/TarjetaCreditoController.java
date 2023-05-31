/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Booking;
import model.Member;
import utils.Usuario;

/**
 * FXML Controller class
 *
 * @author Cristian
 */
public class TarjetaCreditoController implements Initializable {

    @FXML
    private TextField field_tarjeta;
    @FXML
    private TextField field_cvv;
    @FXML
    private Button Aceptar;
    @FXML
    private Button cancelar;
    
    private Booking ReservaActual;
    
    Member miembro;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        field_tarjeta.textProperty().addListener((a,oldV,newV)->{
            if(!newV.equals("") && !newV.matches("\\d+")){
                launch_error_inm("El número de la tarjeta está formado solo por números");
                field_tarjeta.setText(oldV);
            }
            else if(newV.length() > 16){
                launch_error_inm("El número de la tarjeta debe contener 16 dígitos");
                field_tarjeta.setText(oldV);
            }
        });
        field_cvv.textProperty().addListener((a,oldV,newV)->{
            if(!newV.equals("") && !newV.matches("\\d+")){
                launch_error_inm("El número vsc formado solo por números");
                field_cvv.setText(oldV);
            }
            else if(newV.length() > 3){
                launch_error_inm("El número del svc debe contener 3 dígitos");
                field_cvv.setText(oldV);
            }
        });
    }    
    private void launch_error_inm(String s){
        Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error en formato");
                alert.setHeaderText(s);
                alert.showAndWait();
    }

    private void launch_error(ArrayList<String> ar){
        String errores = "";
        for(String s : ar) errores += s+"\n";
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error al crear cuenta");
        alert.setHeaderText("Algunos de los datos introducidos no son válidos:");
        alert.setContentText(errores);
        alert.showAndWait();
    }
    
    public void booking(Booking g){
        ReservaActual = g;
    }
    
    @FXML
    private void AceptarTarjeta(ActionEvent event) {
        ArrayList<String> ar = new ArrayList<>();
        int svc_num = 0;
        if(!field_tarjeta.getText().isEmpty() || !field_cvv.getText().isEmpty()){
            if(field_tarjeta.getText().length() != 16){ar.add("-El número de la tarjeta debe contener 16 carácteres");}
            if(field_cvv.getText().length() != 3 || !field_cvv.getText().matches("\\d+")){ar.add("-El código scv debe contener 3 dígitos");}
            else{
                svc_num=Integer.parseInt(field_cvv.getText());
            }
        }
         if (!ar.isEmpty()) {launch_error(ar);}else{
        miembro = Usuario.getInstancia().getUsuario();
        miembro.setCreditCard(field_tarjeta.getText());
        miembro.setSvc(Integer.parseInt(field_cvv.getText()));
        ReservaActual.setPaid(Boolean.TRUE);
        Aceptar.getScene().getWindow().hide();}
    }

    @FXML
    private void CancelarOperacion(ActionEvent event) {
         cancelar.getScene().getWindow().hide();
    }
    
}
