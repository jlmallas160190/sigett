/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.vocabulario.Vocabulario;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ProyectoCarreraOfertaService implements ProyectoCarreraOfertaServiceLocal {
    @EJB
    private ProyectoServiceLocal proyectoService;

    private String path;
    private Vocabulario vocabulario;
    private Proyecto p;

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
            Individual individuoProyectoCarreraOferta;
            Individual individuoCarrera = null;
            Individual individuoOferta = null;
            Individual individuoPeriodo = null;
            Individual individuoNivel = null;
            Individual individuoArea = null;
            individuoProyecto = vocabulario.getModel().getIndividual(vocabulario.getNS() + "proyecto/" + parametros.get("proyectoId"));
            if (individuoProyecto != null) {
                individuoCarrera = vocabulario.getModel().getIndividual(vocabulario.getNS() + "carrera/" + parametros.get("carreraId"));
                if (individuoCarrera == null) {
                    individuoCarrera = vocabulario.getModel().createIndividual(vocabulario.getNS() + "carrera/" + parametros.get("carreraId"), vocabulario.editarClaseCarrera());

                    individuoArea = vocabulario.getModel().getIndividual(vocabulario.getNS() + "area/" + parametros.get("areaId"));
                    if (individuoArea == null) {
                        individuoArea = vocabulario.getModel().createIndividual(vocabulario.getNS() + "area/" + parametros.get("areaId"), vocabulario.editarClaseArea());
                    }
                    individuoArea.setPropertyValue(FOAF.name, vocabulario.getModel().createTypedLiteral(parametros.get("areaNombre")));
                    individuoArea.setPropertyValue(vocabulario.editarPropiedadSigla(), vocabulario.getModel().createTypedLiteral(parametros.get("areaSigla")));
                    individuoArea.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("areaId")));

                    individuoNivel = vocabulario.getModel().getIndividual(vocabulario.getNS() + "nivel/" + parametros.get("nivelId"));
                    if (individuoNivel == null) {
                        individuoNivel = vocabulario.getModel().createIndividual(vocabulario.getNS() + "nivel/" + parametros.get("nivelId"), vocabulario.editarClaseNivelAcademico());
                    }
                    individuoNivel.setPropertyValue(FOAF.name, vocabulario.getModel().createTypedLiteral(parametros.get("nivelNombre")));
                    individuoNivel.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("nivelId")));

                    individuoCarrera.setPropertyValue(FOAF.name, vocabulario.getModel().createTypedLiteral(parametros.get("carreraNombre")));
                    individuoCarrera.setPropertyValue(vocabulario.editarPropiedadSigla(), vocabulario.getModel().createTypedLiteral(parametros.get("carreraSigla")));
                    individuoCarrera.setPropertyValue(vocabulario.editarPropiedadEsParteDeArea(), individuoArea);
                    individuoCarrera.setPropertyValue(vocabulario.editarPropiedadEsParteDeNivelAcademico(), individuoNivel);
                    individuoCarrera.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("carreraId")));
                }
                individuoOferta = vocabulario.getModel().getIndividual(vocabulario.getNS() + "ofertaAcademica/" + parametros.get("ofertaId"));
                if (individuoOferta == null) {
                    individuoOferta = vocabulario.getModel().createIndividual(vocabulario.getNS() + "ofertaAcademica/" + parametros.get("ofertaId"), vocabulario.editarClaseOfertaAcademica());

                    individuoPeriodo = vocabulario.getModel().getIndividual(vocabulario.getNS() + "periodoAcademico/" + parametros.get("periodoId"));
                    if (individuoPeriodo == null) {
                        individuoPeriodo = vocabulario.getModel().createIndividual(vocabulario.getNS() + "periodoAcademico/" + parametros.get("periodoId"), vocabulario.editarClasePeriodoAcademico());
                    }

                    individuoPeriodo.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("periodoId")));
                    individuoPeriodo.setPropertyValue(vocabulario.editarPropiedadFechaInicioAcademico(), vocabulario.getModel().createTypedLiteral(parametros.get("periodoFechaInicio")));
                    individuoPeriodo.setPropertyValue(vocabulario.editarPropiedadFechaFinAcademico(), vocabulario.getModel().createTypedLiteral(parametros.get("periodoFechaFin")));

                    individuoOferta.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("ofertaId")));
                    individuoOferta.setPropertyValue(vocabulario.editarPropiedadFechaInicioAcademico(), vocabulario.getModel().createTypedLiteral(parametros.get("ofertaFechaInicio")));
                    individuoOferta.setPropertyValue(vocabulario.editarPropiedadFechaFinAcademico(), vocabulario.getModel().createTypedLiteral(parametros.get("ofertaFechaFin")));
                    individuoOferta.setPropertyValue(vocabulario.editarPropiedadEsParteDePeriodoAcademico(), individuoPeriodo);

                }
                individuoProyectoCarreraOferta = vocabulario.getModel().getIndividual(vocabulario.getNS() + "proyectoCarreraOferta/" + parametros.get("proyectoCarreraOfertaId"));
                if (individuoProyectoCarreraOferta == null) {
                    individuoProyectoCarreraOferta = vocabulario.getModel().createIndividual(vocabulario.getNS() + "proyectoCarreraOferta/" + parametros.get("proyectoCarreraOfertaId"), vocabulario.editarClaseProyectoCarreraOferta());
                }
                individuoProyectoCarreraOferta.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("proyectoCarreraOfertaId")));
                individuoProyectoCarreraOferta.setPropertyValue(vocabulario.editarPropiedadHasProyecto(), individuoProyecto);
                individuoProyectoCarreraOferta.setPropertyValue(vocabulario.editarPropiedadHasOfertaAcademica(), individuoOferta);
                individuoProyectoCarreraOferta.setPropertyValue(vocabulario.editarPropiedadHasCarrera(), individuoCarrera);

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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
