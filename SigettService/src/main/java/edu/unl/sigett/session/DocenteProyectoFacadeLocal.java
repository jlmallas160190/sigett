/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.session;

import edu.unl.sigett.entity.DocenteProyecto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface DocenteProyectoFacadeLocal {

    void create(DocenteProyecto docenteProyecto);

    void edit(DocenteProyecto docenteProyecto);

    void remove(DocenteProyecto docenteProyecto);

    DocenteProyecto find(Object id);

    List<DocenteProyecto> findAll();

    List<DocenteProyecto> findRange(int[] range);

    List<DocenteProyecto> buscarPorProyecto(Long proyectoId);

    List<DocenteProyecto> buscarProyectosPorDocente(Long docenteId);

    DocenteProyecto buscarOficioInformeDocenteProyeecto(Long docenteProyectoId);

    List<DocenteProyecto> buscarSinPertinencia(Integer carreraId);

    List<DocenteProyecto> buscarDocenteProyectoParaPertinencia(String ci);

    List<DocenteProyecto> buscarPorDocentePeriodo(String ci,Long ofertaId);

    @Override
    public boolean equals(Object obj);

    int count();

}
