/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafxmlapplication.JavaFXMLApplication;
import model.Club;
import model.ClubDAOException;
import model.Member;
import utils.Usuario;

/**
 * FXML Controller class
 *
 * @author anónimo
 */

public class AutenticarseController implements Initializable {

    @FXML
    private HBox main;
    @FXML
    private TextField nickName;
    @FXML
    private PasswordField contraseña;
    
    Club club ;
    boolean exist=false;
    @FXML
    private Label AccountFail;
 
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0a2d8f")),
                new Stop(1, Color.web("#051540")));
        
        // Establecer el fondo del panel con el gradiente
        main.setBackground(new Background(new BackgroundFill(gradient, null, null)));
          
        try {
            club= Club.getInstance();
        } catch (ClubDAOException ex) {
            Logger.getLogger(AutenticarseController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AutenticarseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void launch_error(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No se ha podido iniciar sesion");
        alert.setHeaderText("NickName o contraseña no válidos");
        alert.showAndWait();
    }

    @FXML
    private void volver(ActionEvent event){
        JavaFXMLApplication.setRoot("inicioapp");
    }

    @FXML
    private void AccederPressed(ActionEvent event) throws IOException{

        String nombre=nickName.getText();
        String Contraseña= contraseña.getText();
        exist=club.existsLogin(nombre);
        if(!exist){
           launch_error();
        }
        else{
            Member user=club.getMemberByCredentials(nombre, Contraseña);//NO SE PUEDE USAR GETTEXT()? PORQUE ME DEJA??
            if(user==null){ launch_error();}
            else{
                Usuario u = Usuario.getInstancia();
                u.setUsuario(user);
                FXMLLoader loader= new  FXMLLoader(getClass().getResource("/javafxmlapplication/view/MenuPrincipal.fxml"));
                JavaFXMLApplication.setRoot((Parent)loader.load());
            }
        }
    }

    @FXML
    private void CrearCuentaPressed(ActionEvent event) throws IOException {
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("/javafxmlapplication/view/registro.fxml"));
        Parent root = loader.load();
        JavaFXMLApplication.setRoot(root);
    }
}