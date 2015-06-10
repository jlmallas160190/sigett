/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service.implement;

import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import edu.unl.sigett.webSemantica.dto.ProyectoOntDTO;
import edu.unl.sigett.webSemantica.service.ProyectoOntService;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class ProyectoOntServiceImplement implements ProyectoOntService {

    private static final Logger LOG = Logger.getLogger(ProyectoOntServiceImplement.class.getName());

    private CabeceraWebSemantica cabecera;

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    @Override
    public void write(ProyectoOntDTO proyectoDTO) {
        try {
            proyectoDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "proyecto/" + proyectoDTO.getId()));
            if (proyectoDTO.getIndividual() == null) {
                proyectoDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(
                        cabecera.getVocabulario().getNS() + "proyecto/" + proyectoDTO.getId(),
                        cabecera.getVocabulario().editarAutorOnt()));
            }
            proyectoDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(proyectoDTO.getId()));
            proyectoDTO.getIndividual().setPropertyValue(DC.title, cabecera.getVocabulario().getModel().createTypedLiteral(proyectoDTO.getTema()));
            proyectoDTO.getIndividual().setPropertyValue(DC.description, cabecera.getVocabulario().getModel().createTypedLiteral(proyectoDTO.getEstado()));
            proyectoDTO.getIndividual().setPropertyValue(DC.type, cabecera.getVocabulario().getModel().createTypedLiteral(proyectoDTO.getTipo()));
            proyectoDTO.getIndividual().setPropertyValue(DC.date, cabecera.getVocabulario().getModel().createTypedLiteral(proyectoDTO.getFechaCreacion()));

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
