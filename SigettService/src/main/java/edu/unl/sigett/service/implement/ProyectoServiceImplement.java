/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.LineaInvestigacionProyectoDao;
import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import edu.unl.sigett.dao.ProyectoDao;
import edu.unl.sigett.dto.ProyectoDTO;
import edu.unl.sigett.entity.LineaInvestigacionProyecto;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.service.ProyectoService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ProyectoServiceImplement implements ProyectoService {

    @EJB
    private ProyectoDao proyectoDao;
    @EJB
    private ProyectoOfertaCarreraDao proyectoOfertaCarreraDao;
    @EJB
    private LineaInvestigacionProyectoDao lineaInvestigacionProyectoDao;

    @Override
    public void guardar(final Proyecto proyecto) {
        this.proyectoDao.create(proyecto);
    }

    @Override
    public void actualizar(final Proyecto proyecto) {
        this.proyectoDao.edit(proyecto);
    }

    @Override
    public void eliminar(final Proyecto proyecto) {
        this.proyectoDao.remove(proyecto);
    }

    @Override
    public Proyecto buscarPorId(final Proyecto proyecto) {
        return this.proyectoDao.find(proyecto.getId());
    }

    @Override
    public List<Proyecto> buscar(final ProyectoDTO proyectoDTO) {
        List<Proyecto> proyectos = new ArrayList<>();
        List<ProyectoCarreraOferta> pcos = proyectoOfertaCarreraDao.buscar(
                proyectoDTO.getProyectoCarreraOferta() != null ? proyectoDTO.getProyectoCarreraOferta() : null);
        List<LineaInvestigacionProyecto> lips = lineaInvestigacionProyectoDao.buscar(
                proyectoDTO.getLineaInvestigacionProyecto() != null ? proyectoDTO.getLineaInvestigacionProyecto() : null);
        if (pcos == null) {
            return proyectos;
        }
        for (ProyectoCarreraOferta pco : pcos) {
            if (!proyectos.contains(pco.getProyectoId())) {
                if (proyectoDTO.getProyecto().getEstado() != null) {
                    if (pco.getProyectoId().getEstado().equals(proyectoDTO.getProyecto().getEstado())) {
                        proyectos.add(pco.getProyectoId());
                        continue;
                    }
                    continue;
                }
                proyectos.add(pco.getProyectoId());
            }
        }
        if (lips == null) {
            return proyectos;
        }
        for (LineaInvestigacionProyecto lp : lips) {
            if (!proyectos.contains(lp.getProyectoId())) {
                proyectos.add(lp.getProyectoId());
            }
        }
        return proyectos;
    }

}
