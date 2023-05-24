/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafxmlapplication.JavaFXMLApplication;
import model.Member;
import utils.Usuario;

/**
 * FXML Controller class
 *
 * @author Javier
 */

public class MenuPrincipalController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private Button misReservas_button;
    @FXML
    private Button miPerfil_button;
    
    private HBox anterior;
    @FXML
    private HBox hBox_Reservar;
    @FXML
    private HBox hBox_misReservas;
    @FXML
    private HBox hBox_miPerfil;
    
    Background background;
    Background background_transparente;
    @FXML
    private Text nickNameLabel;
    @FXML
    private Circle userImage;
    
    Member user;
    @FXML
    private Label NotLoggedLabel;
    @FXML
    private Button reservar_button;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Usuario u = Usuario.getInstancia();
        user = u.getUsuario();
        
        nickNameLabel.textProperty().bind(u.getNickProperty());
        userImage.fillProperty().bind(u.getImagePatternProperty());
        
        //Quería hacer esto con bindings pero el nickname e Imagen de Member no tienen .property
        //prepararUser();
        prepararColores();
        miPerfil_button.disableProperty().bind(u.getIsNotLoggedProperty());
        misReservas_button.disableProperty().bind(u.getIsNotLoggedProperty());
        NotLoggedLabel.visibleProperty().bind(u.getIsNotLoggedProperty());
    }    
    
    private void change_color(HBox hbox){
        if(anterior.equals(hbox)) return;
        hbox.setBackground(background_transparente);
        
        if(anterior != null) anterior.setBackground(background);
        
        anterior = hbox;
    }
    
    @FXML
    private void reservarAction(ActionEvent event) {
        change_color(hBox_Reservar);
    }

    @FXML
    private void misReservasAction(ActionEvent event) {
        change_color(hBox_misReservas);
    }

    @FXML
    private void miPerfilAction(ActionEvent event) throws IOException {
        change_color(hBox_miPerfil);
        
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("/javafxmlapplication/view/MiPerfil.fxml"));
        Node root = loader.load();
        borderpane.setCenter(root);
        
    }
    
    private void prepararColores(){
        BackgroundFill backgroundFill = new BackgroundFill(Color.web("#0b2e8e"), null, null);
        background = new Background(backgroundFill);
        
        backgroundFill = new BackgroundFill(Color.TRANSPARENT, null, null);
        background_transparente = new Background(backgroundFill);
        
        hBox_Reservar.setBackground(background_transparente);
        hBox_misReservas.setBackground(background);
        hBox_miPerfil.setBackground(background);
        
        Stop[] stops = new Stop[]{
                new Stop(0, Color.valueOf("#051540")),
                new Stop(1, Color.valueOf("#7194f4"))
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        borderpane.setBackground(new Background(new BackgroundFill(gradient, null, null)));
        anterior = hBox_Reservar;
    }
    
    private void prepararUser(){
        //Por defecto, usamos la imagen del invitado
        String dir = File.separator+"icons"+File.separator+"default.PNG";
        Image im = new Image(dir,false);
        userImage.setFill(new ImagePattern(im));
        
        if (user != null){
            nickNameLabel.setText(user.getNickName());
            Image image = user.getImage();
            if (image != null) userImage.setFill(new ImagePattern(image));
        }
        else{
            nickNameLabel.setText("Invitado");
        }
    }

    @FXML
    private void volver(ActionEvent event) {
        JavaFXMLApplication.setRoot("inicioapp");
    }
}
