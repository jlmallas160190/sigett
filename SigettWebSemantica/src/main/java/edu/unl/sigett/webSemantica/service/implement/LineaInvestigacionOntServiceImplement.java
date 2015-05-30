/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service.implement;

import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.webSemantica.dto.LineaInvestigacionOntDTO;
import edu.unl.sigett.webSemantica.service.LineaInvestigacionOntService;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class LineaInvestigacionOntServiceImplement implements LineaInvestigacionOntService {

    private static final Logger LOG = Logger.getLogger(LineaInvestigacionOntServiceImplement.class.getName());
    private CabeceraWebSemantica cabeceraWebSemantica;

    @Override
    public void read(final CabeceraWebSemantica cabecera) {
        this.cabeceraWebSemantica = cabecera;
    }

    @Override
    public void write(final LineaInvestigacionOntDTO lineaInvestigacionDTO) {
        try {
            lineaInvestigacionDTO.setIndividual(cabeceraWebSemantica.getVocabulario().getModel().getIndividual(
                    cabeceraWebSemantica.getVocabulario().getNS() + lineaInvestigacionDTO.getUri() + "/" + lineaInvestigacionDTO.getId()));
            if (lineaInvestigacionDTO.getIndividual() == null) {
                lineaInvestigacionDTO.setIndividual(cabeceraWebSemantica.getVocabulario().getModel().createIndividual(
                        cabeceraWebSemantica.getVocabulario().getNS() + lineaInvestigacionDTO.getUri() + "/" + lineaInvestigacionDTO.getId(),
                        cabeceraWebSemantica.getVocabulario().editarAutorOnt()));
            }

            lineaInvestigacionDTO.getIndividual().setPropertyValue(cabeceraWebSemantica.getVocabulario().editarPropiedadId(),
                    cabeceraWebSemantica.getVocabulario().getModel().createTypedLiteral(lineaInvestigacionDTO.getId()));
            lineaInvestigacionDTO.getIndividual().setPropertyValue(DC.title,
                    cabeceraWebSemantica.getVocabulario().getModel().createTypedLiteral(lineaInvestigacionDTO.getNombre()));
            File file = new File(cabeceraWebSemantica.getRuta());
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file);
            out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            cabeceraWebSemantica.getVocabulario().getModel().write(out, "RDF/XML");
        } catch (IOException e) {
            LOG.info(e.getMessage());
        }
    }

}
