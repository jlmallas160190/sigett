/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service;

import edu.jlmallas.academico.entity.Docente;
import javax.ejb.Local;

/**
 *
 * @author jorge-luis
 */
@Local
public interface DocenteService {
    Docente buscarPorId(final Docente docente);
}
