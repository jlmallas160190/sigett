/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.vocabulario.Vocabulario;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class LineaInvestigacionProyectoService implements LineaInvestigacionProyectoServiceLocal {

    private String path;
    private Vocabulario vocabulario;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void read(String path) {
        try {
            this.vocabulario = new Vocabulario();
            this.path = path;
            InputStream in = new FileInputStream(path);
            vocabulario.getModel().read(in, "RDF/XML");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void write(Map parametros) {
        try {
            Individual individuoProyecto = null;
            Individual individuoLineaInvestigacion = null;
            Individual individuoLineaInvestigacionProyecto = null;
            individuoProyecto = vocabulario.getModel().getIndividual(vocabulario.getNS() + "proyecto/" + parametros.get("proyectoId"));
            if (individuoProyecto != null) {
                individuoLineaInvestigacion = vocabulario.getModel().getIndividual(vocabulario.getNS() + "lineaInvestigacion" + parametros.get("lineaInvestigacionId"));
                if (individuoLineaInvestigacion == null) {
                    individuoLineaInvestigacion = vocabulario.getModel().createIndividual(vocabulario.getNS() + "lineaInvestigacion/" + parametros.get("lineaInvestigaciond"), vocabulario.editarClaseLineaInvestigacion());
                }
                individuoLineaInvestigacion.setPropertyValue(FOAF.name, vocabulario.getModel().createTypedLiteral(parametros.get("lineaInvestigacionNombre")));
                individuoLineaInvestigacion.setPropertyValue(DC.description, vocabulario.getModel().createTypedLiteral(parametros.get("lineaInvestigacionNombre")));
                individuoLineaInvestigacion.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("lineaInvestigacionId")));

                individuoLineaInvestigacionProyecto = vocabulario.getModel().getIndividual(vocabulario.getNS() + "lineaInvestigacionProyecto/" + parametros.get("lineaInvestigacionProyectoId"));
                if (individuoLineaInvestigacionProyecto == null) {
                    individuoLineaInvestigacionProyecto = vocabulario.getModel().createIndividual(vocabulario.getNS() + "lineaInvestigacionProyecto/" + parametros.get("lineaInvestigacionProyectoId"), vocabulario.editarClaseLineaInvestigacionProyecto());
                }
                individuoLineaInvestigacionProyecto.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("lineaInvestigacionProyectoId")));
                individuoLineaInvestigacionProyecto.setPropertyValue(vocabulario.editarPropiedadHasLineaInvestigacion(), individuoLineaInvestigacion);
                individuoLineaInvestigacionProyecto.setPropertyValue(vocabulario.editarPropiedadHasProyecto(), individuoProyecto);

                File file = new File(path);
                if (!file.exists()) {
                    file.createNewFile();
                }
                PrintWriter out = new PrintWriter(file);
                out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                vocabulario.getModel().write(out, "RDF/XML");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
