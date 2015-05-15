/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.reportes;

import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.CatalogoOficio;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.OficioCarrera;
import edu.unl.sigett.entity.Pertinencia;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import edu.unl.sigett.dao.ActaFacadeLocal;
import edu.unl.sigett.dao.CatalogoOficioFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.OficioCarreraFacadeLocal;
import edu.unl.sigett.entity.Acta;
import edu.unl.sigett.entity.ConfiguracionArea;
import edu.unl.sigett.entity.RangoEquivalencia;
import org.jlmallas.seguridad.entity.Usuario;
import edu.unl.sigett.entity.CatalogoActa;
import edu.unl.sigett.enumeration.CatalogoActaEnum;
import edu.unl.sigett.enumeration.CatalogoOficioEnum;
import edu.unl.sigett.dao.CategoriaActaFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionAreaFacadeLocal;
import java.util.Date;

/**
 *
 * @author JorgeLuis
 */
public class AdministrarReportes implements Serializable {

    public void oficioDocenteProyecto(HttpServletResponse response, Map datosReporte, OficioCarrera oficioCarrera, String path, String pathSetting,
            String tipo, Integer nroOficio, Carrera carrera, String fecha, String plazo,
            String usuario, ConfiguracionCarreraDao configuracionCarreraFacadeLocal, ConfiguracionCarrera configuracionCarrera,
            Long tablaId, OficioCarreraFacadeLocal oficioCarreraFacadeLocal, CatalogoOficioFacadeLocal catalogoOficioFacadeLocal) {
        try {
            byte[] bytes = null;
            JasperPrint jasperPrint = null;
            Calendar fechaActual = Calendar.getInstance();
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(carrera.getLogo());
            p.put("lugarFecha", carrera.getLugar() + ", " + fecha);
            p.put("carrera", carrera.getNombre().toUpperCase());
            p.put("carreraSigla", carrera.getSigla());
            p.put("carreraLogo", is);
            p.put("area", carrera.getAreaId().getNombre());
            p.put("tituloDocente", datosReporte.get("tituloDocente"));
            p.put("docente", datosReporte.get("docente"));
            p.put("articulos", datosReporte.get("articulos_pertinencia_tt"));
            p.put("asunto", datosReporte.get("asunto_pertinencia_tt"));
            p.put("nota", datosReporte.get("nota_pertinencia_tt"));
            p.put("plazo", plazo);
            p.put("proyecto", "<b>" + datosReporte.get("temaProyecto") + "</b>");
            p.put("usuario", usuario.toUpperCase());
            p.put("autores", datosReporte.get("autores"));
            p.put("tituloCoordinador", datosReporte.get("tituloCoordinador"));
            p.put("coordinador", datosReporte.get("coordinador"));
            p.put("tituloOtorgar", carrera.getNombreTitulo());
            p.put("sello", path + "/resources/img/selloInstitucion.png");
            p.put("nroOficio", String.valueOf(nroOficio) + "-" + datosReporte.get("abreviacion_oficio") + "-" + carrera.getSigla());
            File fileReport = new File(path + "/reportes/postulacion/oficio_docente_proyecto.jasper");

            if (oficioCarrera == null) {
                oficioCarrera = new OficioCarrera();
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                /*Grabar Oficio*/
                grabarOficio(oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, CatalogoOficioEnum.DOCENTEPROYECTO.getTipo(), nroOficio, fechaActual.getTime(), nroOficio + "", bytes, tablaId);
                actualizarNroOficio(configuracionCarrera, configuracionCarreraFacadeLocal);
            } else {
                bytes = oficioCarrera.getOficio();
            }

            if (tipo.equalsIgnoreCase("pdf")) {
                response.setCharacterEncoding("ISO-8859-1");
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    response.setCharacterEncoding("ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=oficioDocenteProyecto.docx");
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void responseProrrogaAutorProyecto(String tipo, String fecha, HttpServletResponse response, Map datosReporte, ConfiguracionCarreraDao configuracionCarreraFacadeLocal,
            ConfiguracionCarrera configuracionCarrera, OficioCarrera oficioCarrera, Long tablaId, OficioCarreraFacadeLocal oficioCarreraFacadeLocal,
            CatalogoOficioFacadeLocal catalogoOficioFacadeLocal, Carrera carrera, Integer nroOficio, String secretario,
            String fechaEmision, String resolucion, String path, String pathSetting) {
        try {
            Calendar fechaActual = Calendar.getInstance();
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(carrera.getLogo());
            p.put("lugarFecha", carrera.getLugar() + ", " + fecha);
            p.put("carrera", carrera.getNombre().toUpperCase());
            p.put("carreraSigla", carrera.getSigla());
            p.put("tituloOtorgar", carrera.getNombreTitulo());
            p.put("carreraLogo", is);
            p.put("area", carrera.getAreaId().getNombre());
            p.put("tituloCoordinador", datosReporte.get("tituloCoordinador"));
            p.put("coordinador", datosReporte.get("coordinador"));
            p.put("resolucion", "<b>" + resolucion + "</b>");
            p.put("proyecto", "<b>" + datosReporte.get("tema_proyecto") + "</b>");
            p.put("nroOficio", String.valueOf(nroOficio) + "-" + datosReporte.get("abreviacion_oficio") + "-" + carrera.getSigla());
            p.put("secretario", secretario);
            p.put("fechaEmision", fechaEmision);
            p.put("sello", path + "/resources/img/selloInstitucion.png");
            p.put("autores", datosReporte.get("autores"));

            byte[] bytes = null;
            File fileReport = new File(path + "/reportes/adjudicacion/response_autor_prorroga.jasper");
            if (oficioCarrera == null) {
                oficioCarrera = new OficioCarrera();
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                /*Grabar Oficio*/
                grabarOficio(oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, CatalogoOficioEnum.RESPUESTAPRORROGAAUTOR.getTipo(), nroOficio, fechaActual.getTime(), nroOficio + "", bytes, tablaId);
                actualizarNroOficio(configuracionCarrera, configuracionCarreraFacadeLocal);
            } else {
                bytes = oficioCarrera.getOficio();
            }

            if (tipo.equalsIgnoreCase("pdf")) {
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                response.setCharacterEncoding("ISO-8859-1");
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    response.setCharacterEncoding("ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=oficioProrroga.docx");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void informeDocenteProyecto(String tipo, String fecha, String fechaOficio, Pertinencia pertinencia, HttpServletResponse response,
            Map datosReporte, ConfiguracionCarreraDao configuracionCarreraFacadeLocal, ConfiguracionCarrera configuracionCarrera, OficioCarrera oficioCarrera,
            OficioCarreraFacadeLocal oficioCarreraFacadeLocal, CatalogoOficioFacadeLocal catalogoOficioFacadeLocal, Carrera carrera, Integer nroOficio, String path, String resolucion) {
        try {
            Calendar fechaActual = Calendar.getInstance();
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(carrera.getLogo());
            p.put("lugarFecha", carrera.getLugar() + ", " + fecha);
            p.put("carrera", carrera.getNombre().toUpperCase());
            p.put("nroOficio", nroOficio + "");
            p.put("fechaOficio", fechaOficio);
            p.put("observacion", pertinencia.getObservacion());
            p.put("resolucion", resolucion);
            p.put("carreraSigla", carrera.getSigla());
            p.put("carreraLogo", is);
            p.put("area", carrera.getAreaId().getNombre());
            p.put("tituloDocente", datosReporte.get("tituloDocente"));
            p.put("docente", datosReporte.get("docente"));
            p.put("proyecto", "<b>" + datosReporte.get("temaProyecto") + "</b>");
            p.put("autores", datosReporte.get("autores"));
            p.put("tituloCoordinador", datosReporte.get("tituloCoordinador"));
            p.put("coordinador", datosReporte.get("coordinador"));
            p.put("cedulaDocente", datosReporte.get("cedulaDocente"));
            p.put("sello", path + "/resources/img/selloInstitucion.png");

            byte[] bytes = null;
            File fileReport = new File(path + "/reportes/postulacion/informe_docente_proyecto.jasper");
            if (oficioCarrera == null) {
                oficioCarrera = new OficioCarrera();
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                /*Grabar Oficio*/
                grabarOficio(oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, CatalogoOficioEnum.PERTINENCIA.getTipo(), nroOficio, fechaActual.getTime(), nroOficio + "", bytes, pertinencia.getId());
                actualizarNroOficio(configuracionCarrera, configuracionCarreraFacadeLocal);
            } else {
                bytes = oficioCarrera.getOficio();
            }
            if (tipo.equalsIgnoreCase("pdf")) {
                response.setCharacterEncoding("ISO-8859-1");
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    response.setCharacterEncoding("ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=informeDocenteProyecto.docx");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();
                }
            }

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void oficioDirectorProrroga(String tipo, String fecha, HttpServletResponse response, Map datosReporte,
            ConfiguracionCarreraDao configuracionCarreraFacadeLocal, ConfiguracionCarrera configuracionCarrera, OficioCarrera oficioCarrera,
            Long tablaId, OficioCarreraFacadeLocal oficioCarreraFacadeLocal, CatalogoOficioFacadeLocal catalogoOficioFacadeLocal,
            Carrera carrera, Integer nroOficio, String secretario, String path, String pathSetting) {
        try {
            Calendar fechaActual = Calendar.getInstance();
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(carrera.getLogo());
            p.put("lugarFecha", carrera.getLugar() + ", " + fecha);
            p.put("carrera", carrera.getNombre().toUpperCase());
            p.put("carreraSigla", carrera.getSigla());
            p.put("carreraLogo", is);
            p.put("area", carrera.getAreaId().getNombre());
            p.put("tituloDocente", datosReporte.get("tituloDocente"));
            p.put("docente", datosReporte.get("docente"));
            p.put("tituloCoordinador", datosReporte.get("tituloCoordinador"));
            p.put("coordinador", datosReporte.get("coordinador"));
            p.put("asunto", datosReporte.get("asunto_prorroga_tt"));
            p.put("nota", datosReporte.get("nota_prorroga_tt"));
            p.put("proyecto", "<b>" + datosReporte.get("temaProyecto") + "</b>");
            p.put("nroOficio", String.valueOf(nroOficio) + "-" + datosReporte.get("abreviacion_oficio") + "-" + carrera.getSigla());
            p.put("usuario", secretario);
            p.put("sello", path + "/resources/img/selloInstitucion.png");
            p.put("autores", datosReporte.get("autores"));
            byte[] bytes = null;
            File fileReport = new File(path + "/reportes/adjudicacion/oficio_prorroga.jasper");

            if (oficioCarrera == null) {
                oficioCarrera = new OficioCarrera();
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                /*Grabar Oficio*/
                grabarOficio(oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, CatalogoOficioEnum.PRORROGA.getTipo(), nroOficio, fechaActual.getTime(), nroOficio + "", bytes, tablaId);
                actualizarNroOficio(configuracionCarrera, configuracionCarreraFacadeLocal);
            } else {
                bytes = oficioCarrera.getOficio();
            }

            if (tipo.equalsIgnoreCase("pdf")) {
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                response.setCharacterEncoding("ISO-8859-1");
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    response.setCharacterEncoding("ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=oficioProrroga.docx");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();
                }
            }

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void informeDirectorProrroga(String tipo, String fechaOficio, String objetivos, String avance, String resolucion, String fecha,
            HttpServletResponse response, Map datosReporte, ConfiguracionCarreraDao configuracionCarreraFacadeLocal, ConfiguracionCarrera configuracionCarrera,
            OficioCarrera oficioCarrera, Long tablaId, OficioCarreraFacadeLocal oficioCarreraFacadeLocal, CatalogoOficioFacadeLocal catalogoOficioFacadeLocal,
            Carrera carrera, Integer nroOficio, String secretario, Long prorrogaId, String path, String pathSetting) {
        try {
            Calendar fechaActual = Calendar.getInstance();
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(carrera.getLogo());
            p.put("lugarFecha", carrera.getLugar() + ", " + fecha);
            p.put("carrera", carrera.getNombre().toUpperCase());
            p.put("carreraSigla", carrera.getSigla());
            p.put("carreraLogo", is);
            p.put("area", carrera.getAreaId().getNombre());
            p.put("asunto", resolucion);
            p.put("nroOficio", oficioCarrera.getNumeroOficio() + "-" + datosReporte.get("abreviacion_oficio") + "-" + carrera.getSigla());
            p.put("fechaOficio", fechaOficio);
            p.put("avance", avance);
            p.put("objetivos", objetivos);
            p.put("autores", datosReporte.get("autores"));
            p.put("tituloDocente", datosReporte.get("tituloDocente"));
            p.put("docente", datosReporte.get("docente"));
            p.put("tituloCoordinador", datosReporte.get("tituloCoordinador"));
            p.put("coordinador", datosReporte.get("coordinador"));
            p.put("sello", path + "/resources/img/selloInstitucion.png");
            byte[] bytes = null;
            File fileReport = new File(path + "/reportes/adjudicacion/informe_director_prorroga.jasper");
            if (oficioCarrera == null) {
                oficioCarrera = new OficioCarrera();
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                /*Grabar Oficio*/
                grabarOficio(oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, CatalogoOficioEnum.INFORMEDIRECTORPRORROGA.getTipo(), nroOficio, fechaActual.getTime(), nroOficio + "", bytes, tablaId);
                actualizarNroOficio(configuracionCarrera, configuracionCarreraFacadeLocal);
            } else {
                bytes = oficioCarrera.getOficio();
            }

            if (tipo.equalsIgnoreCase("pdf")) {
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                response.setCharacterEncoding("ISO-8859-1");
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    response.setCharacterEncoding("ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=oficioProrroga.docx");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();
                }
            }

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void oficioDirectorProyecto(HttpServletResponse response, Map datosReporte, String fecha, ConfiguracionCarreraDao configuracionCarreraFacadeLocal,
            ConfiguracionCarrera configuracionCarrera, String tipo, OficioCarrera oficioCarrera, Long tablaId, OficioCarreraFacadeLocal oficioCarreraFacadeLocal,
            CatalogoOficioFacadeLocal catalogoOficioFacadeLocal, Carrera carrera, Integer nroOficio, String usuario, String path, String pathSetting) {
        try {
            InputStream is = new ByteArrayInputStream(carrera.getLogo());
            Calendar fechaActual = Calendar.getInstance();
            Map p = new HashMap();
            p.put("lugarFecha", carrera.getLugar() + ", " + fecha);
            p.put("carrera", carrera.getNombre().toUpperCase());
            p.put("carreraSigla", carrera.getSigla());
            p.put("carreraLogo", is);
            p.put("area", carrera.getAreaId().getNombre());
            p.put("tituloDocente", datosReporte.get("tituloDocente"));
            p.put("docente", datosReporte.get("docente"));
            p.put("proyecto", datosReporte.get("temaProyecto"));
            p.put("autores", datosReporte.get("autores"));
            p.put("usuario", usuario);
            p.put("tituloCoordinador", datosReporte.get("tituloCoordinador"));
            p.put("coordinador", datosReporte.get("coordinador"));
            p.put("sello", path + "/resources/img/selloInstitucion.png");
            p.put("articulos", datosReporte.get("articulos_adjudicacion_tt"));
            p.put("asunto", datosReporte.get("asunto_adjudicacion_tt"));
            p.put("nota", datosReporte.get("nota_adjudicacion_tt"));
            p.put("tituloOtorgar", carrera.getNombreTitulo());
            p.put("nroOficio", String.valueOf(nroOficio) + "-" + datosReporte.get("abreviacion_oficio") + "-" + carrera.getSigla());

            byte[] bytes = null;
            File fileReport = new File(path + "/reportes/adjudicacion/oficio_director_proyecto.jasper");
            if (oficioCarrera == null) {
                oficioCarrera = new OficioCarrera();
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                /*Grabar Oficio*/
                grabarOficio(oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, CatalogoOficioEnum.DIRECTORPROYECTO.getTipo(), nroOficio, fechaActual.getTime(), nroOficio + "", bytes, tablaId);
                actualizarNroOficio(configuracionCarrera, configuracionCarreraFacadeLocal);
            } else {
                bytes = oficioCarrera.getOficio();
            }
            if (tipo.equalsIgnoreCase("pdf")) {
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                response.setCharacterEncoding("ISO-8859-1");
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    response.setCharacterEncoding("ISO-8859-1");
                    response.setContentType("text/html;charset=ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=oficioDirectorProyecto.docx");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();

                }
            }
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void oficioMiembroTribunalSprivada(String tipo, String fecha, HttpServletResponse response, Map datosReporte, ConfiguracionCarreraDao configuracionCarreraFacadeLocal,
            ConfiguracionCarrera configuracionCarrera, OficioCarrera oficioCarrera, String presidente, String miembros, Long tablaId,
            OficioCarreraFacadeLocal oficioCarreraFacadeLocal, CatalogoOficioFacadeLocal catalogoOficioFacadeLocal, Carrera carrera, Integer nroOficio, String secretario, String path, String pathSetting) {
        try {
            Calendar fechaActual = Calendar.getInstance();
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(carrera.getLogo());
            p.put("lugarFecha", carrera.getLugar() + ", " + fecha);
            p.put("miembros", miembros);
            p.put("presidente", presidente);
            p.put("articulos", datosReporte.get("articulos_designar_miembro_tribunal"));
            p.put("asunto", datosReporte.get("asunto_designar_miembro_tribunal"));
            p.put("nota", datosReporte.get("nota_designar_miembro_tribunal"));
            p.put("nOficio", String.valueOf(nroOficio) + "-" + datosReporte.get("abreviacion_oficio") + "-" + carrera.getSigla());
            p.put("sello", path + "/resources/img/selloInstitucion.png");
            p.put("secretario", secretario);
            p.put("autores", datosReporte.get("autores"));
            p.put("carrera", carrera.getNombre());
            p.put("carreraSigla", carrera.getSigla());
            p.put("carreraLogo", is);
            p.put("area", carrera.getAreaId().getNombre());
            p.put("tituloMiembro", datosReporte.get("tituloMiembro"));
            p.put("miembro", datosReporte.get("miembro"));
            p.put("cargoMiembro", datosReporte.get("cargoMiembro"));
            p.put("tituloCoordinador", datosReporte.get("tituloCoordinador"));
            p.put("coordinador", datosReporte.get("coordinador"));
            p.put("tituloOtorgar", carrera.getNombreTitulo());
            p.put("proyecto", "<b>" + datosReporte.get("temaProyecto") + "</b>");
            File fileReport = new File(path + "/reportes/finalizacion/oficio_miembro_tribunal_sprivada.jasper");
            byte[] bytes = null;
            if (oficioCarrera == null) {
                oficioCarrera = new OficioCarrera();
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                /*Grabar Oficio*/
                grabarOficio(oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, CatalogoOficioEnum.MIEMBROTRIBUNALPRIVADA.getTipo(), nroOficio, fechaActual.getTime(), nroOficio + "", bytes, tablaId);
                actualizarNroOficio(configuracionCarrera, configuracionCarreraFacadeLocal);
            } else {
                bytes = oficioCarrera.getOficio();
            }
            if (tipo.equalsIgnoreCase("pdf")) {
                response.setCharacterEncoding("ISO-8859-1");
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    response.setCharacterEncoding("ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=oficioDocenteProyecto.docx");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();
                }
            }

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void certificacionDirector(String tipo, String fecha, HttpServletResponse response, Map datosReporte,
            Carrera carrera, String path, String pathSetting) {
        try {
            Map p = new HashMap();
            p.put("lugarFecha", carrera.getLugar() + ", " + fecha);
            p.put("director", datosReporte.get("tituloDirector") + " " + datosReporte.get("tituloDirector"));
            p.put("razon", datosReporte.get("razon"));
            p.put("autores", datosReporte.get("autores"));
            p.put("carrera", carrera.getNombre().toLowerCase());
            p.put("proyecto", "<b>" + datosReporte.get("temaProyecto") + "</b>");
            File fileReport = new File(path + "/reportes/finalizacion/certificacion.jasper");
            byte[] bytes = null;

            if (tipo.equalsIgnoreCase("pdf")) {
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                response.setCharacterEncoding("ISO-8859-1");
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    response.setCharacterEncoding("ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=oficioDocenteProyecto.docx");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();
                }
            }

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void oficioMiembroTribunalSpublica(String tipo, String fecha, String fechaSustentacion, String lugarSustentacion, HttpServletResponse response, Map datosReporte,
            ConfiguracionCarreraDao configuracionCarreraFacadeLocal, ConfiguracionCarrera configuracionCarrera, OficioCarrera oficioCarrera,
            String presidente, String miembros, Long tablaId, OficioCarreraFacadeLocal oficioCarreraFacadeLocal, CatalogoOficioFacadeLocal catalogoOficioFacadeLocal,
            Carrera carrera, Integer nroOficio, String secretario, String path, String pathSetting) {
        try {
            Calendar fechaActual = Calendar.getInstance();
            Map p = new HashMap();
            InputStream is = new ByteArrayInputStream(carrera.getLogo());
            p.put("lugarFecha", carrera.getLugar() + ", " + fecha);
            p.put("miembros", miembros);
            p.put("presidente", presidente);
            p.put("articulos", datosReporte.get("articulos_sustentacion_publica"));
            p.put("fechaSustentacion", fechaSustentacion);
            p.put("lugarSustentacion", lugarSustentacion);
            p.put("nOficio", String.valueOf(nroOficio) + "-" + datosReporte.get("abreviacion_oficio") + "-" + carrera.getSigla());
            p.put("sello", path + "/resources/img/selloInstitucion.png");
            p.put("secretario", secretario);
            p.put("autores", datosReporte.get("autores"));
            p.put("carrera", carrera.getNombre().toUpperCase());
            p.put("carreraSigla", carrera.getSigla());
            p.put("carreraLogo", is);
            p.put("area", carrera.getAreaId().getNombre());
            p.put("tituloMiembro", datosReporte.get("tituloMiembro"));
            p.put("miembro", datosReporte.get("nombresCompletosMiembro"));
            p.put("cargoMiembro", datosReporte.get("cargoMiembro"));
            p.put("tituloCoordinador", datosReporte.get("tituloCoordinador"));
            p.put("coordinador", datosReporte.get("coordinador"));
            p.put("tituloOtorgar", carrera.getNombreTitulo());
            p.put("proyecto", "<b>" + datosReporte.get("temaProyecto") + "</b>");
            File fileReport = new File(path + "/reportes/finalizacion/oficio_miembro_tribunal_spublica.jasper");
            byte[] bytes = null;
            if (oficioCarrera == null) {
                oficioCarrera = new OficioCarrera();
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                /*Grabar Oficio*/
                grabarOficio(oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, CatalogoOficioEnum.MIEMBROTRIBUNALPUBLICA.getTipo(), nroOficio, fechaActual.getTime(), nroOficio + "", bytes, tablaId);
                actualizarNroOficio(configuracionCarrera, configuracionCarreraFacadeLocal);
            } else {
                bytes = oficioCarrera.getOficio();
            }
            if (tipo.equalsIgnoreCase("pdf")) {
                response.setCharacterEncoding("ISO-8859-1");
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    response.setCharacterEncoding("ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=oficioSustentacionPublica.docx");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();
                }
            }

        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, ex.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void actaGrado(String tipo, HttpServletResponse response, EvaluacionTribunal evaluacionTribunal, ConfiguracionAreaFacadeLocal configuracionAreaFacadeLocal, ConfiguracionCarreraDao configuracionCarreraFacadeLocal, ActaFacadeLocal actaFacadeLocal, CategoriaActaFacadeLocal categoriaActaFacadeLocal, Carrera carrera, String presidente, String presidenteCargo, String miembros, String miembrosCargo, String autor, String autorCargo,
            String notasPublica, double promedioPrivada, double promedioEstudio, String secretario, String generoAutor, String nacionalidadAutor, String ciAutor, String fechaInicioEstudio, String fechaFinEstudio, String path, String pathSetting, String lenguaje, Usuario usuario) {
        try {
            String nivel = "";
            String equivalencia = "";
            String declaracion = "";
            double promedioTotal = 0.0;
            promedioTotal = (evaluacionTribunal.getNota() + promedioPrivada + promedioEstudio) / 3;
            promedioTotal = Math.round(promedioTotal * 100);
            promedioTotal = promedioTotal / 100;
            for (RangoEquivalencia rangoEquivalencia : evaluacionTribunal.getRangoNotaId().getRangoEquivalenciaList()) {
                if (promedioTotal >= rangoEquivalencia.getNotaInicio() && promedioTotal <= rangoEquivalencia.getNotaFin()) {
                    equivalencia = rangoEquivalencia.getEquivalenciaId().getNombre();
                }
            }
            String duracion = "";
            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM");
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy");
            SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm");
            String mes = formatMes.format(evaluacionTribunal.getFechaInicio());
            String anio = formatAnio.format(evaluacionTribunal.getFechaInicio());
            String hora = formatHora.format(evaluacionTribunal.getFechaInicio());
            ResourceBundle bundle = null;
            ConfiguracionArea configuracionArea = configuracionAreaFacadeLocal.buscarPorAreaId(carrera.getAreaId().getId(), "NA");
            int valor = Integer.parseInt(configuracionArea.getValor());
            if (lenguaje.equalsIgnoreCase("es")) {
                bundle = ResourceBundle.getBundle("edu.unl.gestionproyectos.internacionalizacion.mensajes_es");
            } else {
                if (lenguaje.equalsIgnoreCase("en")) {
                    bundle = ResourceBundle.getBundle("edu.unl.gestionproyectos.internacionalizacion.mensajes_en");
                }
            }
            if (carrera.getNivelId().getId() == 4) {
                nivel = bundle.getString("lbl.tercer_nivel_academico");
            } else {
                if (carrera.getNivelId().getId() == 5) {
                    nivel = bundle.getString("lbl.cuarto_nivel_academico");

                }
            }
//            int numeroModulos = Integer.parseInt(configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "ME").getValor());
//            int dur = numeroModulos / 2;
//            duracion = bundle.getString("lbl.con_duracion_acta") + " " + bundle.getString("lbl.de") + " " + dur + " " + bundle.getString("lbl.anios") + ", " + bundle.getString("lbl.equivale_acta") + ""
//                    + " " + numeroModulos + " " + bundle.getString("lbl.modulos");

            if (evaluacionTribunal.getRangoEquivalenciaId().getId() != 4) {
                declaracion = bundle.getString("lbl.aprobado");
            } else {
                declaracion = bundle.getString("lbl.reprobado");
            }
            Map p = new HashMap();
            p.put("encabezado", carrera.getAreaId().getNombre().toUpperCase());
            p.put("titulo", bundle.getString("lbl.acta_publica") + " " + bundle.getString("lbl.de") + " <b>" + carrera.getNombreTitulo().toUpperCase() + "</b>" + ", " + bundle.getString("lbl.otorgado_por") + " " + carrera.getAreaId().getNombre().toUpperCase());
            p.put("corresponde", bundle.getString("lbl.corresponde_senior"));
            p.put("autor", autor);
            p.put("detalle", bundle.getString("lbl.en_la_ciudad") + " " + carrera.getLugar() + ", " + bundle.getString("lbl.a_los") + " " + evaluacionTribunal.getFechaInicio().getDate() + " " + bundle.getString("lbl.dias") + " " + bundle.getString("lbl.del_mes") + " " + mes + " "
                    + "" + bundle.getString("lbl.del_anio") + " " + anio + " " + bundle.getString("lbl.a_las") + " " + hora + ", " + bundle.getString("lbl.ante_tribunal") + ", " + bundle.getString("lbl.presidido_por") + ": " + presidente + ", " + bundle.getString("lbl.integrado_miembros") + ": " + miembros
                    + " " + bundle.getString("lbl.actuacion_secretario") + " " + carrera.getAreaId().getSecretario() + ", " + bundle.getString("lbl.secreario_abogado") + ", " + bundle.getString("lbl.detalle_acta_grado") + " " + nivel + " " + bundle.getString("lbl.en_modalidad") + " " + carrera.getModalidad() + " " + bundle.getString("lbl.de_la_carrera")
                    + " " + carrera.getNombre() + ", " + bundle.getString("lbl.lugar_estudio_acta") + ", " + duracion + "; " + bundle.getString("lbl.cumplido_acta") + ": <b>" + autor.toUpperCase() + "</b>" + " " + bundle.getString("lbl.de_genero") + " " + generoAutor + " " + bundle.getString("lbl.de_nacionalidad") + " " + nacionalidadAutor + ", " + bundle.getString("lbl.portador_ci") + " "
                    + "" + ciAutor + " " + bundle.getString("lbl.inicio_estudios_acta") + " " + fechaInicioEstudio + " " + bundle.getString("lbl.fin_estudios_acta") + " " + fechaFinEstudio + ", " + bundle.getString("lbl.objetivo_acta") + ": <b>" + evaluacionTribunal.getTribunalId().getProyectoId().getTemaActual().toUpperCase() + "</b>; " + bundle.getString("lbl.previo_acta") + " " + carrera.getNombreTitulo() + ". " + bundle.getString("lbl.examinado_acta") + ": "
                    + "<b>" + notasPublica + "</b> " + bundle.getString("lbl.siendo_promedio_acta") + ": " + evaluacionTribunal.getNota() + ", " + bundle.getString("lbl.declara_acta") + " " + declaracion + " " + bundle.getString("lbl.tiene_computo_acta") + ": " + bundle.getString("lbl.modulos") + ": <b>" + promedioEstudio + "</b>, " + bundle.getString("lbl.calificacion_privada_acta") + ": <b>" + promedioPrivada + "</b>, "
                    + bundle.getString("lbl.calificacion_publica_acta") + ": <b>" + evaluacionTribunal.getNota() + "</b>, " + bundle.getString("lbl.promedio_total_acta") + " <b>" + promedioTotal + "</b> " + bundle.getString("lbl.equivale_acta") + ": <b>" + equivalencia + "</b>. " + bundle.getString("lbl.promesa_acta") + " <b>"
                    + bundle.getString("lbl.grado_acta").toUpperCase() + " " + carrera.getNombreTitulo().toUpperCase() + "</b>, " + bundle.getString("lbl.habilita_acta") + " <b>" + bundle.getString("lbl.titulo_academico_acta").toUpperCase() + " " + carrera.getNombreTitulo().toUpperCase() + "</b>. " + bundle.getString("lbl.suscriben"));

            p.put("miembros", miembrosCargo);
            p.put("presidente", presidenteCargo);
            p.put("nroActa", bundle.getString("lbl.acta_publica") + " # " + valor);
            p.put("logoUnl", path + "/resources/img/selloInstitucion.png");
            p.put("secretario", secretario + "<br/>" + bundle.getString("lbl.secreario_abogado").toUpperCase());
            p.put("autor_cargo", autorCargo);
            p.put("usuario", bundle.getString("lbl.elaborado_por") + ": " + usuario.getNombres() + " " + usuario.getApellidos());

            File fileReport = new File(path + "/reportes/finalizacion/acta_publica.jasper");
            byte[] bytes = null;
            if (tipo.equalsIgnoreCase("pdf")) {
                bytes = JasperRunManager.runReportToPdf(fileReport.getPath(), p, new JREmptyDataSource());
                response.setCharacterEncoding("ISO-8859-1");
                response.setContentType("application/pdf");
                response.setContentLength(bytes.length);
                ServletOutputStream outStream = response.getOutputStream();
                outStream.write(bytes, 0, bytes.length);
                outStream.flush();
                outStream.close();
            } else {
                if (tipo.equalsIgnoreCase("docx")) {
                    response.setCharacterEncoding("ISO-8859-1");
                    response.addHeader("Content-disposition", "attachment; filename=ActaGrado.docx");
                    JasperPrint jasperPrint = JasperFillManager.fillReport(fileReport.getPath(), p, new JREmptyDataSource());
                    ServletOutputStream servletOutputStream = response.getOutputStream();
                    JRDocxExporter docxExporter = new JRDocxExporter();
                    docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                    docxExporter.exportReport();
                }
            }
            CatalogoActa categoriaActa = categoriaActa = categoriaActaFacadeLocal.buscarPorCodigo(CatalogoActaEnum.ACTAGRADO.getTipo());

            Acta acta = actaFacadeLocal.buscarPorEvaluacionCategoria(evaluacionTribunal.getId(), categoriaActa.getId());
            if (acta == null) {
                acta = new Acta();
                acta.setEvaluacionTribunalId(evaluacionTribunal);
                acta.setDocumento(bytes);
                acta.setCatalogoActaId(categoriaActa);
                acta.setNumero(valor + "");
                actaFacadeLocal.create(acta);
                valor = valor + 1;
                configuracionArea.setValor(valor + "");
                configuracionAreaFacadeLocal.edit(configuracionArea);
            } else {
                acta.setDocumento(bytes);
                actaFacadeLocal.edit(acta);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void grabarOficio(OficioCarrera oficioCarrera, OficioCarreraFacadeLocal oficioCarreraFacadeLocal, CatalogoOficioFacadeLocal catalogoOficioFacadeLocal,
            String catalogo, Integer carreraId, Date fechaActual, String nroOficio, byte[] bytes, Long tablaId) {
        try {
            if (oficioCarrera.getId() == null) {
                CatalogoOficio catalogoOficio = catalogoOficioFacadeLocal.buscarPorCodigo(catalogo);
                oficioCarrera.setCarreraId(carreraId);
                if (catalogoOficio != null) {
                    oficioCarrera.setCatalogoOficioId(catalogoOficio);
                }
                oficioCarrera.setFecha(fechaActual);
                oficioCarrera.setNumeroOficio(nroOficio + "");
                oficioCarrera.setOficio(bytes);
                oficioCarrera.setTablaOficioId(tablaId);
                oficioCarreraFacadeLocal.create(oficioCarrera);
            }
        } catch (Exception e) {
        }
    }

    private void generarPdf() {
        try {

        } catch (Exception e) {
        }
    }

    private void generarDoc(JasperPrint jasperPrint) {
        try {

        } catch (Exception e) {
        }
    }

    private void actualizarNroOficio(ConfiguracionCarrera configuracionCarrera, ConfiguracionCarreraDao configuracionCarreraFacadeLocal) {
        try {
            int valor = Integer.parseInt(configuracionCarrera.getValor()) + 1;
            configuracionCarrera.setValor(valor + "");
            configuracionCarreraFacadeLocal.edit(configuracionCarrera);
        } catch (Exception e) {
        }
    }

}
