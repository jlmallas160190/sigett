/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.logs;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.usuario.UsuarioDM;
import org.jlmallas.seguridad.entity.Log;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.jlmallas.seguridad.dao.LogDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import org.jlmallas.seguridad.service.UsuarioService;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "logs",
            pattern = "/logs/",
            viewId = "/faces/pages/buscarLogs.xhtml"
    )})
public class AdministrarLogs implements Serializable {

    @Inject
    private UsuarioDM sessionUsuario;
    @EJB(lookup = "java:global/SeguridadService/UsuarioServiceImplement!org.jlmallas.seguridad.service.UsuarioService")
    private UsuarioService usuarioService;
    @EJB(lookup = "java:global/SeguridadService/LogDaoImplement!org.jlmallas.seguridad.dao.LogDao")
    private LogDao logFacadeLocal;

    private Date fecha;
    private Date fechaFin;

    private List<Log> logs;

    private String tabla = "";
    private String tablaId;

    public AdministrarLogs() {

    }

    public String historico(String tabla, String tablaId) {
        String navegacion = "";
        if (!tablaId.equalsIgnoreCase("")) {
            this.tabla = tabla;
            this.tablaId = tablaId;
            buscarByEntity(tabla, tablaId);
            navegacion = "pretty:logs";
        }
        return navegacion;
    }

    public List<Log> listadoLogs(String tabla, Date fechaInicio, Date fechaFin) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaFormat = formatoFecha.format(fechaInicio);
            String fechaFinFormat = formatoFecha.format(fechaFin);
            fechaInicio = formatoFecha.parse(fechaFormat);
            fechaFin = formatoFecha.parse(fechaFinFormat);
            int tienePermiso = usuarioService.tienePermiso(sessionUsuario.getUsuario(), "buscar_log");
            if (tienePermiso == 1) {
                return logFacadeLocal.buscarPorTablaFecha(tabla, fechaInicio, fechaFin);
            } else {
                if (tienePermiso == 2) {
                    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                    FacesContext.getCurrentInstance().responseComplete();
                    response.sendRedirect("admin.xhtml");
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No tiene permisos para listar Logs", "");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                    FacesContext.getCurrentInstance().responseComplete();
                    response.sendRedirect("loginAdmin.xhtml");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void buscarByEntity(String tabla, String tablaId) {
        this.logs = new ArrayList<>();
        try {
            this.logs = logFacadeLocal.buscarPorTablaId(tabla, tablaId);
        } catch (Exception e) {
        }
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getTablaId() {
        return tablaId;
    }

    public void setTablaId(String tablaId) {
        this.tablaId = tablaId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

}
