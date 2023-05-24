/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.File;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import model.Member;

/**
 *
 * @author Javier
 */
public class Usuario {
    public static Usuario instancia;
    
    Member usuario;
    private static ObjectProperty<ImagePattern> imagePatternProperty = new SimpleObjectProperty<>();
    private static StringProperty nickNameProperty = new SimpleStringProperty("Invitado");
    private static StringProperty nameProperty = new SimpleStringProperty();
    private static StringProperty surnameProperty = new SimpleStringProperty();
    private static StringProperty passwordProperty = new SimpleStringProperty();
    private static StringProperty telephoneProperty = new SimpleStringProperty();
    private static StringProperty creditCardProperty = new SimpleStringProperty();
    private static IntegerProperty svcProperty = new SimpleIntegerProperty();
    private static BooleanProperty notLogged = new SimpleBooleanProperty(true);
    
    public Usuario(Member u){this.usuario = u;}
    
    public static Usuario getInstancia(){
        if (instancia == null){
            instancia = new Usuario(null);
            String dir = File.separator+"icons"+File.separator+"default.PNG";
            imagePatternProperty.set(new ImagePattern(new Image(dir)));
            nickNameProperty.set("Invitado");
        }
        return instancia;
    }
    
    public Member getUsuario(){return usuario;}
    
    public void setUsuario(Member u){
        this.usuario = u;
        notLogged.set(false);
        updateUser();
    }
    public void updateUser(){
        if(usuario.getImage() != null){
            imagePatternProperty.set(new ImagePattern(usuario.getImage()));
        }
        nickNameProperty.set(usuario.getNickName());
        nameProperty.set(usuario.getName());
        surnameProperty.set(usuario.getSurname());
        passwordProperty.set(usuario.getPassword());
        telephoneProperty.set(usuario.getTelephone());
        creditCardProperty.set(usuario.getCreditCard());
        svcProperty.set(usuario.getSvc());
    }
    
    public StringProperty getNickProperty() {return nickNameProperty;}
    public StringProperty getSurnameProperty() {return surnameProperty;}
    public StringProperty getNameProperty() {return nameProperty;}
    public StringProperty getPasswordProperty() {return passwordProperty;}
    public StringProperty getTelephoneProperty() {return telephoneProperty;}
    public IntegerProperty getSvcProperty() {return svcProperty;}
    public ObjectProperty<ImagePattern> getImagePatternProperty() {return imagePatternProperty;}
    public BooleanProperty getIsNotLoggedProperty() {return notLogged;}
}
