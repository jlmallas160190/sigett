/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lud.service.implement;

import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.lud.dto.LineaInvestigacionProyectoOntDTO;
import edu.unl.sigett.lud.service.LineaInvestigacionProyectoOntService;
import edu.unl.sigett.lud.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class LineaInvestigacionProyectoOntServiceImplement implements LineaInvestigacionProyectoOntService {

    private static final Logger LOG = Logger.getLogger(LineaInvestigacionProyectoOntServiceImplement.class.getName());
    private CabeceraWebSemantica cabeceraWebSemantica;

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabeceraWebSemantica = cabecera;
    }

    @Override
    public void write(LineaInvestigacionProyectoOntDTO lineaInvestigacionProyectoDTO) {
        try {
            lineaInvestigacionProyectoDTO.setIndividual(cabeceraWebSemantica.getVocabulario().getModel().getIndividual(
                    cabeceraWebSemantica.getVocabulario().getNS() + lineaInvestigacionProyectoDTO.getUri() + "/" + lineaInvestigacionProyectoDTO.getId()));
            if (lineaInvestigacionProyectoDTO.getIndividual() == null) {
                lineaInvestigacionProyectoDTO.setIndividual(cabeceraWebSemantica.getVocabulario().getModel().createIndividual(
                        cabeceraWebSemantica.getVocabulario().getNS() + lineaInvestigacionProyectoDTO.getUri() + "/" + lineaInvestigacionProyectoDTO.getId(),
                        cabeceraWebSemantica.getVocabulario().editarLineaInvestigacionProyectoOnt()));
            }
            lineaInvestigacionProyectoDTO.getProyectoDTO().setIndividual(cabeceraWebSemantica.getVocabulario().getModel().getIndividual(
                    cabeceraWebSemantica.getVocabulario().getNS() + lineaInvestigacionProyectoDTO.getProyectoDTO().getUri() + "/"
                    + lineaInvestigacionProyectoDTO.getProyectoDTO().getId()));
            if (lineaInvestigacionProyectoDTO.getProyectoDTO().getIndividual() == null) {
                return;
            }
            lineaInvestigacionProyectoDTO.getLineaInvestigacionDTO().setIndividual(cabeceraWebSemantica.getVocabulario().getModel().getIndividual(
                    cabeceraWebSemantica.getVocabulario().getNS() + lineaInvestigacionProyectoDTO.getLineaInvestigacionDTO().getUri() + "/"
                    + lineaInvestigacionProyectoDTO.getLineaInvestigacionDTO().getId()));
            if (lineaInvestigacionProyectoDTO.getLineaInvestigacionDTO().getIndividual() == null) {
                return;
            }
            lineaInvestigacionProyectoDTO.getIndividual().setPropertyValue(cabeceraWebSemantica.getVocabulario().editarPropiedadId(),
                    cabeceraWebSemantica.getVocabulario().getModel().createTypedLiteral(lineaInvestigacionProyectoDTO.getId()));
            lineaInvestigacionProyectoDTO.getIndividual().setPropertyValue(cabeceraWebSemantica.getVocabulario().editarPropiedadHasLineaInvestigacion(),
                    lineaInvestigacionProyectoDTO.getLineaInvestigacionDTO().getIndividual());
            lineaInvestigacionProyectoDTO.getIndividual().setPropertyValue(cabeceraWebSemantica.getVocabulario().editarPropiedadHasProyecto(),
                    lineaInvestigacionProyectoDTO.getProyectoDTO().getIndividual());

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
