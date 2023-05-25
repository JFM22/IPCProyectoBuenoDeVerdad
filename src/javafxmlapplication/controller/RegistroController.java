/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxmlapplication.JavaFXMLApplication;
import model.Club;
import model.ClubDAOException;

/**
 * FXML Controller class
 *
 * @author Javier
 */
public class RegistroController implements Initializable {

    @FXML
    private TextField field_nombre;
    @FXML
    private TextField field_apellidos;
    @FXML
    private TextField field_telefono;
    @FXML
    private TextField field_nickname;
    @FXML
    private PasswordField field_password;
    @FXML
    private TextField field_tarjeta;
    @FXML
    private TextField field_cvv;
    @FXML
    private Button button_imagen;
    @FXML
    private Label imagen_nombre;

    Club club;
    @FXML
    private Circle circulo;
    
    Image im;
    @FXML
    private HBox main;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            club = Club.getInstance();
        } catch (Exception ex){}
        //button_imagen.getScene().getStylesheets().add(getClass().getResource("/estilos/styles.css").toExternalForm());
        String dir = File.separator+"icons"+File.separator+"default.PNG";
        Image im = new Image(dir,false);
        circulo.setFill(new ImagePattern(im));
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0a2d8f")),
                new Stop(1, Color.web("#051540")));
        main.setBackground(new Background(new BackgroundFill(gradient, null, null)));
        
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
        field_nickname.textProperty().addListener((a,oldV,newV)->{
            if(newV.contains(" ")){
                launch_error_inm("El nickname no puede contener espacios");
                field_nickname.setText(oldV);
            }
        });
        field_telefono.textProperty().addListener((a,oldV,newV)->{
            if(!newV.equals("") && !newV.matches("\\d+")){
                launch_error_inm("El número de teléfono está formado solo por números");
                field_telefono.setText(oldV);
            }
            else if(newV.length() > 9){
                launch_error_inm("El numero de telefono está formado por 9 dígitos");
                field_telefono.setText(oldV);
            }
        });
        field_password.focusedProperty().addListener((observable, oldValue, newValue)->{
            if (!newValue){
                if(field_password.getText().length()<7){
                    launch_error_inm("La contraseña debe contener más de 6 carácteres");
                    field_password.requestFocus();
                };
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
    
    @FXML
    private void button_confirmar(ActionEvent event) throws ClubDAOException,IOException{
        ArrayList<String> ar = new ArrayList<>(); //ArrayList auxiliar para almacenar todos los errores detectados.
        
        String nick = field_nickname.getText();
        String nombre = field_nombre.getText();
        String apellidos = field_apellidos.getText();
        String telefono = field_telefono.getText();
        String pswrd = field_password.getText();
        String tarjeta = field_tarjeta.getText();
        String svc = field_cvv.getText();
        int svc_num = 0;
        
        if(nick.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty()) {ar.add("-Alguno de los campos obligatorios está vacío");}
        if(nick.contains(" ")){ar.add("-El nickname no puede contener espacios");}
        if(club.existsLogin(nick)){ar.add("-El nickname introducido ya existe");}
        
        if(pswrd.length() <= 6){
            ar.add("-La contraseña debe contener más de 6 carácteres");
            field_password.setText("");
        }
        
        if(!tarjeta.isEmpty() || !svc.isEmpty()){
            if(tarjeta.length() != 16){ar.add("-El número de la tarjeta debe contener 16 carácteres");}
            if(svc.length() != 3 || !svc.matches("\\d+")){ar.add("-El código scv debe contener 3 dígitos");}
            else{
                svc_num=Integer.parseInt(svc);
            }
        }
        if (!ar.isEmpty()) launch_error(ar);
        else{
            //club.registerMember(nombre, apellidos, telefono, nick, pswrd, tarjeta, svc_num, im);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cuenta válida");
            alert.setHeaderText("Al aceptar este mensaje tu cuenta será creada."
                    + "\nDeberás autentificarte con tus datos para acceder a la aplicación.");
            //alert.setContentText();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                club.registerMember(nombre, apellidos, telefono, nick, pswrd, tarjeta, svc_num, im);
                FXMLLoader loader= new  FXMLLoader(getClass().getResource("/javafxmlapplication/view/autenticarse.fxml"));
                JavaFXMLApplication.setRoot((Parent)loader.load());
            } else {}
        }
    }


    @FXML
    private void escoger_imagen(ActionEvent event) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");

        // Filtrar solo archivos de imagen
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Archivos de imagen", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog((Stage) button_imagen.getScene().getWindow());
        if (selectedFile != null) {
            String dir = selectedFile.getAbsolutePath();
            this.im = new Image(new FileInputStream(dir));
            circulo.setFill(new ImagePattern(im));
            imagen_nombre.setText(selectedFile.getName());
        }
    }

    @FXML
    private void volver(ActionEvent event){
        JavaFXMLApplication.setRoot("inicioapp");
    }
    
}
