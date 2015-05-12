/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.dao;

import edu.jlmallas.academico.entity.Titulo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author JorgeLuis
 */
@Local
public interface TituloDao {

    void create(Titulo titulo);

    void edit(Titulo titulo);

    void remove(Titulo titulo);

    Titulo find(Object id);

    List<Titulo> findAll();

    List<Titulo> findRange(int[] range);

    List<Titulo> buscar(Titulo titulo);

    int count();

}
