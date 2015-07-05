/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lud.service.implement;

import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.lud.dto.NivelAcademicoOntDTO;
import edu.unl.sigett.lud.service.NivelAcademicoOntService;
import edu.unl.sigett.lud.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class NivelAcademicoOntServiceImplement implements NivelAcademicoOntService {

    private static final Logger LOG = Logger.getLogger(NivelAcademicoOntServiceImplement.class.getName());

    private CabeceraWebSemantica cabecera;

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    @Override
    public void write(NivelAcademicoOntDTO nivelDTO) {
        try {
            nivelDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + nivelDTO.getUri() + "/" + nivelDTO.getId()));
            if (nivelDTO.getIndividual() == null) {
                nivelDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(
                        cabecera.getVocabulario().getNS() + nivelDTO.getUri() + "/" + nivelDTO.getId(),
                        cabecera.getVocabulario().editarNivelAcademicoOnt()));
            }
            nivelDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(nivelDTO.getId()));
            nivelDTO.getIndividual().setPropertyValue(DC.title, cabecera.getVocabulario().getModel().createTypedLiteral(nivelDTO.getNombre()));

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
