/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.DocenteDao;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.service.DocenteService;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class DocenteServiceImplement implements DocenteService {

    @EJB
    private DocenteDao docenteDao;

    @Override
    public Docente buscarPorId(final Docente docente) {
        return docenteDao.find(docente.getId());
    }

}
