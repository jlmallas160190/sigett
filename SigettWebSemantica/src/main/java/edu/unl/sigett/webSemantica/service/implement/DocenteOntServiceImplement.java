/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service.implement;

import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import edu.unl.sigett.webSemantica.dto.DocenteOntDTO;
import edu.unl.sigett.webSemantica.service.DocenteOntService;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class DocenteOntServiceImplement implements DocenteOntService {

    private CabeceraWebSemantica cabecera;
    private static final Logger LOG = Logger.getLogger(DocenteOntServiceImplement.class.getName());

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    @Override
    public void write(DocenteOntDTO docenteOntDTO) {
        try {
            docenteOntDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(cabecera.getVocabulario().getNS() + docenteOntDTO.getUri()
                    + "/" + docenteOntDTO.getId()));
            if (docenteOntDTO.getIndividual() == null) {
                docenteOntDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(cabecera.getVocabulario().getNS()
                        + docenteOntDTO.getUri() + "/" + docenteOntDTO.getId(),
                        cabecera.getVocabulario().editarDocenteOnt()));
            }
            docenteOntDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(docenteOntDTO.getId()));
            docenteOntDTO.getIndividual().setPropertyValue(FOAF.name, cabecera.getVocabulario().getModel().createTypedLiteral(docenteOntDTO.toString()));
            docenteOntDTO.getIndividual().setPropertyValue(FOAF.gender, cabecera.getVocabulario().getModel().createTypedLiteral(docenteOntDTO.getGenero()));
            docenteOntDTO.getIndividual().setPropertyValue(FOAF.mbox, cabecera.getVocabulario().getModel().createTypedLiteral(docenteOntDTO.getEmail()));
            docenteOntDTO.getIndividual().setPropertyValue(FOAF.birthday, cabecera.getVocabulario().getModel().createTypedLiteral(
                    docenteOntDTO.getFechaNacimiento()));

            File file = new File(cabecera.getRuta());
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file);
            out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            cabecera.getVocabulario().getModel().write(out, "RDF/XML");
        } catch (IOException e) {
            LOG.warning(e.getMessage());
        }

    }

}
