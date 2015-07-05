/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lud.service.implement;

import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import edu.unl.sigett.lud.dto.AutorOntDTO;
import edu.unl.sigett.lud.service.AutorOntService;
import edu.unl.sigett.lud.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class AutorOntServiceImplement implements AutorOntService {

    private static final Logger LOG = Logger.getLogger(AutorOntServiceImplement.class.getName());

    private CabeceraWebSemantica cabecera;

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    @Override
    public void write(AutorOntDTO autorDTO) {
        try {
            autorDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(cabecera.getVocabulario().getNS() + "autor/" + autorDTO.getId()));
            if (autorDTO.getIndividual() == null) {
                autorDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(cabecera.getVocabulario().getNS() + "autor/" + autorDTO.getId(),
                        cabecera.getVocabulario().editarAutorOnt()));
            }
            autorDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(autorDTO.getId()));
            autorDTO.getIndividual().setPropertyValue(FOAF.name, cabecera.getVocabulario().getModel().createTypedLiteral(autorDTO.toString()));
            autorDTO.getIndividual().setPropertyValue(FOAF.gender, cabecera.getVocabulario().getModel().createTypedLiteral(autorDTO.getGenero()));
            autorDTO.getIndividual().setPropertyValue(FOAF.mbox, cabecera.getVocabulario().getModel().createTypedLiteral(autorDTO.getEmail()));
            autorDTO.getIndividual().setPropertyValue(FOAF.birthday, cabecera.getVocabulario().getModel().createTypedLiteral(autorDTO.getFechaNacimiento()));

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
