/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.Pertinencia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface PertinenciaFacadeLocal {

    void create(Pertinencia pertinencia);

    void edit(Pertinencia pertinencia);

    void remove(Pertinencia pertinencia);

    Pertinencia find(Object id);

    List<Pertinencia> findAll();

    List<Pertinencia> findRange(int[] range);

    List<Pertinencia> buscarPertinenciasPorDocenteProyecto(Long docenteProyectoId);

    int count();

}
