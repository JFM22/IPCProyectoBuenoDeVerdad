/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

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
    
}
