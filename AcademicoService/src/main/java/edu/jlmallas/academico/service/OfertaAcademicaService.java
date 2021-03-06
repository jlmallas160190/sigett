/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.OfertaAcademica;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface OfertaAcademicaService {

    void create(OfertaAcademica ofertaAcademica);

    void edit(OfertaAcademica ofertaAcademica);

    void remove(OfertaAcademica ofertaAcademica);

    OfertaAcademica find(Object id);

    List<OfertaAcademica> findAll();

    List<OfertaAcademica> findRange(int[] range);

    OfertaAcademica buscarPorIdSga(String id);

    List<OfertaAcademica> buscarPorPeriodoActual();

    List<OfertaAcademica> buscar(final OfertaAcademica ofertaAcademica);

    OfertaAcademica ultimaOfertaPorFechaYPeriodoLectivo(Integer periodoId);

    OfertaAcademica primerOfertaPorFechaYPeriodoLectivo(Integer periodoId);
}
