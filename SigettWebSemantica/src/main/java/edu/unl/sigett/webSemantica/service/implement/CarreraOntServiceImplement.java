/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service.implement;

import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.webSemantica.dto.CarreraOntDTO;
import edu.unl.sigett.webSemantica.service.CarreraOntService;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class CarreraOntServiceImplement implements CarreraOntService {

    private CabeceraWebSemantica cabecera;
    private static final Logger LOG = Logger.getLogger(CarreraOntServiceImplement.class.getName());

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    @Override
    public void write(CarreraOntDTO carreraDTO) {
        try {
            carreraDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "carrera/" + carreraDTO.getId()));
            if (carreraDTO.getIndividual() == null) {
                carreraDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(
                        cabecera.getVocabulario().getNS() + "carrera/" + carreraDTO.getId(),
                        cabecera.getVocabulario().editarCarreraOnt()));
            }
            carreraDTO.getAreaAcademicaDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "areaAcademica/" + carreraDTO.getAreaAcademicaDTO().getId()));
            if (carreraDTO.getAreaAcademicaDTO().getIndividual() == null) {
                return;
            }
            carreraDTO.getNivelAcademicoDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "nivelAcademico/" + carreraDTO.getNivelAcademicoDTO().getId()));
            if (carreraDTO.getAreaAcademicaDTO().getIndividual() == null) {
                return;
            }
            carreraDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadEsParteDeArea(),
                    carreraDTO.getAreaAcademicaDTO().getIndividual());
            carreraDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadEsParteDeNivelAcademico(),
                    carreraDTO.getNivelAcademicoDTO().getIndividual());
            carreraDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(carreraDTO.getId()));
            carreraDTO.getIndividual().setPropertyValue(DC.title, cabecera.getVocabulario().getModel().createTypedLiteral(carreraDTO.getNombre()));
            carreraDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadSigla(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(carreraDTO.getSigla()));

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
