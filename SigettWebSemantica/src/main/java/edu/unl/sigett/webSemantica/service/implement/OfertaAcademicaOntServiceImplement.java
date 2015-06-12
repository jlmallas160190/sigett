/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service.implement;

import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.webSemantica.dto.OfertaAcademicaOntDTO;
import edu.unl.sigett.webSemantica.service.OfertaAcademicoOntService;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class OfertaAcademicaOntServiceImplement implements OfertaAcademicoOntService {

    private static final Logger LOG = Logger.getLogger(OfertaAcademicaOntServiceImplement.class.getName());

    private CabeceraWebSemantica cabecera;

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    @Override
    public void write(OfertaAcademicaOntDTO ofertaAcademicaDTO) {
        try {
            ofertaAcademicaDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "ofertaAcademica/" + ofertaAcademicaDTO.getId()));
            if (ofertaAcademicaDTO.getIndividual() == null) {
                ofertaAcademicaDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(
                        cabecera.getVocabulario().getNS() + "ofertaAcademica/" + ofertaAcademicaDTO.getId(),
                        cabecera.getVocabulario().editarOfertaAcademicaOnt()));
            }
            ofertaAcademicaDTO.getPeriodoAcademicoDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "periodo/" + ofertaAcademicaDTO.getPeriodoAcademicoDTO().getId()));
            if (ofertaAcademicaDTO.getPeriodoAcademicoDTO().getIndividual() == null) {
                return;
            }
            ofertaAcademicaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(ofertaAcademicaDTO.getId()));
            ofertaAcademicaDTO.getIndividual().setPropertyValue(DC.title, cabecera.getVocabulario().getModel().createTypedLiteral(
                    ofertaAcademicaDTO.getNombre()));
            ofertaAcademicaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadFechaInicioAcademico(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(ofertaAcademicaDTO.getFechaInicio()));
            ofertaAcademicaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadFechaFinAcademico(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(ofertaAcademicaDTO.getFechaFin()));
            ofertaAcademicaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadEsParteDePeriodoAcademico(),
                    ofertaAcademicaDTO.getPeriodoAcademicoDTO().getIndividual());
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
