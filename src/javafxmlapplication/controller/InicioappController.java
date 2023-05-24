/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import java.io.IOException;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
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
    private Text UserTexto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //rect.setFill(new javafx.scene.paint.LinearGradient(0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
        //        new javafx.scene.paint.Stop(0, Color.web("#4f46e4")),
        //        new javafx.scene.paint.Stop(1, Color.TRANSPARENT)));
        //main.setBackground(new javafx.scene.paint.LinearGradient(0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
        //        new javafx.scene.paint.Stop(0, Color.web("#4f46e4")),
        //        new javafx.scene.paint.Stop(1, Color.TRANSPARENT)));
        //main.getChildren().add(rect);
        UserTexto.textProperty().bind(Usuario.getInstancia().getNickProperty());
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0a2d8f")),
                new Stop(1, Color.web("#051540")));
        
        // Establecer el fondo del panel con el gradiente
        main.setBackground(new Background(new BackgroundFill(gradient, null, null)));
    }    

    @FXML
    private void aut_button(ActionEvent event) throws IOException {
        //new Thread(()->{FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), main);
        //fadeTransition.setToValue(0.0);
        //fadeTransition.play();}).start();
        //Thread.sleep(2);
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
