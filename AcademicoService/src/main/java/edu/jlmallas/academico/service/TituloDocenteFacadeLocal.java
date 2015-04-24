/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.TituloDocente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface TituloDocenteFacadeLocal {

    void create(TituloDocente tituloDocente);

    void edit(TituloDocente tituloDocente);

    void remove(TituloDocente tituloDocente);

    TituloDocente find(Object id);

    List<TituloDocente> findAll();

    List<TituloDocente> findRange(int[] range);

    TituloDocente buscarPorTitulo(String nombre);

    int count();

}
