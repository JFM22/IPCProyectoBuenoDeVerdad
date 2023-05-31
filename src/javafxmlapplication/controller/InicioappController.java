/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafxmlapplication.JavaFXMLApplication;
import utils.Usuario;

/**
 * FXML Controller class
 *
 * @author Javier
 */
public class InicioappController implements Initializable {

    @FXML
    private VBox main;
    @FXML
    private Label UserTexto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserTexto.textProperty().bind(Usuario.getInstancia().getNickProperty());
    }    

    @FXML
    private void aut_button(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxmlapplication/view/autenticarse.fxml"));
        Parent root = loader.load();
        JavaFXMLApplication.setRoot(root);
    }

    @FXML
    private void crear_cuenta_action(ActionEvent event) throws IOException {
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("/javafxmlapplication/view/registro.fxml"));
        Parent root = loader.load();
        JavaFXMLApplication.setRoot(root);
    }

    @FXML
    private void inivitadoAction(ActionEvent event) throws IOException{
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("/javafxmlapplication/view/MenuPrincipal.fxml"));
        JavaFXMLApplication.setRoot((Parent)loader.load());
    }
    
}
