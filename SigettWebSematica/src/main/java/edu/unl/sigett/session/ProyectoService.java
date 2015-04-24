/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.vocabulario.Vocabulario;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ProyectoService implements ProyectoServiceLocal {

    private String path;
    private Vocabulario vocabulario;
    private OntClass proyectoClass;

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
    public Map getTodo(Map parametros) {
        Map resultado = new HashMap();
        try {
            InputStream in = new FileInputStream(path);
            vocabulario.getModel().read(in, "RDF/XML");
            String queryString = "PREFIX sigett:<http://www.owl-ontologies.com/sigett/>\n"
                    + "PREFIX foaf:<http://xmlns.com/foaf/0.1/>\n"
                    + "PREFIX dc:<http://purl.org/dc/elements/1.1/>\n"
                    + "select ?id\n"
                    + "where{\n"
                    + "?proyecto sigett:id ?id.\n"
                    + "?proyecto dc:title ?tema.\n"
                    + "?proyecto dc:type ?tipo.\n"
                    + "?proyecto sigett:hasEstadoProyecto ?estadoProyecto.\n"
                    + "?estadoProyecto foaf:name ?nombreEstado.\n"
                    + "OPTIONAL{?directorProyecto sigett:hasProyecto ?proyecto}.\n"
                    + "OPTIONAL{?directorProyecto sigett:hasDocente ?docente}.\n"
                    + "OPTIONAL{?docente sigett:esInvestigador ?investigador1}.\n"
                    + "OPTIONAL{?investigador1 foaf:firstName ?nombresDirector}.\n"
                    + "OPTIONAL{?investigador1 foaf:surname ?apellidosDirector}.\n"
                    + "?autorProyecto sigett:hasProyecto ?proyecto.\n"
                    + "?autorProyecto sigett:hasAutor ?autor.\n"
                    + "?autor sigett:id ?id_autor.\n"
                    + "?autor sigett:esInvestigador ?investigador.\n"
                    + "?investigador foaf:firstName ?nombresAutor.\n"
                    + "?investigador foaf:surname  ?apellidosAutor.\n"
                    + "?lineaInvestigacionProyecto sigett:hasProyecto ?proyecto.\n"
                    + "?lineaInvestigacionProyecto sigett:hasLineaInvestigacion ?lineaInvestigacion.\n"
                    + "?lineaInvestigacion foaf:name ?nombreLineaInvestigacion.\n"
                    + "?proyectoCarreraOferta sigett:hasProyecto ?proyecto.\n"
                    + "?proyectoCarreraOferta sigett:hasCarrera ?carrera.\n"
                    + "?proyectoCarreraOferta sigett:hasOfertaAcademica ?ofertaAcademica.\n"
                    + "?carrera foaf:name ?nombreCarrera.\n"
                    + "?carrrera sigett:esParteDeArea ?area.\n"
                    + "?area sigett:sigla ?siglaArea.\n"
                    + "?carrrera sigett:esParteDeNivelAcademico ?nivelAcademico.\n"
                    + "?nivelAcademico foaf:name ?nombreNivel.\n"
                    + "?ofertaAcademica sigett:hasPeriodoAcademico ?periodoAcademico.\n"
                    + "?periodoAcademico sigett:fechaInicio ?fechaInicioPeriodo.\n"
                    + "FILTER(regex(?tema,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?tipo,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreLineaInvestigacion,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombresAutor,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?apellidosAutor,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombresDirector,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?apellidosDirector,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreCarrera,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreEstado,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?siglaArea,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreNivel,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?fechaInicioPeriodo,'" + parametros.get("filtro") + "','i'))"
                    + "}";
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, vocabulario.getModel());

            ResultSet results = qe.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String name = soln.getLiteral("id").getString();
                if (!resultado.containsValue(name)) {
                    resultado.put("id", name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public Map getPorDocente(Map parametros) {
        Map resultado = new HashMap();
        try {
            InputStream in = new FileInputStream(path);
            vocabulario.getModel().read(in, "RDF/XML");
            String queryString = "PREFIX sigett:<http://www.owl-ontologies.com/sigett/>\n"
                    + "PREFIX foaf:<http://xmlns.com/foaf/0.1/>\n"
                    + "PREFIX dc:<http://purl.org/dc/elements/1.1/>\n"
                    + "select ?id\n"
                    + "where{\n"
                    + "?proyecto dc:title ?tema.\n"
                    + "?proyecto dc:type ?tipo.\n"
                    + "?proyecto sigett:hasEstadoProyecto ?estadoProyecto.\n"
                    + "?estadoProyecto foaf:name ?estadoId.\n"
                    + "?estadoProyecto sigett:id ?nombreEstado.\n"
                    + "?directorProyecto sigett:hasProyecto ?proyecto.\n"
                    + "?directorProyecto sigett:id ?id.\n"
                    + "?directorProyecto sigett:hasDocente ?docente.\n"
                    + "?docente sigett:id ?docenteId.\n"
                    + "?autorProyecto sigett:hasProyecto ?proyecto.\n"
                    + "?autorProyecto sigett:hasAutor ?autor.\n"
                    + "?autor sigett:id ?id_autor.\n"
                    + "?autor sigett:esInvestigador ?investigador.\n"
                    + "?investigador foaf:firstName ?nombresAutor.\n"
                    + "?investigador foaf:surname  ?apellidosAutor.\n"
                    + "?lineaInvestigacionProyecto sigett:hasProyecto ?proyecto.\n"
                    + "?lineaInvestigacionProyecto sigett:hasLineaInvestigacion ?lineaInvestigacion.\n"
                    + "?lineaInvestigacion foaf:name ?nombreLineaInvestigacion.\n"
                    + "?proyectoCarreraOferta sigett:hasProyecto ?proyecto.\n"
                    + "?proyectoCarreraOferta sigett:hasCarrera ?carrera.\n"
                    + "?proyectoCarreraOferta sigett:hasOfertaAcademica ?ofertaAcademica.\n"
                    + "?carrera foaf:name ?nombreCarrera.\n"
                    + "?carrrera sigett:esParteDeArea ?area.\n"
                    + "?area sigett:sigla ?siglaArea.\n"
                    + "?carrrera sigett:esParteDeNivelAcademico ?nivelAcademico.\n"
                    + "?nivelAcademico foaf:name ?nombreNivel.\n"
                    + "?ofertaAcademica sigett:hasPeriodoAcademico ?periodoAcademico.\n"
                    + "?ofertaAcademica sigett:fechaInicio ?fechaInicioOferta.\n"
                    + "?periodoAcademico sigett:fechaInicio ?fechaInicioPeriodo.\n"
                    + "FILTER(regex(?tema,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?tipo,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreLineaInvestigacion,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombresAutor,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?apellidosAutor,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreCarrera,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreEstado,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?siglaArea,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreNivel,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?fechaInicioPeriodo,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?estadoId,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?fechaInicioOferta,'" + parametros.get("filtro") + "','i'))"
                    + "FILTER(?docenteId=" + parametros.get("docenteId") + ")"
                    + "}"
                    + "ORDER BY DESC(?id)";
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, vocabulario.getModel());

            ResultSet results = qe.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String name = soln.getLiteral("id").getString();
                if (!resultado.containsValue(name)) {
                    resultado.put("id", name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public Map getPorCarrera(Map parametros) {
        Map resultado = new HashMap();
        try {
            String queryString = "";
            queryString = "PREFIX sigett:<http://www.owl-ontologies.com/sigett/>\n"
                    + "PREFIX foaf:<http://xmlns.com/foaf/0.1/>\n"
                    + "PREFIX dc:<http://purl.org/dc/elements/1.1/>\n"
                    + "select ?id\n"
                    + "where{\n"
                    + "?proyecto sigett:id ?id.\n"
                    + "?proyecto dc:title ?tema.\n"
                    + "?proyecto dc:type ?tipo.\n"
                    + "?proyecto sigett:hasEstadoProyecto ?estadoProyecto.\n"
                    + "?estadoProyecto foaf:name ?nombreEstado.\n"
                    + "?autorProyecto sigett:hasProyecto ?proyecto.\n"
                    + "?autorProyecto sigett:hasAutor ?autor.\n"
                    + "?autor sigett:id ?id_autor.\n"
                    + "?autor sigett:esInvestigador ?investigador.\n"
                    + "?investigador foaf:firstName ?nombresAutor.\n"
                    + "?investigador foaf:surname  ?apellidosAutor.\n"
                    + "OPTIONAL{?lineaInvestigacionProyecto sigett:hasProyecto ?proyecto.}\n"
                    + "OPTIONAL{?lineaInvestigacionProyecto sigett:hasLineaInvestigacion ?lineaInvestigacion.}\n"
                    + "OPTIONAL{?lineaInvestigacion foaf:name ?nombreLineaInvestigacion.}\n"
                    + "?proyectoCarreraOferta sigett:hasProyecto ?proyecto.\n"
                    + "?proyectoCarreraOferta sigett:hasCarrera ?carrera.\n"
                    + "?proyectoCarreraOferta sigett:hasOfertaAcademica ?ofertaAcademica.\n"
                    + "?carrera foaf:name ?nombreCarrera.\n"
                    + "?carrera sigett:id ?carreraId.\n"
                    + "?carrrera sigett:esParteDeArea ?area.\n"
                    + "?carrrera sigett:esParteDeNivelAcademico ?nivelAcademico.\n"
                    + "?nivelAcademico foaf:name ?nombreNivel.\n"
                    + "?area sigett:sigla ?siglaArea.\n"
                    + "?ofertaAcademica sigett:fechaInicio ?fechaInicioOferta.\n"
                    + "?ofertaAcademica sigett:hasPeriodoAcademico ?periodoAcademico.\n"
                    + "?periodoAcademico sigett:fechaInicio ?fechaInicioPeriodo.\n"
                    + "FILTER(regex(?tema,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?tipo,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreLineaInvestigacion,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombresAutor,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?apellidosAutor,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreCarrera,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreEstado,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?siglaArea,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?nombreNivel,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?fechaInicioPeriodo,'" + parametros.get("filtro") + "','i')||"
                    + "regex(?fechaInicioOferta,'" + parametros.get("filtro") + "','i'))"
                    + "FILTER(?carreraId=" + parametros.get("carreraId") + ")"
                    + "}";
            Query query = QueryFactory.create(queryString);
            QueryExecution qe = QueryExecutionFactory.create(query, vocabulario.getModel());

            ResultSet results = qe.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String name = soln.getLiteral("id").getString();
                if (!resultado.containsValue(name)) {
                    resultado.put("id", name);
                }
            }
        } catch (Exception e) {
        }
        return resultado;
    }

    @Override
    public void write(Map parametros) {
        try {
            proyectoClass = vocabulario.editarClaseProyecto();
            OntClass estadoProyectoClass = vocabulario.editarClaseEstadoProyecto();
            Individual individuo = null;
            Individual individuoEstado = null;
            DatatypeProperty idProperty = vocabulario.editarPropiedadId();
            individuo = vocabulario.getModel().getIndividual(vocabulario.getNS() + "proyecto/" + parametros.get("proyectoId"));
            if (individuo == null) {
                individuo = vocabulario.getModel().createIndividual(vocabulario.getNS() + "proyecto/" + parametros.get("proyectoId"), proyectoClass);
            }

            individuoEstado = vocabulario.getModel().getIndividual(vocabulario.getNS() + "estadoProyecto/" + parametros.get("estadoId"));
            if (individuoEstado == null) {
                individuoEstado = vocabulario.getModel().createIndividual(vocabulario.getNS() + "estadoProyecto/" + parametros.get("estadoId"), estadoProyectoClass);
                individuoEstado.setPropertyValue(vocabulario.editarPropiedadId(), vocabulario.getModel().createTypedLiteral(parametros.get("estadoId")));
                individuoEstado.setPropertyValue(FOAF.name, vocabulario.getModel().createTypedLiteral(parametros.get("nombre")));
            }
            individuo.setPropertyValue(DC.title, vocabulario.getModel().createTypedLiteral(parametros.get("tema")));
            individuo.setPropertyValue(vocabulario.editarPropiedadHasEstadoProyecto(), individuoEstado);
            individuo.setPropertyValue(DC.description, vocabulario.getModel().createTypedLiteral(parametros.get("descripcion")));
            individuo.setPropertyValue(idProperty, vocabulario.getModel().createTypedLiteral(parametros.get("id")));
            individuo.setPropertyValue(DC.type, vocabulario.getModel().createTypedLiteral(parametros.get("tipo")));
            individuo.setPropertyValue(vocabulario.editarPropiedadFechaPublicacion(), vocabulario.getModel().createTypedLiteral(parametros.get("fecha")));

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file);
            out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            vocabulario.getModel().write(out, "RDF/XML");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
