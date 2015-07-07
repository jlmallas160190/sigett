/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jlmallas.comun.service.implement;

import com.jlmallas.comun.dao.PersonaDao;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.service.PersonaService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class PersonaServiceImplement implements PersonaService {

    @EJB
    private PersonaDao personaDao;

    @Override
    public void guardar(final Persona persona) {
        this.personaDao.create(persona);
    }

    @Override
    public void actualizar(final Persona persona) {
        this.personaDao.edit(persona);
    }

    @Override
    public void eliminar(final Persona persona) {
        this.personaDao.remove(persona);
    }

    @Override
    public Persona buscarPorId(final Persona persona) {
        return this.personaDao.find(persona.getId());
    }

    @Override
    public List<Persona> buscar(final Persona persona) {
        return this.personaDao.buscar(persona);
    }

    @Override
    public Boolean esUnico(final String numeroIdentificacion, Long id) {
       return this.personaDao.esUnico(numeroIdentificacion, id);
    }

    @Override
    public Persona buscarPorNumeroIdentificacion(final String numeroIdentificacion) {
      return this.personaDao.buscarPorNumeroIdentificacion(numeroIdentificacion);
    }

}
