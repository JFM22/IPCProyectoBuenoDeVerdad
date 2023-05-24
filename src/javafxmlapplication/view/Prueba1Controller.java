/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication.view;

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
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Javier
 */
public class Prueba1Controller implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private Button butt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void button1(ActionEvent event) throws IOException{
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("autenticarse.fxml"));
        Node root = loader.load();
        borderpane.setCenter(root);
    }

    @FXML
    private void button2(ActionEvent event) throws IOException{
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("registro.fxml"));
        Node root = loader.load();
        borderpane.setCenter(root);
    }
    
}
