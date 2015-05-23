/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.LineaInvestigacionProyectoDao;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import edu.unl.sigett.service.LineaInvestigacionProyectoService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class LineaInvestigacionProyectoServiceImplement implements LineaInvestigacionProyectoService {

    @EJB
    private LineaInvestigacionProyectoDao lineaInvestigacionProyectoDao;

    @Override
    public void guardar(final LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        this.lineaInvestigacionProyectoDao.create(lineaInvestigacionProyecto);
    }

    @Override
    public void actulizar(final LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        this.lineaInvestigacionProyectoDao.edit(lineaInvestigacionProyecto);
    }

    @Override
    public void eliminar(final LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        this.lineaInvestigacionProyectoDao.remove(lineaInvestigacionProyecto);
    }

    @Override
    public LineaInvestigacionProyecto buscarPorId(final LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        return this.lineaInvestigacionProyectoDao.find(lineaInvestigacionProyecto.getId());
    }

    @Override
    public List<LineaInvestigacionProyecto> buscar(final LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        return this.lineaInvestigacionProyectoDao.buscar(lineaInvestigacionProyecto);
    }

    @Override
    public Integer count(LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        List<LineaInvestigacionProyecto> lips = this.lineaInvestigacionProyectoDao.buscar(lineaInvestigacionProyecto);
        if (lips == null) {
            return 0;
        }
        return lips.size();
    }

    @Override
    public List<LineaInvestigacion> buscarLineaInvestigacion(final LineaInvestigacionProyecto lineaInvestigacionProyecto) {
        List<LineaInvestigacionProyecto> lineaInvestigacionProyectos = lineaInvestigacionProyectoDao.buscar(lineaInvestigacionProyecto);
        List<LineaInvestigacion> lineaInvestigacions = new ArrayList<>();
        if (lineaInvestigacionProyectos == null) {
            return lineaInvestigacions;
        }
        for (LineaInvestigacionProyecto lp : lineaInvestigacionProyectos) {
            if (!lineaInvestigacions.contains(lp.getLineaInvestigacionId())) {
                lineaInvestigacions.add(lp.getLineaInvestigacionId());
            }
        }
        return lineaInvestigacions;
    }

}
