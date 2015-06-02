/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.webSemantica.service.implement;

import edu.unl.sigett.webSemantica.dto.AutorProyectoOntDTO;
import edu.unl.sigett.webSemantica.service.AutorProyectoOntService;
import edu.unl.sigett.webSemantica.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author jorge-luis
 */
public class AutorProyectoOntServiceImplement implements AutorProyectoOntService {

    private CabeceraWebSemantica cabecera;

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    @Override
    public void write(AutorProyectoOntDTO autorProyectoDTO) {
        try {
            autorProyectoDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "autorProyecto/" + autorProyectoDTO.getId()));
            if (autorProyectoDTO.getIndividual() == null) {
                autorProyectoDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(
                        cabecera.getVocabulario().getNS() + "autorProyecto/" + autorProyectoDTO.getId(),
                        cabecera.getVocabulario().editarAutorOnt()));
            }
            autorProyectoDTO.getAutorDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "autor/" + autorProyectoDTO.getAutorDTO().getId()));

            autorProyectoDTO.getProyectoDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + "proyecto/" + autorProyectoDTO.getProyectoDTO().getId()));
            if (autorProyectoDTO.getAutorDTO().getIndividual() == null) {
                return;
            }
            if (autorProyectoDTO.getProyectoDTO().getIndividual() == null) {
                return;
            }
            autorProyectoDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(autorProyectoDTO.getId()));
            autorProyectoDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadHasProyecto(),
                    autorProyectoDTO.getProyectoDTO().getIndividual());
            autorProyectoDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadHasAutor(),
                    autorProyectoDTO.getAutorDTO().getIndividual());
            File file = new File(cabecera.getRuta());
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file);
            out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            cabecera.getVocabulario().getModel().write(out, "RDF/XML");
        } catch (IOException e) {
        }

    }

}
