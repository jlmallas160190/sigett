/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jlmallas.academico.service.implement;

import edu.jlmallas.academico.dao.AreaDao;
import edu.jlmallas.academico.entity.Area;
import edu.jlmallas.academico.service.AreaService;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author JorgeLuis
 */
@Stateless
public class AreaServiceImplement implements AreaService {

    @EJB
    private AreaDao areaDao;

    @Override
    public void guardar(Area area) {
        areaDao.create(area);
    }

    @Override
    public void actualizar(Area area) {
        areaDao.edit(area);
    }

    @Override
    public void eliminar(Area area) {
        Area areaBuscar = areaDao.find(area.getId());
        if (areaBuscar.getCarreraList() != null) {

        }
        areaDao.remove(area);
    }

    @Override
    public Area buscarPorId(Integer id) {
        return areaDao.find(id);
    }

    @Override
    public List<Area> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Area> buscarPorCriterio(Area area) {
        return areaDao.buscarPorCriterio(area);
    }

}
