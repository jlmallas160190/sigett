/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.dao;

import edu.unl.sigett.entity.DocenteProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DocenteProyectoDao {

    void create(DocenteProyecto docenteProyecto);

    void edit(DocenteProyecto docenteProyecto);

    void remove(DocenteProyecto docenteProyecto);

    DocenteProyecto find(Object id);

    List<DocenteProyecto> findAll();

    List<DocenteProyecto> findRange(int[] range);

    DocenteProyecto buscarOficioInformeDocenteProyeecto(Long docenteProyectoId);

    List<DocenteProyecto> buscarSinPertinencia(Integer carreraId);

    List<DocenteProyecto> buscarDocenteProyectoParaPertinencia(String ci);

    List<DocenteProyecto> buscarPorDocentePeriodo(String ci, Long ofertaId);

    List<DocenteProyecto> buscar(final DocenteProyecto docenteProyecto);

    @Override
    public boolean equals(Object obj);

    int count();

}
