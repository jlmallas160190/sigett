/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lud.service.implement;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.lud.dto.ProyectoOntDTO;
import edu.unl.sigett.lud.service.ProyectoOntService;
import edu.unl.sigett.lud.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class ProyectoOntServiceImplement extends AbstractSigettLUD implements ProyectoOntService {

    @Override
    public void read(CabeceraWebSemantica cabeceraWebSemantica) {
        this.setCabecera(cabeceraWebSemantica);
    }

    @Override
    public void write(ProyectoOntDTO proyectoDTO) {
        try {
            proyectoDTO.setIndividual(this.getCabecera().getVocabulario().getModel().getIndividual(
                    this.getCabecera().getVocabulario().getNS() + "proyecto/" + proyectoDTO.getId()));
            if (proyectoDTO.getIndividual() == null) {
                proyectoDTO.setIndividual(this.getCabecera().getVocabulario().getModel().createIndividual(
                        this.getCabecera().getVocabulario().getNS() + "proyecto/" + proyectoDTO.getId(),
                        this.getCabecera().getVocabulario().editarProyectoOnt()));
            }
            proyectoDTO.getIndividual().setPropertyValue(this.getCabecera().getVocabulario().editarPropiedadId(),
                    this.getCabecera().getVocabulario().getModel().createTypedLiteral(proyectoDTO.getId()));
            proyectoDTO.getIndividual().setPropertyValue(DC.title, this.getCabecera().getVocabulario().getModel().createTypedLiteral(proyectoDTO.getTema()));
            proyectoDTO.getIndividual().setPropertyValue(DC.description, this.getCabecera().getVocabulario().getModel().createTypedLiteral(proyectoDTO.getEstado()));
            proyectoDTO.getIndividual().setPropertyValue(DC.type, this.getCabecera().getVocabulario().getModel().createTypedLiteral(proyectoDTO.getTipo()));
            proyectoDTO.getIndividual().setPropertyValue(DC.date, this.getCabecera().getVocabulario().getModel().createTypedLiteral(proyectoDTO.getFechaCreacion()));

            File file = new File(this.getCabecera().getRuta());
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file);
            out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            this.getCabecera().getVocabulario().getModel().write(out, "RDF/XML");
        } catch (IOException e) {
            Logger.getLogger(ProyectoOntServiceImplement.class.getName()).warning(e.getMessage());
        }
    }

    @Override
    public List<ProyectoOntDTO> buscar(final ProyectoOntDTO proyectoOntDTO) {
        List<ProyectoOntDTO> proyectos = new ArrayList<>();
        try {
            StringBuilder sparql = new StringBuilder();
            sparql.append("PREFIX sigett:<http://www.owl-ontologies.com/sigett/>\n"
                    + "PREFIX foaf:<http://xmlns.com/foaf/0.1/>\n"
                    + "PREFIX dc:<http://purl.org/dc/elements/1.1/>\n"
                    + "select DISTINCT ?id ?tema ?nombreCarrera ?estadoProyecto ?tipo ?carreraId ?ofertaAcademicaId \n"
                    + "where{\n");
            sparql.append("?proyecto sigett:id ?id.\n"
                    + "?proyecto dc:title ?tema.\n"
                    + "?proyecto dc:type ?tipo.\n"
                    + "?proyecto dc:description ?estadoProyecto.\n"
                    + "?directorProyecto sigett:hasProyecto ?proyecto.\n"
                    + "?directorProyecto sigett:hasDocente ?docente.\n"
                    + "?docente foaf:name ?datosDirector.\n"
                    + "?autorProyecto sigett:hasProyecto ?proyecto.\n"
                    + "?autorProyecto sigett:hasAutor ?autor.\n"
                    + "?autor foaf:name ?datosAutor.\n"
                    + "?lineaInvestigacionProyecto sigett:hasProyecto ?proyecto.\n"
                    + "?lineaInvestigacionProyecto sigett:hasLineaInvestigacion ?lineaInvestigacion.\n"
                    + "?lineaInvestigacion dc:title ?nombreLineaInvestigacion.\n"
                    + "?proyectoCarreraOferta sigett:hasProyecto ?proyecto.\n"
                    + "?proyectoCarreraOferta sigett:hasCarrera ?carrera.\n"
                    + "?proyectoCarreraOferta sigett:hasOfertaAcademica ?ofertaAcademica.\n"
                    + "?ofertaAcademica sigett:id ?ofertaAcademicaId.\n"
                    + "?carrera dc:title ?nombreCarrera.\n"
                    + "?carrera sigett:id ?carreraId.\n"
                    + "?carrrera sigett:esParteDeArea ?area.\n"
                    + "?area sigett:sigla ?siglaArea.\n"
                    + "?carrrera sigett:esParteDeNivelAcademico ?nivelAcademico.\n"
                    + "?nivelAcademico dc:title ?nombreNivel.\n"
                    + "?ofertaAcademica sigett:hasPeriodoAcademico ?periodoAcademico.\n"
                    + "?periodoAcademico sigett:fechaInicio ?fechaInicioPeriodo.");
            sparql.append("FILTER(regex(?tema,'" + "").append(proyectoOntDTO.getTema()).append("','i')");

            if (proyectoOntDTO.getAutor() != null) {
                sparql.append("|| regex(?datosAutor,'").append(proyectoOntDTO.getAutor()).append("','i')");
            }
            if (proyectoOntDTO.getCarrera() != null) {
                sparql.append("||regex(?nombreCarrera,'").append(proyectoOntDTO.getCarrera()).append("','i')");
            }
            if (proyectoOntDTO.getTipo() != null) {
                sparql.append("|| regex(?tipo,'").append(proyectoOntDTO.getTipo()).append("','i')");
            }
            if (proyectoOntDTO.getEstado() != null) {
                sparql.append("|| regex(?estadoProyecto,'").append(proyectoOntDTO.getEstado()).append("','i')");
            }
            if (proyectoOntDTO.getDocente() != null) {
                sparql.append("|| regex(?datosDirector,'").append(proyectoOntDTO.getDocente()).append("','i')");
            }
            if (proyectoOntDTO.getLineaInvestigacion() != null) {
                sparql.append("|| regex(?nombreLineaInvestigacion,'").append(proyectoOntDTO.getLineaInvestigacion()).append("','i')");
            }
            if (proyectoOntDTO.getNivelAcademico() != null) {
                sparql.append("|| regex(?nivelAcademico,'").append(proyectoOntDTO.getNivelAcademico()).append("','i')");
            }
            sparql.append(")");
            sparql.append("}");
            Query query = QueryFactory.create(sparql.toString());
            QueryExecution qe = QueryExecutionFactory.create(query, this.getCabecera().getVocabulario().getModel());
            ResultSet results = qe.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                String id = soln.getLiteral("id").getString();
                String tipo = soln.getLiteral("tipo").getString();
                String nombreCarrera = soln.getLiteral("nombreCarrera").getString();
                String estado = soln.getLiteral("estadoProyecto").getString();
                String tema = soln.getLiteral("tema").getString();
                String carreraId = soln.getLiteral("carreraId").getString();
                String ofertaId = soln.getLiteral("ofertaAcademicaId").getString();
                ProyectoOntDTO proyecto = new ProyectoOntDTO();
                proyecto.setId(Long.parseLong(id));
                proyecto.setCarrera(nombreCarrera);
                proyecto.setEstado(estado);
                proyecto.setTipo(tipo);
                proyecto.setOfertaAcademicaId(Long.parseLong(ofertaId));
                proyecto.setCarreraId(Integer.parseInt(carreraId));
                proyecto.setTema(tema);
                proyectos.add(proyecto);
            }
        } catch (NumberFormatException e) {
            Logger.getLogger(ProyectoOntServiceImplement.class.getName()).warning(e.getMessage());
        }
        return proyectos;
    }

}
