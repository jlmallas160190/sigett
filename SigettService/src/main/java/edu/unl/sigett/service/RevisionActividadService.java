/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service;

import edu.unl.sigett.entity.RevisionActividad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface RevisionActividadService {

    void guardar(final RevisionActividad revisionActividad);

    void actualizar(final RevisionActividad revisionActividad);

    void eliminar(final RevisionActividad revisionActividad);

    RevisionActividad buscarPorId(final RevisionActividad revisionActividad);

    List<RevisionActividad> buscar(final RevisionActividad revisionActividad);
}
