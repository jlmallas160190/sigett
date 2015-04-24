/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguridad.controlador;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.unl.sigett.seguridad.managed.session.SessionUsuario;
import com.jlmallas.seguridad.entity.Log;
import com.jlmallas.soporte.entity.Objeto;
import java.io.IOException;
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
import com.jlmallas.seguridad.session.LogFacadeLocal;
import com.jlmallas.soporte.session.ObjetoFacadeLocal;
import com.jlmallas.seguridad.session.UsuarioFacadeLocal;

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
            viewId = "/faces/pages/seguridad/buscarLogs.xhtml"
    )})
public class AdministrarLogs implements Serializable {

    @Inject
    private SessionUsuario sessionUsuario;

    @EJB
    private ObjetoFacadeLocal objetoFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private LogFacadeLocal logFacadeLocal;

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
            int tienePermiso = usuarioFacadeLocal.tienePermiso(sessionUsuario.getUsuario(), "buscar_log");
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

    public List<Objeto> listadoObjetos() {
        try {
            return objetoFacadeLocal.buscarPorOrdenAlfabetico();
        } catch (Exception e) {
        }
        return null;
    }

    public void preProcessPDF(Object document) throws IOException, DocumentException {
        final Document pdf = (Document) document;
        pdf.setPageSize(PageSize.A4.rotate());
        pdf.open();
        PdfPTable pdfTable = new PdfPTable(2);
        pdfTable.setWidthPercentage(40f);
        pdfTable.setHorizontalAlignment(0);
        pdf.add(pdfTable);
        final Phrase phrase = new Phrase("Listado de Logs");
        pdf.add(phrase);
    }

    public void postProcessPDF(Object document) throws IOException, DocumentException {
        final Document pdf = (Document) document;
        pdf.setPageSize(PageSize.A4.rotate());
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

    public SessionUsuario getSessionUsuario() {
        return sessionUsuario;
    }

    public void setSessionUsuario(SessionUsuario sessionUsuario) {
        this.sessionUsuario = sessionUsuario;
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
