/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.LineaInvestigacionDocenteDao;
import edu.unl.sigett.entity.LineaInvestigacionDocente;
import edu.unl.sigett.service.LineaInvestigacionDocenteService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class LineaInvestigacionDocenteServiceImplement implements LineaInvestigacionDocenteService {

    @EJB
    private LineaInvestigacionDocenteDao lineaInvestigacionDocenteDao;

    @Override
    public void guardar(final LineaInvestigacionDocente lineaInvestigacionDocente) {
        this.lineaInvestigacionDocenteDao.create(lineaInvestigacionDocente);
    }

    @Override
    public void actualizar(final LineaInvestigacionDocente lineaInvestigacionDocente) {
        this.lineaInvestigacionDocenteDao.edit(lineaInvestigacionDocente);
    }

    @Override
    public void eliminar(LineaInvestigacionDocente lineaInvestigacionDocente) {
        this.lineaInvestigacionDocenteDao.remove(lineaInvestigacionDocente);
    }

    @Override
    public LineaInvestigacionDocente buscarPorId(final LineaInvestigacionDocente lineaInvestigacionDocente) {
        return this.lineaInvestigacionDocenteDao.find(lineaInvestigacionDocente.getId());
    }

    @Override
    public List<LineaInvestigacionDocente> buscar(final LineaInvestigacionDocente lineaInvestigacionDocente) {
        return this.lineaInvestigacionDocenteDao.buscar(lineaInvestigacionDocente);
    }

}
