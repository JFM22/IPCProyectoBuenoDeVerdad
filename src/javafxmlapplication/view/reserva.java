/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxmlapplication.view;

import java.time.LocalTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Javier
 */
public class reserva {
    private final StringProperty pista = new SimpleStringProperty();
    private final StringProperty hora = new SimpleStringProperty();
    private final StringProperty miembro = new SimpleStringProperty();
    private final StringProperty disponibilidad = new SimpleStringProperty();
        
    public reserva(String p, LocalTime h, String m, String d) {
        setPista(p);
        setHora(h.toString());
        setMiembro(m);
        setDisponibilidad(d);
    }

    public StringProperty pistaProperty() {
        return this.pista;
    }

    public final String getPista() {
        return this.pistaProperty().get();
    }

    public final void setPista(String pista) {
        this.pistaProperty().set(pista);
    }

    public StringProperty horaProperty() {
        return this.hora;
    }

    public final String getHora() {
        return this.horaProperty().get();
    }

    public final void setHora(String hora) {
        this.horaProperty().set(hora);
    }

    public StringProperty miembroProperty() {
        return this.miembro;
    }

    public final String getMiembro() {
        return this.miembroProperty().get();
    }

    public final void setMiembro(String miembro) {
        this.miembroProperty().set(miembro);
    }

    public StringProperty disponibilidadProperty() {
        return this.disponibilidad;
    }

    public final String getDisponibilidad() {
        return this.disponibilidadProperty().get();
    }

    public final void setDisponibilidad(String disponibilidad) {
        this.disponibilidadProperty().set(disponibilidad);
    }
}
