/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lud.service.implement;

import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.lud.dto.AreaAcademicaOntDTO;
import edu.unl.sigett.lud.service.AreaAcademicaOntService;
import edu.unl.sigett.lud.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class AreaAcademicaOntServiceImplement implements AreaAcademicaOntService {

    private CabeceraWebSemantica cabecera;
    private static final Logger LOG = Logger.getLogger(AreaAcademicaOntServiceImplement.class.getName());

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    @Override
    public void write(final AreaAcademicaOntDTO areaAcademicaDTO) {
        try {
            areaAcademicaDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + areaAcademicaDTO.getUri() + "/" + areaAcademicaDTO.getId()));
            if (areaAcademicaDTO.getIndividual() == null) {
                areaAcademicaDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(
                        cabecera.getVocabulario().getNS() + areaAcademicaDTO.getUri() + "/" + areaAcademicaDTO.getId(),
                        cabecera.getVocabulario().editarAreaOnt()));
            }
            areaAcademicaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(areaAcademicaDTO.getId()));
            areaAcademicaDTO.getIndividual().setPropertyValue(DC.title, cabecera.getVocabulario().getModel().createTypedLiteral(areaAcademicaDTO.getNombre()));
            areaAcademicaDTO.getIndividual().setPropertyValue(DC.description, cabecera.getVocabulario().getModel().createTypedLiteral(areaAcademicaDTO.getNombre()));
            areaAcademicaDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadSigla(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(areaAcademicaDTO.getSigla()));

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
