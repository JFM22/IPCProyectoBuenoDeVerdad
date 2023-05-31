/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Club;
import utils.Usuario;


public class JavaFXMLApplication extends Application {
    
    private static Scene scene;
    private static HashMap<String, Parent> roots = new HashMap<>();
    
    public static void setRoot(Parent root){
        scene.setRoot(root);
    }
    public static void setRoot(String clave){
        Parent root = roots.get(clave);
        if (root != null){
            setRoot(root);
        }else{
            System.err.printf("No se encuentra la escena: %s", clave);
        }
    }
    @Override
    public void start(Stage stage) throws Exception {
        
        //======================================================================
        // 1- creación del grafo de escena a partir del fichero FXML
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("view/inicioapp.fxml"));
        Parent root = loader.load();
        scene = new Scene(root);
        roots.put("inicioapp", root);
        
        //======================================================================
        // 2- creación de la escena con el nodo raiz del grafo de escena
        setRoot("inicioapp");
        
        //======================================================================
        // 3- asiganación de la escena al Stage que recibe el metodo 
        
        scene.getStylesheets().add(getClass().getResource("/estilos/styles_1.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Club de tenis-GreenBall");
        Usuario.getInstancia();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }


    
}
