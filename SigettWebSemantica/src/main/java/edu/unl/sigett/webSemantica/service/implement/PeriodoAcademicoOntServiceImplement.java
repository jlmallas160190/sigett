/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service.implement;

import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.webSemantica.dto.PeriodoAcademicoDTO;
import edu.unl.sigett.webSemantica.service.PeriodoAcademicoOntService;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class PeriodoAcademicoOntServiceImplement implements PeriodoAcademicoOntService {

    private static final Logger LOG = Logger.getLogger(PeriodoAcademicoOntServiceImplement.class.getName());

    private CabeceraWebSemantica cabecera;

    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    public void write(PeriodoAcademicoDTO periodoAcademicoDTO) {
        try {
            periodoAcademicoDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "periodo/" + periodoAcademicoDTO.getId()));
            if (periodoAcademicoDTO.getIndividual() == null) {
                periodoAcademicoDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(
                        cabecera.getVocabulario().getNS() + "periodo/" + periodoAcademicoDTO.getId(),
                        cabecera.getVocabulario().editarAutorOnt()));
            }
            periodoAcademicoDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(periodoAcademicoDTO.getId()));
            periodoAcademicoDTO.getIndividual().setPropertyValue(DC.title, cabecera.getVocabulario().getModel().createTypedLiteral(
                    periodoAcademicoDTO.getNombre()));
            periodoAcademicoDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadFechaInicioAcademico(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(periodoAcademicoDTO.getFechaInicio()));
            periodoAcademicoDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadFechaFinAcademico(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(periodoAcademicoDTO.getFechaFin()));
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
