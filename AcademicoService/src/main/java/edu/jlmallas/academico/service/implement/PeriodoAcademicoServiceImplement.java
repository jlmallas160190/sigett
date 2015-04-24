/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.PeriodoAcademicoDao;
import edu.jlmallas.academico.entity.PeriodoAcademico;
import edu.jlmallas.academico.service.PeriodoAcademicoService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class PeriodoAcademicoServiceImplement implements PeriodoAcademicoService {

    @EJB
    private PeriodoAcademicoDao periodoAcademicoDao;

    @Override
    public void guardar(PeriodoAcademico periodoAcademico) {
        periodoAcademicoDao.create(periodoAcademico);
    }

    @Override
    public void actualizar(PeriodoAcademico periodoAcademico) {
        periodoAcademicoDao.edit(periodoAcademico);
    }

    @Override
    public void eliminar(PeriodoAcademico periodoAcademico) {
        if (periodoAcademico.getOfertaAcademicaList().isEmpty()) {
            periodoAcademicoDao.remove(periodoAcademico);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No puede eliminar", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    @Override
    public PeriodoAcademico buscarPorId(Object id) {
        return periodoAcademicoDao.find(id);
    }

    @Override
    public List<PeriodoAcademico> buscarPorCriterio(PeriodoAcademico periodoAcademico) {
        return periodoAcademicoDao.buscarPorCriterio(periodoAcademico);
    }

    @Override
    public PeriodoAcademico buscarPorIdSga(PeriodoAcademico periodoAcademico) {
        return periodoAcademicoDao.buscarPorIdSga(periodoAcademico);
       
    }

}
