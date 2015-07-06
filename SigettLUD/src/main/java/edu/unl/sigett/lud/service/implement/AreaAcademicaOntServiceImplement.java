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

/**
 *
 * @author jorge-luis
 */
public class AreaAcademicaOntServiceImplement extends AbstractSigettLUD implements AreaAcademicaOntService {


    @Override
    public void read(CabeceraWebSemantica cabeceraWebSemantica) {
        this.setCabecera(cabeceraWebSemantica);
    }

    @Override
    public void write(final AreaAcademicaOntDTO areaAcademicaDTO) {
        try {
            areaAcademicaDTO.setIndividual(this.getCabecera().getVocabulario().getModel().getIndividual(
                    this.getCabecera().getVocabulario().getNS() + areaAcademicaDTO.getUri() + "/" + areaAcademicaDTO.getId()));
            if (areaAcademicaDTO.getIndividual() == null) {
                areaAcademicaDTO.setIndividual(this.getCabecera().getVocabulario().getModel().createIndividual(
                        this.getCabecera().getVocabulario().getNS() + areaAcademicaDTO.getUri() + "/" + areaAcademicaDTO.getId(),
                        this.getCabecera().getVocabulario().editarAreaOnt()));
            }
            areaAcademicaDTO.getIndividual().setPropertyValue(this.getCabecera().getVocabulario().editarPropiedadId(),
                    this.getCabecera().getVocabulario().getModel().createTypedLiteral(areaAcademicaDTO.getId()));
            areaAcademicaDTO.getIndividual().setPropertyValue(DC.title, this.getCabecera().getVocabulario().getModel().createTypedLiteral(areaAcademicaDTO.getNombre()));
            areaAcademicaDTO.getIndividual().setPropertyValue(DC.description, this.getCabecera().getVocabulario().getModel().createTypedLiteral(areaAcademicaDTO.getNombre()));
            areaAcademicaDTO.getIndividual().setPropertyValue(this.getCabecera().getVocabulario().editarPropiedadSigla(),
                    this.getCabecera().getVocabulario().getModel().createTypedLiteral(areaAcademicaDTO.getSigla()));

            File file = new File(this.getCabecera().getRuta());
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file);
            out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            this.getCabecera().getVocabulario().getModel().write(out, "RDF/XML");
        } catch (IOException e) {
        }
    }

}
