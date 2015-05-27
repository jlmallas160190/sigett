/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service.implement;

import edu.unl.sigett.webSemantica.dto.ProyectoCarreraOfertaDTO;
import edu.unl.sigett.webSemantica.service.ProyectoCarreraOfertaOntService;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class ProyectoCarreraOfertaOntServiceImplement implements ProyectoCarreraOfertaOntService {

    private static final Logger LOG = Logger.getLogger(ProyectoCarreraOfertaOntServiceImplement.class.getName());

    private CabeceraWebSemantica cabecera;

    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    public void write(final ProyectoCarreraOfertaDTO proyectoCarreraOfertaDTO) {
        try {
            proyectoCarreraOfertaDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "proyectoCarreraOferta/" + proyectoCarreraOfertaDTO.getId()));
            if (proyectoCarreraOfertaDTO.getIndividual() == null) {
                proyectoCarreraOfertaDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(
                        cabecera.getVocabulario().getNS() + "proyectoCarreraOferta/" + proyectoCarreraOfertaDTO.getId(),
                        cabecera.getVocabulario().editarAutorOnt()));
            }
            proyectoCarreraOfertaDTO.getCarreraDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "carrera/" +proyectoCarreraOfertaDTO.getCarreraDTO().getId()));

            if(proyectoCarreraOfertaDTO.getCarreraDTO().getIndividual()==null){
                return;
            }
           proyectoCarreraOfertaDTO.getOfertaAcademicaDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "ofertaAcademica/" +proyectoCarreraOfertaDTO.getOfertaAcademicaDTO().getId()));

            if(proyectoCarreraOfertaDTO.getOfertaAcademicaDTO().getIndividual()==null){
                return;
            }
            proyectoCarreraOfertaDTO.getProyectoDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "proyecto/" +proyectoCarreraOfertaDTO.getProyectoDTO().getId()));

            if(proyectoCarreraOfertaDTO.getProyectoDTO().getIndividual()==null){
                return;
            }
           proyectoCarreraOfertaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(), 
                   cabecera.getVocabulario().getModel().createTypedLiteral(proyectoCarreraOfertaDTO.getId()));
            proyectoCarreraOfertaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadHasProyecto(),
                    proyectoCarreraOfertaDTO.getProyectoDTO().getIndividual());
          proyectoCarreraOfertaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadHasOfertaAcademica(),
                  proyectoCarreraOfertaDTO.getOfertaAcademicaDTO().getIndividual());
           proyectoCarreraOfertaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadHasCarrera(),
                   proyectoCarreraOfertaDTO.getCarreraDTO().getIndividual());

            File file = new File(cabecera.getRuta());
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file);
            out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            cabecera.getVocabulario().getModel().write(out, "RDF/XML");
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }
    }

}
