/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.EstructuraCatDocProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface EstructuraCatDocProyectoFacadeLocal {

    void create(EstructuraCatDocProyecto estructuraCatDocProyecto);

    void edit(EstructuraCatDocProyecto estructuraCatDocProyecto);

    void remove(EstructuraCatDocProyecto estructuraCatDocProyecto);

    EstructuraCatDocProyecto find(Object id);

    List<EstructuraCatDocProyecto> findAll();

    List<EstructuraCatDocProyecto> findRange(int[] range);

    int count();

}
