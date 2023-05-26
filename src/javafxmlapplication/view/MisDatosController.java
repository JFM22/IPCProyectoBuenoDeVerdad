/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

///jshflsahdlkjdaslkfjlkasfjdlkjasdkfjlkasfjlkd
package javafxmlapplication.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafxmlapplication.JavaFXMLApplication;
import model.Club;
import model.ClubDAOException;
import model.Member;
import utils.Usuario;

/**
 * FXML Controller class
 *
 * @author Javier
 */
public class MisDatosController implements Initializable {

    @FXML
    private TextField field_nombre;
    @FXML
    private TextField field_apellidos;
    @FXML
    private TextField field_telefono;
    @FXML
    private Label field_nickname;
    @FXML
    private PasswordField field_password;
    @FXML
    private TextField field_tarjeta;
    @FXML
    private TextField field_cvv;
    @FXML
    private Button button_imagen;
    @FXML
    private StackPane stackpane;
    @FXML
    private Circle circulo;
    @FXML
    private Label imagen_nombre;
    
    Member member;
    Club club;
    Usuario u;
    Image im;
    @FXML
    private ImageView mod_nombre;
    @FXML
    private ImageView mod_apellidos;
    @FXML
    private ImageView mod_telefono;
    @FXML
    private ImageView mod_contraseña;
    @FXML
    private ImageView mod_tarjeta;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        u = Usuario.getInstancia();
        member = u.getUsuario();
        try {
            club = Club.getInstance();
        } catch (ClubDAOException ex) {
            Logger.getLogger(MisDatosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MisDatosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        im = member.getImage();
        inicializar();
        
        mod_nombre.visibleProperty().bind(Bindings.notEqual(u.getNameProperty(), field_nombre.textProperty()));
        mod_apellidos.visibleProperty().bind(Bindings.notEqual(u.getSurnameProperty(), field_apellidos.textProperty()));
        mod_telefono.visibleProperty().bind(Bindings.notEqual(u.getTelephoneProperty(), field_telefono.textProperty()));
        mod_contraseña.visibleProperty().bind(field_password.textProperty().isNotEmpty());
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
                if(field_password.getText().length()<7 && !field_password.getText().equals("")){
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
    public void inicializar(){
        im = member.getImage();
        mod_tarjeta.visibleProperty().unbind(); //Por si el usuario ha modificado una tarjeta vacía y ahora ya no lo está
        
        field_nombre.setText(member.getName());
        field_apellidos.setText(member.getSurname());
        field_telefono.setText(member.getTelephone());
        field_nickname.setText(member.getNickName());
        field_password.setText("");
        
        if(member.checkHasCreditInfo()){
            field_tarjeta.setText(member.getCreditCard());
            field_cvv.setText(Integer.toString(member.getSvc()));
        }else{
            field_tarjeta.setText("");
            field_cvv.setText("");
        }
        
        
        circulo.setFill(new ImagePattern(member.getImage()));
        imagen_nombre.setStyle("");
        if(field_tarjeta.getText().isEmpty()){
            mod_tarjeta.visibleProperty().bind(Bindings.or(field_tarjeta.textProperty().isNotEmpty(),field_cvv.textProperty().isNotEmpty()));
        }else {
            mod_tarjeta.visibleProperty().bind(Bindings.or(
                    Bindings.notEqual(Integer.toString(member.getSvc()), field_cvv.textProperty()),
                    Bindings.notEqual(member.getCreditCard(), field_tarjeta.textProperty())
            ));
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
            im = new Image(new FileInputStream(dir));
            circulo.setFill(new ImagePattern(im));
            imagen_nombre.setText(selectedFile.getName());
            imagen_nombre.setStyle("-fx-text-fill: #0088c4");
        }
    }

    @FXML
    private void button_actuDatos(ActionEvent event) throws IOException, ClubDAOException {
        ArrayList<String> ar = new ArrayList<>(); //ArrayList auxiliar para almacenar todos los errores detectados.

        String nombre = field_nombre.getText();
        String apellidos = field_apellidos.getText();
        String telefono = field_telefono.getText();
        String pswrd = field_password.getText();
        String tarjeta = field_tarjeta.getText();
        String svc = field_cvv.getText();
        int svc_num = 0;
        
        
        if(nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty()) {ar.add("-Alguno de los campos obligatorios está vacío");}
        if(pswrd.isEmpty()){
            pswrd = member.getPassword();
        }
        else if(pswrd.length() <= 6){
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
            alert.setTitle("Datos válidos");
            alert.setHeaderText("Al aceptar este mensaje tus datos serán modificados."
                    + "\n¿Quieres continuar?");
            //alert.setContentText();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                member.setName(nombre);
                member.setSurname(apellidos);
                member.setTelephone(telefono);
                member.setPassword(pswrd);
                member.setCreditCard(tarjeta);
                member.setSvc(svc_num);
                member.setImage(im);
                u.setUsuario(member);
                inicializar();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Datos modificados");
                alert.setHeaderText("Tus datos han sido modificados correctamente");
                alert.showAndWait();
            } else {inicializar();}
        }
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
    private void button_cancelar(ActionEvent event) {
        inicializar();
        //FXMLLoader loader= new  FXMLLoader(getClass().getResource("/javafxmlapplication/view/MenuPrincipal.fxml"));
    }
    
}
