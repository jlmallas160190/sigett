/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.util;

import edu.unl.sigett.webSemantica.vocabulay.Vocabulario;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

/**
 *
 * @author jorge-luis
 */
public class CabeceraWebSemantica implements Serializable {

    private Vocabulario vocabulario;
    private String ruta;

    public CabeceraWebSemantica() {
    }

    public CabeceraWebSemantica(Vocabulario vocabulario, String ruta) throws FileNotFoundException {
        InputStream in = new FileInputStream(ruta);
        vocabulario.getModel().read(in, "RDF/XML");
        this.vocabulario = vocabulario;
        this.ruta = ruta;
    }

    public Vocabulario getVocabulario() {
        return vocabulario;
    }

    public void setVocabulario(Vocabulario vocabulario) {
        this.vocabulario = vocabulario;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

}
