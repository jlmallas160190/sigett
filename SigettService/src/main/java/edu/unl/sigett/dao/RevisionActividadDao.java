/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.RevisionActividad;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface RevisionActividadDao {

    void create(final RevisionActividad revisionActividad);

    void edit(final RevisionActividad revisionActividad);

    void remove(final RevisionActividad revisionActividad);

    RevisionActividad find(Object id);

    List<RevisionActividad> findAll();

    List<RevisionActividad> findRange(int[] range);

    List<RevisionActividad> buscar(final RevisionActividad revisionActividad);

}
