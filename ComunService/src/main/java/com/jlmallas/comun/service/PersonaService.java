/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service;

import com.jlmallas.comun.entity.Persona;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface PersonaService {

    void guardar(final Persona persona);

    void actualizar(final Persona persona);

    void eliminar(final Persona persona);

    Persona buscarPorId(final Persona persona);

    List<Persona> buscar(final Persona persona);
}
