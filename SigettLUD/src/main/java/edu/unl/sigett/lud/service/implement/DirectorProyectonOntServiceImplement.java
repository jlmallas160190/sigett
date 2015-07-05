/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.lud.service.implement;

import edu.unl.sigett.lud.dto.DirectorProyectoOntDTO;
import edu.unl.sigett.lud.service.DirectorProyectoOntService;
import edu.unl.sigett.lud.util.CabeceraWebSemantica;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 *
 * @author jorge-luis
 */
public class DirectorProyectonOntServiceImplement implements DirectorProyectoOntService {

    private CabeceraWebSemantica cabecera;
    private static final Logger LOG = Logger.getLogger(DirectorProyectonOntServiceImplement.class.getName());

    @Override
    public void read(CabeceraWebSemantica cabecera) {
        this.cabecera = cabecera;
    }

    @Override
    public void write(DirectorProyectoOntDTO directorProyectoOntDTO) {
        try {
            directorProyectoOntDTO.setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + directorProyectoOntDTO.getUri() + "/" + directorProyectoOntDTO.getId()));
            if (directorProyectoOntDTO.getIndividual() == null) {
                directorProyectoOntDTO.setIndividual(cabecera.getVocabulario().getModel().createIndividual(
                        cabecera.getVocabulario().getNS() + directorProyectoOntDTO.getUri() + "/" + directorProyectoOntDTO.getId(),
                        cabecera.getVocabulario().editarDirectorProyectoOnt()));
            }
            directorProyectoOntDTO.getDocenteDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + directorProyectoOntDTO.getDocenteDTO().getUri() + "/" + directorProyectoOntDTO
                    .getDocenteDTO().getId()));

            directorProyectoOntDTO.getProyectoDTO().setIndividual(cabecera.getVocabulario().getModel().getIndividual(
                    cabecera.getVocabulario().getNS() + directorProyectoOntDTO.getProyectoDTO().getUri() + "/" + directorProyectoOntDTO.
                    getProyectoDTO().getId()));
            if (directorProyectoOntDTO.getDocenteDTO().getIndividual() == null) {
                return;
            }
            if (directorProyectoOntDTO.getProyectoDTO().getIndividual() == null) {
                return;
            }
            directorProyectoOntDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadId(),
                    cabecera.getVocabulario().getModel().createTypedLiteral(directorProyectoOntDTO.getId()));
            directorProyectoOntDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadHasProyecto(),
                    directorProyectoOntDTO.getProyectoDTO().getIndividual());
            directorProyectoOntDTO.getIndividual().setPropertyValue(cabecera.getVocabulario().editarPropiedadHasDocente(),
                    directorProyectoOntDTO.getDocenteDTO().getIndividual());
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
