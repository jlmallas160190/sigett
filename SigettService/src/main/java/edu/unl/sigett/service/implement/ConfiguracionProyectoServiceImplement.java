/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.service.implement;

import edu.unl.sigett.dao.ConfiguracionProyectoDao;
import edu.unl.sigett.entity.ConfiguracionProyecto;
import edu.unl.sigett.service.ConfiguracionProyectoService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jorge-luis
 */
@Stateless
public class ConfiguracionProyectoServiceImplement implements ConfiguracionProyectoService {

    @EJB
    private ConfiguracionProyectoDao configuracionProyectoDao;

    @Override
    public void guardar(final ConfiguracionProyecto configuracionProyecto) {
        this.configuracionProyectoDao.create(configuracionProyecto);
    }

    @Override
    public void actualizar(final ConfiguracionProyecto configuracionProyecto) {
        this.configuracionProyectoDao.edit(configuracionProyecto);
    }

    @Override
    public void eliminar(final ConfiguracionProyecto configuracionProyecto) {
        this.configuracionProyectoDao.remove(configuracionProyecto);
    }

    @Override
    public ConfiguracionProyecto buscarPorId(final ConfiguracionProyecto configuracionProyecto) {
        return configuracionProyectoDao.find(configuracionProyecto.getId());
    }

    @Override
    public List<ConfiguracionProyecto> buscar(final ConfiguracionProyecto configuracionProyecto) {
        List<ConfiguracionProyecto> configuracionProyectos = configuracionProyectoDao.buscar(configuracionProyecto);
        if (configuracionProyectos == null) {
            return new ArrayList<>();
        }
        return configuracionProyectos;
    }

}
