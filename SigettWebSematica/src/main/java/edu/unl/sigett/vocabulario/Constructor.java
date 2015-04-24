/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.vocabulario;

import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import java.io.File;
import java.io.PrintWriter;

/**
 *
 * @author jorge-luis
 */
public class Constructor {

    public static void main(String[] args) throws Exception {
        try {
            Vocabulario lUDSigett = new Vocabulario();
            lUDSigett.getModel().setNsPrefix("foaf", FOAF.NS);
            lUDSigett.getModel().setNsPrefix("sigett", lUDSigett.getNS());
            lUDSigett.crearClaseProyecto();
            lUDSigett.crearClaseInvestigador();
            lUDSigett.crearClaseAutor();
            lUDSigett.crearClaseAutorProyecto();
            lUDSigett.crearClaseDocente();
            lUDSigett.crearClaseDirectorProyecto();
            lUDSigett.crearClaseArea();
            lUDSigett.crearClaseNivelAcademico();
            lUDSigett.crearClaseCarrera();
            lUDSigett.crearClaseLineaInvestigacion();
            lUDSigett.crearClaseLineaInvestigacionProyecto();
            lUDSigett.crearClasePeriodoAcademico();
            lUDSigett.crearClaseOfertaAcademica();
            lUDSigett.crearClaseProyectoCarreraOferta();
            lUDSigett.crearClaseEstadoProyecto();

            lUDSigett.getDocente().setDisjointWith(lUDSigett.getAutor());

            lUDSigett.crearPropiedadHasAutor();
            lUDSigett.crearPropiedadHasDocente();
            lUDSigett.crearPropiedadHasProyecto();
            lUDSigett.crearPropiedadId();
            lUDSigett.crearPropiedadNumeroIdentificacion();
            lUDSigett.crearPropiedadHasCarrera();
            lUDSigett.crearPropiedadSigla();
            lUDSigett.crearPropiedadEsParteDeArea();
            lUDSigett.crearPropiedadEsParteDeNivelAcademico();
            lUDSigett.crearPropiedadEsParteDePeriodoAcademico();
            lUDSigett.crearPropiedadFechaFinAcademico();
            lUDSigett.crearPropiedadFechaInicioAcademico();
            lUDSigett.crearPropiedadFechaPublicacion();
            lUDSigett.crearPropiedadHasLineaInvestigacion();
            lUDSigett.crearPropiedadHasOfertaAcademica();
            lUDSigett.crearPropiedadEsInvestigador();
            lUDSigett.crearPropiedadHasEstadoProyecto();
            File file = new File("D://sigett.owl");
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file);
            out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            lUDSigett.getModel().write(out, "RDF/XML");
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
