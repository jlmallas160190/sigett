/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.servlet;

import edu.unl.sigett.academico.controlador.AdministrarEstudiantesCarrera;
import edu.unl.sigett.finalizacion.controlador.AdministrarActas;
import edu.unl.sigett.reportes.AdministrarReportes;
import edu.unl.sigett.entity.Actividad;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.jlmallas.academico.entity.Carrera;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.jlmallas.academico.entity.CoordinadorPeriodo;
import edu.unl.sigett.entity.DirectorProyecto;
import edu.unl.sigett.entity.DocenteProyecto;
import edu.unl.sigett.entity.DocumentoActividad;
import edu.unl.sigett.entity.DocumentoExpediente;
import edu.unl.sigett.entity.DocumentoProyecto;
import edu.unl.sigett.entity.EvaluacionTribunal;
import com.jlmallas.comun.entity.Foto;
import com.jlmallas.comun.entity.Persona;
import edu.unl.sigett.entity.Miembro;
import edu.unl.sigett.entity.OficioCarrera;
import edu.unl.sigett.entity.Pertinencia;
import edu.unl.sigett.entity.Prorroga;
import edu.jlmallas.academico.entity.ReporteMatricula;
import org.jlmallas.seguridad.entity.Usuario;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.unl.sigett.dao.ActaFacadeLocal;
import edu.unl.sigett.dao.ActividadFacadeLocal;
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.jlmallas.academico.service.CarreraService;
import edu.unl.sigett.dao.CatalogoOficioFacadeLocal;
import edu.unl.sigett.dao.CategoriaActaFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionAreaFacadeLocal;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.jlmallas.academico.dao.CoordinadorPeriodoDao;
import edu.unl.sigett.dao.DirectorProyectoFacadeLocal;
import edu.unl.sigett.dao.DocenteProyectoFacadeLocal;
import edu.unl.sigett.dao.DocumentoActividadFacadeLocal;
import edu.unl.sigett.dao.DocumentoExpedienteFacadeLocal;
import edu.unl.sigett.dao.DocumentoProyectoFacadeLocal;
import edu.unl.sigett.dao.EvaluacionTribunalFacadeLocal;
import com.jlmallas.comun.dao.FotoFacadeLocal;
import com.jlmallas.comun.dao.ItemDao;
import com.jlmallas.comun.dao.PersonaDao;
import edu.unl.sigett.dao.MiembroFacadeLocal;
import edu.unl.sigett.dao.OficioCarreraFacadeLocal;
import edu.unl.sigett.dao.PertinenciaFacadeLocal;
import edu.unl.sigett.dao.ProrrogaFacadeLocal;
import edu.jlmallas.academico.dao.ReporteMatriculaDao;
import org.jlmallas.seguridad.dao.UsuarioDao;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.dao.DocenteCarreraDao;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.enumeration.CargoMiembroEnum;
import edu.unl.sigett.enumeration.CatalogoOficioEnum;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.seguridad.managed.session.SessionUsuarioCarrera;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author JorgeLuis
 */
@WebServlet(name = "AppServlet", urlPatterns = {"/AppServlet/*"})
public class AppServlet extends HttpServlet {

    @Inject
    private AdministrarActas administrarActas;
    @Inject
    private AdministrarEstudiantesCarrera administrarEstudiantesCarrera;
    @Inject
    private SessionUsuarioCarrera sessionUsuarioCarrera;
    @EJB
    private FotoFacadeLocal fotoFacadeLocal;
    @EJB
    private DocumentoProyectoFacadeLocal documentoProyectoFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private UsuarioDao usuarioFacadeLocal;
    @EJB
    private ActaFacadeLocal actaFacadeLocal;
    @EJB
    private CategoriaActaFacadeLocal categoriaActaFacadeLocal;
    @EJB
    private DocumentoActividadFacadeLocal documentoActividadFacadeLocal;
    @EJB
    private DocumentoExpedienteFacadeLocal documentoExpedienteFacadeLocal;
    @EJB
    private AutorProyectoDao autorProyectoFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private OficioCarreraFacadeLocal oficioCarreraFacadeLocal;
    @EJB
    private CatalogoOficioFacadeLocal catalogoOficioFacadeLocal;
    @EJB
    private DirectorProyectoFacadeLocal directorProyectoFacadeLocal;
    @EJB
    private DocenteProyectoFacadeLocal docenteProyectoFacadeLocal;
    @EJB
    private MiembroFacadeLocal miembroFacadeLocal;
    @EJB
    private ProrrogaFacadeLocal prorrogaFacadeLocal;
    @EJB
    private ActividadFacadeLocal actividadFacadeLocal;
    @EJB
    private PertinenciaFacadeLocal pertinenciaFacadeLocal;
    @EJB
    private EvaluacionTribunalFacadeLocal evaluacionTribunalFacadeLocal;
    @EJB
    private ReporteMatriculaDao reporteMatriculaFacadeLocal;
    @EJB
    private ConfiguracionAreaFacadeLocal configuracionAreaFacadeLocal;
    @EJB
    private CoordinadorPeriodoDao coordinadorPeriodoFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private DocenteDao docenteFacadeLocal;
    @EJB
    private DocenteCarreraDao docenteCarreraFacadeLocal;
    @EJB
    private ItemDao itemFacadeLocal;

    private AdministrarReportes reportes;
    private ConfiguracionCarrera configuracionCarrera;

    private Docente docente;
    private CoordinadorPeriodo coordinadorPeriodo;
    private Prorroga prorroga;
    private DirectorProyecto directorProyecto;
    private Miembro miembro;
    private DocumentoProyecto documentoProyecto;
    private Usuario usuario;
    private OficioCarrera oficioCarrera;
    private Foto foto;
    private Docente docenteCoordinador;
    private Persona datosCoordinador;
    private Persona datosDirector;
    private Persona datosDocente;
    private Persona datosAutor;

    private Long prorrogaId;
    private String presidente;
    private String datosMiembros;
    private Long miembroId;
    private String datosUsuario;
    private String path;
    private String pathSetting;
    private Integer nOficio;
    private String resolucion;
    private String fechaOficioFormat;
    private Calendar fechaActual;
    private String fechaFormateada;
    private Carrera carrera;

    private Map datosReporte;
    private ResourceBundle resourceBundle;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String entityId = request.getParameter("id");
            String lenguaje = request.getParameter("lenguaje");
            String entity = request.getParameter("entity");
            String carreraId = request.getParameter("carreraId");
            String usuarioId = request.getParameter("usuarioId");
            path = request.getRealPath("/");
            pathSetting = request.getRealPath("/settings.txt");
            if (entityId == null && entity == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                return;
            }
            inicio();
            getResourceBundle(lenguaje);
            if (entityId.equalsIgnoreCase("")) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                return;
            }
            switch (entity) {
                case "Foto":
                    foto = fotoFacadeLocal.find(Long.parseLong(entityId));
                    if (foto == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    } else {
                        response.reset();
                        response.setContentType("/image/png");
                        response.getOutputStream().write(foto.getFoto());
                        response.getOutputStream().close();
                    }
                    break;
                case "Persona":
                    foto = fotoFacadeLocal.fotoActual(Long.parseLong(entityId));
                    response.reset();
                    response.setContentType("/image/png");
                    response.getOutputStream().write(foto.getFoto());
                    response.getOutputStream().close();
                    break;
                case "DocumentoProyecto":
                    documentoProyecto = documentoProyectoFacadeLocal.find(Long.parseLong(entityId));
                    if (documentoProyecto == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    }
                    response.reset();
                    response.setContentType("/application/pdf");
                    response.getOutputStream().write(documentoProyecto.getDocumento());
                    response.getOutputStream().close();

                    break;
                case "Carrera":
                    if (sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    }
                    if (sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getLogo() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    }
                    response.reset();
                    response.setContentType("/image/png");
                    response.getOutputStream().write(sessionUsuarioCarrera.getUsuarioCarreraDTO().getCarrera().getLogo());
                    response.getOutputStream().close();
                    break;
                case "DocumentoActividad":
                    DocumentoActividad documentoActividad = documentoActividadFacadeLocal.find(Long.parseLong(entityId));
                    if (documentoActividad == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    } else {
                        response.reset();
                        response.setContentType("/application/pdf");
                        response.getOutputStream().write(documentoActividad.getDocumento());
                        response.getOutputStream().close();
                    }
                    break;
                case "Actividad":
                    documentoActividad = documentoActividadFacadeLocal.documentoActual(Long.parseLong(entityId));
                    if (documentoActividad == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    } else {
                        response.reset();
                        response.setContentType("/application/pdf");
                        response.getOutputStream().write(documentoActividad.getDocumento());
                        response.getOutputStream().close();
                    }
                    break;
                case "DocumentoExpediente":
                    DocumentoExpediente documentoExpediente = documentoExpedienteFacadeLocal.find(Long.parseLong(entityId));
                    if (documentoExpediente == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    } else {
                        response.reset();
                        response.setContentType("/application/pdf");
                        response.getOutputStream().write(documentoExpediente.getDocumento());
                        response.getOutputStream().close();
                    }
                    break;

                case "oficioDocenteProyecto":
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
//                    configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(Integer.parseInt(carreraId), "NO");
                    nOficio = Integer.parseInt(configuracionCarrera.getValor());
                    String plazo = configuracionGeneralFacadeLocal.find(3).getValor() + " días laborables";
                    Long docenteProyectoId = Long.parseLong(entityId);
                    DocenteProyecto docenteProyecto = docenteProyectoFacadeLocal.find(docenteProyectoId);
                    docente = docenteFacadeLocal.find(docenteProyecto.getDocenteId());
                    datosDocente = personaFacadeLocal.find(docente.getId());
//                    coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
                    datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
                    docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
                    usuario = usuarioFacadeLocal.find(Long.parseLong(usuarioId));
                    datosUsuario = usuario.getNombres().toUpperCase() + " " + usuario.getApellidos().toUpperCase();
                    oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(Long.parseLong(entityId), CatalogoOficioEnum.DOCENTEPROYECTO.getTipo());
                    /*Envio de datos ha reporte*/

                    datosReporte.put("docente", datosDocente.getNombres().toUpperCase() + " " + datosDocente.getApellidos().toUpperCase());
                    datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
                    datosReporte.put("temaProyecto", docenteProyecto.getProyectoId().getTemaActual());
                    datosReporte.put("tituloDocente", docente.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
                    datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
                    datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
                    datosReporte.put("articulos_pertinencia_tt", resourceBundle.getString("lbl.articulos_pertinencia_tt"));
                    datosReporte.put("asunto_pertinencia_tt", resourceBundle.getString("lbl.asunto_pertinencia_tt"));
                    datosReporte.put("nota_pertinencia_tt", resourceBundle.getString("lbl.nota_pertinencia_tt"));
                    datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(docenteProyecto.getProyectoId().getId())));
                    reportes.oficioDocenteProyecto(response, datosReporte, oficioCarrera, request.getRealPath("/"), pathSetting, "pdf", nOficio, carrera, fechaFormateada,
                            plazo, datosUsuario, configuracionCarreraFacadeLocal, configuracionCarrera, docenteProyectoId,
                            oficioCarreraFacadeLocal, catalogoOficioFacadeLocal);
                    break;
                case "oficioDirectorProyecto":
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
//                    configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(Integer.parseInt(carreraId), "NO");
                    nOficio = Integer.parseInt(configuracionCarrera.getValor());
                    Long directorProyectoId = Long.parseLong(entityId);
                    directorProyecto = directorProyectoFacadeLocal.find(directorProyectoId);
                    docente = docenteCarreraFacadeLocal.find(directorProyecto.getDirectorId().getId()).getDocenteId();
                    datosDirector = personaFacadeLocal.find(docente.getId());
//                    coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
                    usuario = usuarioFacadeLocal.find(Long.parseLong(usuarioId));
                    datosUsuario = usuario.getNombres().toUpperCase() + " " + usuario.getApellidos().toUpperCase();
                    oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(Long.parseLong(entityId), CatalogoOficioEnum.DIRECTORPROYECTO.getTipo());
                    datosReporte.put("docente", datosDocente.getNombres().toUpperCase() + " " + datosDocente.getApellidos().toUpperCase());
                    datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
                    datosReporte.put("temaProyecto", directorProyecto.getProyectoId().getTemaActual());
                    datosReporte.put("tituloDocente", docente.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
                    datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
                    datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(directorProyecto.getProyectoId().getId())));
                    datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
                    datosReporte.put("articulos_adjudicacion_tt", resourceBundle.getString("lbl.articulos_pertinencia_tt"));
                    datosReporte.put("asunto_adjudicacion_tt", resourceBundle.getString("lbl.asunto_adjudciacion_tt"));
                    datosReporte.put("nota_adjudicacion_tt", resourceBundle.getString("lbl.nota_adjudicacion_tt"));
                    reportes.oficioDirectorProyecto(response, datosReporte, fechaFormateada, configuracionCarreraFacadeLocal, configuracionCarrera,
                            "pdf", oficioCarrera, directorProyectoId, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera, nOficio,
                            datosUsuario, request.getRealPath("/"), pathSetting);
                    break;
                case "oficioMiembroTribunalSprivada":
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
//                    configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(Integer.parseInt(carreraId), "NO");
                    nOficio = Integer.parseInt(configuracionCarrera.getValor());
                    miembroId = Long.parseLong(entityId);
                    miembro = miembroFacadeLocal.find(miembroId);
                    Docente docenteMiembro = docenteFacadeLocal.find(miembro.getDocenteId());
                    Persona datosMiembro = personaFacadeLocal.find(docenteMiembro.getId());
                    getMiembros(miembroFacadeLocal.buscarPorTribunal(miembro.getTribunalId().getId()));
//                    coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
                    datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
                    docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
                    usuario = usuarioFacadeLocal.find(Long.parseLong(usuarioId));
                    datosUsuario = usuario.getNombres().toUpperCase() + " " + usuario.getApellidos().toUpperCase();
                    oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(Long.parseLong(entityId), CatalogoOficioEnum.MIEMBROTRIBUNALPRIVADA.getTipo());
                    datosReporte.put("miembro", datosMiembro.getNombres().toUpperCase() + " " + datosMiembro.getApellidos().toUpperCase());
                    datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
                    datosReporte.put("temaProyecto", miembro.getTribunalId().getProyectoId().getTemaActual());
                    datosReporte.put("tituloMiembro", docente.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
                    datosReporte.put("cargoMiembro", miembro.getCargoId().getNombre().toUpperCase());
                    datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
                    datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(miembro.getTribunalId().getProyectoId().getId())));
                    datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
                    datosReporte.put("articulos_designar_miembro_tribunal", resourceBundle.getString("lbl.articulos_designar_miembro_tribunal"));
                    datosReporte.put("asunto_designar_miembro_tribunal", resourceBundle.getString("lbl.asunto_designar_miembro_tribunal"));
                    datosReporte.put("nota_designar_miembro_tribunal", resourceBundle.getString("lbl.designar_miembro_tribunal"));

                    reportes.oficioMiembroTribunalSprivada("pdf", fechaFormateada, response, datosReporte, configuracionCarreraFacadeLocal, configuracionCarrera,
                            oficioCarrera, presidente, datosMiembros, Long.parseLong(entityId), oficioCarreraFacadeLocal, catalogoOficioFacadeLocal,
                            carrera, nOficio, datosUsuario, path, pathSetting);
                    break;
                case "oficioMiembroTribunalSpublica":
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
//                    configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(Integer.parseInt(carreraId), "NO");
                    nOficio = Integer.parseInt(configuracionCarrera.getValor());
                    miembroId = Long.parseLong(entityId);
                    miembro = miembroFacadeLocal.find(miembroId);
                    Docente docenteMiembroPublica = docenteFacadeLocal.find(miembro.getDocenteId());
                    Persona datosMiembroPublica = personaFacadeLocal.find(docenteMiembroPublica.getId());
                    getMiembros(miembroFacadeLocal.buscarPorTribunal(miembro.getTribunalId().getId()));
                    String lugarSustentacion = "";
                    String fechaSustentacion = "";
                    for (EvaluacionTribunal evaluacionTribunal : miembro.getTribunalId().getEvaluacionTribunalList()) {
                        if (evaluacionTribunal.getCatalogoEvaluacionId().getId() == 2) {
                            lugarSustentacion = evaluacionTribunal.getLugar();
                            int tiempo = evaluacionTribunal.getFechaFin().getHours() - evaluacionTribunal.getFechaInicio().getHours();
                            fechaSustentacion = configuracionGeneralFacadeLocal.dateFormat(evaluacionTribunal.getFechaInicio()) + " a partir de " + configuracionGeneralFacadeLocal.timeFormat(evaluacionTribunal.getFechaInicio()) + ""
                                    + " a efecto para que se cumpla por un espacio  máximo de " + tiempo + " hora(s) con la sustentación pública.";
                        }
                    }
//                    coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
                    datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
                    docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
                    usuario = usuarioFacadeLocal.find(Long.parseLong(usuarioId));
                    datosUsuario = usuario.getNombres().toUpperCase() + " " + usuario.getApellidos().toUpperCase();

                    datosReporte.put("miembro", datosMiembroPublica.getNombres().toUpperCase() + " " + datosMiembroPublica.getApellidos().toUpperCase());
                    datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
                    datosReporte.put("temaProyecto", miembro.getTribunalId().getProyectoId().getTemaActual());
                    datosReporte.put("cargoMiembro", miembro.getCargoId().getNombre().toUpperCase());
                    datosReporte.put("tituloMiembro", docenteMiembroPublica.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
                    datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
                    datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(miembro.getTribunalId().getProyectoId().getId())));
                    datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
                    datosReporte.put("articulos_sustentacion_publica", resourceBundle.getString("lbl.articulos_sustentacion_publica"));
                    oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(Long.parseLong(entityId), CatalogoOficioEnum.MIEMBROTRIBUNALPUBLICA.getTipo());
                    reportes.oficioMiembroTribunalSpublica("pdf", fechaFormateada, fechaSustentacion, lugarSustentacion, response, datosReporte,
                            configuracionCarreraFacadeLocal, configuracionCarrera, oficioCarrera,
                            presidente, datosMiembros, Long.parseLong(entityId), oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera,
                            nOficio, datosUsuario, path, pathSetting);
                    break;
                case "oficioDirectorProrroga":
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
//                    configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(Integer.parseInt(carreraId), "NO");
                    nOficio = Integer.parseInt(configuracionCarrera.getValor());
                    prorrogaId = Long.parseLong(entityId);
                    prorroga = prorrogaFacadeLocal.find(prorrogaId);

                    for (DirectorProyecto director : directorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())) {
                        if (director.getEstadoDirectorId().getId() != 2) {
                            this.directorProyecto = director;
                            break;
                        }
                    }
                    Docente docenteDirectorProyecto = docenteCarreraFacadeLocal.find(this.directorProyecto.getDirectorId().getId()).getDocenteId();
                    Persona datosDocenteDirector = personaFacadeLocal.find(docenteDirectorProyecto.getId());
//                    coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
                    datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
                    docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
                    usuario = usuarioFacadeLocal.find(Long.parseLong(usuarioId));
                    datosUsuario = usuario.getNombres() + " " + usuario.getApellidos();
                    oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(Long.parseLong(entityId), CatalogoOficioEnum.PRORROGA.getTipo());
                    datosReporte.put("tituloDocente", docenteDirectorProyecto.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
                    datosReporte.put("docente", datosDocenteDirector.getNombres().toUpperCase() + " " + datosDocenteDirector.getApellidos().toUpperCase());
                    datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
                    datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
                    datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())));
                    datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
                    datosReporte.put("asunto_prorroga_tt", resourceBundle.getString("lbl.asunto_prorroga_tt"));
                    datosReporte.put("nota_prorroga_tt", resourceBundle.getString("nota_prorroga_tt"));

                    reportes.oficioDirectorProrroga("pdf", fechaFormateada, response, datosReporte, configuracionCarreraFacadeLocal, configuracionCarrera,
                            oficioCarrera, Long.parseLong(entityId), oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera, nOficio, datosUsuario,
                            path, pathSetting);
                    break;
                case "informeDirectorProrroga":
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
//                    configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(Integer.parseInt(carreraId), "NO");
                    prorrogaId = Long.parseLong(entityId);
                    prorroga = prorrogaFacadeLocal.find(prorrogaId);
                    String objetivos = "";
                    for (Actividad objetivo : actividadFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getId())) {
                        if (objetivo.getTipoActividadId().getId() == 1) {
                            double valor = (objetivo.getAvance() * objetivo.getPorcentajeDuracion()) / 100;
                            valor = Math.round(valor * 100);
                            valor = valor / 100;
                            objetivos += "" + valor + "% " + objetivo.getNombre() + "\n";
                        }
                    }
                    for (DirectorProyecto director : directorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())) {
                        if (director.getEstadoDirectorId().getId() != 2) {
                            this.directorProyecto = director;
                            break;
                        }
                    }
                    Docente docenteDirectorInforme = docenteCarreraFacadeLocal.find(this.directorProyecto.getDirectorId().getId()).getDocenteId();
                    Persona datosDocenteDirectorInforme = personaFacadeLocal.find(docenteDirectorInforme.getId());
//                    coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
                    datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
                    docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
                    usuario = usuarioFacadeLocal.find(Long.parseLong(usuarioId));
                    datosUsuario = usuario.getNombres().toUpperCase() + " " + usuario.getApellidos().toUpperCase();
                    oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(Long.parseLong(entityId), CatalogoOficioEnum.INFORMEDIRECTORPRORROGA.getTipo());
                    fechaOficioFormat = configuracionGeneralFacadeLocal.dateFormat(oficioCarrera.getFecha());
                    datosReporte.put("tituloDocente", docenteDirectorInforme.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
                    datosReporte.put("docente", datosDocenteDirectorInforme.getNombres().toUpperCase() + " " + datosDocenteDirectorInforme.getApellidos().toUpperCase());
                    datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
                    datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
                    datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())));

//                    reportes.informeDirectorProrroga("pdf", fechaOficioFormat + "", objetivos, prorroga.getCronogramaId().getAvance() + "%", prorroga.getObservacion(),
//                            fechaFormateada, response, datosReporte, configuracionCarreraFacadeLocal, configuracionCarrera, oficioCarrera, prorrogaId, oficioCarreraFacadeLocal,
//                            catalogoOficioFacadeLocal, carrera, Integer.parseInt(oficioCarrera.getNumeroOficio()), datosUsuario, prorrogaId, path, pathSetting);
                    break;
                case "informeDocenteProyecto":
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
//                    configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(Integer.parseInt(carreraId), "NO");
                    Long pertinenciaId = Long.parseLong(entityId);
                    Pertinencia pertinencia = pertinenciaFacadeLocal.find(pertinenciaId);
                    oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(Long.parseLong(entityId), CatalogoOficioEnum.PERTINENCIA.getTipo());
                    OficioCarrera oficioResponse = oficioCarreraFacadeLocal.buscarPorTablaId(pertinencia.getDocenteProyectoId().getId(), CatalogoOficioEnum.DOCENTEPROYECTO.getTipo());
                    if (oficioResponse != null) {
                        fechaOficioFormat = configuracionGeneralFacadeLocal.dateFormat(oficioResponse.getFecha());
                    }
                    docente = docenteFacadeLocal.find(pertinencia.getDocenteProyectoId().getDocenteId());
                    datosDocente = personaFacadeLocal.find(docente.getId());
//                    coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
                    datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
                    docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
                    if (pertinencia.getEsAceptado()) {
                        resolucion = "<html><body>Por consiguiente <b>SE OTORGA LA PERTINENCIA</b> para el desarrollo de este proyecto.";
                    } else {
                        resolucion = "<html><body>Por consiguiente <b> NO SE OTORGA LA PERTINENCIA</b> para el desarrollo de este proyecto.</body></html>";
                    }
                    datosReporte.put("docente", datosDocente.getNombres().toUpperCase() + " " + datosDocente.getApellidos().toUpperCase());
                    datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
                    datosReporte.put("temaProyecto", pertinencia.getDocenteProyectoId().getProyectoId().getTemaActual());
                    datosReporte.put("tituloDocente", docente.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase());
                    datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
                    datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(pertinencia.getDocenteProyectoId().getProyectoId().getId())));

                    reportes.informeDocenteProyecto("pdf", fechaFormateada, fechaOficioFormat, pertinencia, response, datosReporte, configuracionCarreraFacadeLocal,
                            configuracionCarrera, oficioCarrera, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera, Integer.parseInt(oficioResponse.getNumeroOficio()), request.getRealPath("/"), resolucion);
                    break;
                case "responseProrrogaAutorProyecto":
                    prorrogaId = Long.parseLong(entityId);
                    prorroga = prorrogaFacadeLocal.find(prorrogaId);
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
//                    configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(Integer.parseInt(carreraId), "NO");
                    String fechaEmision = "";
                    if (prorroga.getEsAceptado()) {
                        int tiempo = prorroga.getFecha().getMonth() - prorroga.getFechaInicial().getMonth();
                        resolucion = "<b>acepta la prorroga de " + tiempo + " meses</b>";
                        fechaEmision = configuracionGeneralFacadeLocal.dateFormat(prorroga.getFecha());
                        fechaEmision = "a partir del " + fechaEmision + ", fecha en que el director emite su informe";
                    } else {
                        resolucion = "<b>no acepta la prorroga<b>";
                    }

//                    coordinadorPeriodo = coordinadorPeriodoFacadeLocal.buscarVigente(carrera.getId());
                    datosCoordinador = personaFacadeLocal.find(coordinadorPeriodo.getCoordinadorId().getId());
                    docenteCoordinador = docenteFacadeLocal.find(datosCoordinador.getId());
                    usuario = usuarioFacadeLocal.find(Long.parseLong(usuarioId));
                    datosUsuario = usuario.getNombres().toUpperCase() + " " + usuario.getApellidos().toUpperCase();
                    oficioCarrera = oficioCarreraFacadeLocal.buscarPorTablaId(Long.parseLong(entityId), CatalogoOficioEnum.RESPUESTAPRORROGAAUTOR.getTipo());

                    datosReporte.put("coordinador", datosCoordinador.getNombres().toUpperCase() + " " + datosCoordinador.getApellidos().toUpperCase());
                    datosReporte.put("tituloCoordinador", docenteCoordinador.getTituloDocenteId().getTituloId().getAbreviacion());
                    datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(prorroga.getCronogramaId().getProyecto().getId())));
                    datosReporte.put("abreviacion_oficio", resourceBundle.getString("lbl.abreviacion_oficio"));
                    reportes.responseProrrogaAutorProyecto("pdf", fechaFormateada, response, datosReporte, configuracionCarreraFacadeLocal, configuracionCarrera,
                            oficioCarrera, prorrogaId, oficioCarreraFacadeLocal, catalogoOficioFacadeLocal, carrera,
                            nOficio, datosUsuario, fechaEmision, resolucion, path, pathSetting);
                    break;
                case "certificacion":
                    reportes = new AdministrarReportes();
                    path = request.getRealPath("/");
                    pathSetting = request.getRealPath("/settings.txt");
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
                    Long id = Long.parseLong(entityId);
                    this.directorProyecto = directorProyectoFacadeLocal.find(id);
                    docente = docenteCarreraFacadeLocal.find(directorProyecto.getId()).getDocenteId();
                    datosDirector = personaFacadeLocal.find(docente.getId());
                    fechaActual = Calendar.getInstance();
                    fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());

                    datosReporte.put("tituloDirector", datosDirector.getNombres().toUpperCase() + " " + datosDirector.getApellidos().toUpperCase());
                    datosReporte.put("razon", resourceBundle.getString("lbl.razon_certificacion_director"));
                    datosReporte.put("autores", getAutores(autorProyectoFacadeLocal.buscarPorProyecto(this.directorProyecto.getProyectoId().getId())));
                    reportes.certificacionDirector("pdf", fechaFormateada, response, datosReporte, carrera, path, pathSetting);
                    break;
                case "actaPrivada":
                    EvaluacionTribunal evaluacionTribunal = evaluacionTribunalFacadeLocal.find(Long.parseLong(entityId));
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
                    String tipoActa = request.getParameter("tipoActa");
                    administrarActas.generarActaPrivada(Integer.parseInt(tipoActa), "pdf", response, request, lenguaje, evaluacionTribunal, carrera);
                    break;
                case "actaGrado":
                    String autorProyectoId = request.getParameter("autorId");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
                    evaluacionTribunal = evaluacionTribunalFacadeLocal.find(Long.parseLong(entityId));
                    AutorProyecto ap = autorProyectoFacadeLocal.find(Long.parseLong(autorProyectoId));
                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(ap.getAspiranteId().getId());
                    String fechaInicioEstudio = "";
                    String fechaFinEstudio = "";
                    String presidenteCargo = "";
                    String notasPublica = "";
                    String autor = "";
                    String autorCargo = "";
                    String miembrosCargo = "<html><body><table>";
                    int contadorMiembros = 0;
                    int contadoNotas = 0;
                    double promedioPrivada = 0;
                    double promedioEstudio = 0;
                    String generoAutor = "";
                    String ciAutor = "";
                    String nacionalidadAutor = "";
                    EstudianteCarrera estudianteAutor = estudianteCarreraFacadeLocal.find(ap.getAspiranteId().getId());
                    Persona datosAutorProy = personaFacadeLocal.find(estudianteAutor.getEstudianteId().getId());
//                    autorCargo = datosAutorProy.getNombres().toUpperCase() + " " + datosAutorProy.getApellidos().toUpperCase() + " <br/>" + estudianteAutor.getEstadoId().getNombre();
                    autor = datosAutorProy.getNombres().toUpperCase() + " " + datosAutorProy.getApellidos().toUpperCase();
                    generoAutor = itemFacadeLocal.find(datosAutorProy.getGeneroId()).getNombre().toUpperCase();
                    nacionalidadAutor = datosAutorProy.getNacionalidadId().getNombre().toUpperCase();
                    ciAutor = datosAutorProy.getNumeroIdentificacion();
//                    fechaInicioEstudio = formatoFecha.format(administrarEstudiantesCarrera.obtenerPrimerMatricula(estudianteAutor).getOfertaAcademicaId().getFechaInicio());
//                    fechaFinEstudio = formatoFecha.format(administrarEstudiantesCarrera.obtenerMatriculaUltima(estudianteAutor).getOfertaAcademicaId().getFechaFin());

                    for (Miembro miembroTribunal : miembroFacadeLocal.buscarPorTribunal(evaluacionTribunal.getTribunalId().getId())) {
                        docente = docenteFacadeLocal.find(miembroTribunal.getDocenteId());
                        Persona personaMiembro = personaFacadeLocal.find(docente.getId());
                        if (miembroTribunal.getCargoId().getCodigo().equalsIgnoreCase(CargoMiembroEnum.PRESIDENTE.getTipo())) {
                            presidente += docente.getTituloDocenteId().getTituloId().getAbreviacion() + " " + personaMiembro.getNombres().toUpperCase() + " " + personaMiembro.getApellidos().toUpperCase();
                            presidenteCargo += docente.getTituloDocenteId().getTituloId().getAbreviacion() + " " + personaMiembro.getNombres().toUpperCase() + " " + personaMiembro.getApellidos().toUpperCase() + "<br/>" + miembroTribunal.getCargoId().getNombre();
                        } else {
                            miembrosCargo += "<tr> <td>" + docente.getTituloDocenteId().getTituloId().getAbreviacion() + " " + personaMiembro.getNombres().toUpperCase() + " " + personaMiembro.getApellidos().toUpperCase() + "</td><td>" + miembroTribunal.getCargoId().getNombre() + "</td></tr>";
                            if (contadorMiembros == 0) {
                                datosMiembros += "" + docente.getTituloDocenteId().getTituloId().getAbreviacion() + " " + personaMiembro.getNombres().toUpperCase() + " " + personaMiembro.getApellidos().toUpperCase();
                                contadorMiembros++;
                            } else {
                                datosMiembros += ", " + docente.getTituloDocenteId().getTituloId().getAbreviacion() + " " + personaMiembro.getNombres().toUpperCase() + " " + personaMiembro.getApellidos().toUpperCase();
                                contadorMiembros++;
                            }
                        }
                    }
                    miembrosCargo = miembrosCargo + "</table></body></html>";
                    for (CalificacionMiembro calificacionMiembro : evaluacionTribunal.getCalificacionMiembroList()) {
                        if (calificacionMiembro.getEsActivo()) {
                            if (contadoNotas == 0) {
                                notasPublica += calificacionMiembro.getNota();
                            } else {
                                notasPublica += ", " + calificacionMiembro.getNota();
                            }
                            contadoNotas++;
                        }
                    }

                    for (EvaluacionTribunal ev : evaluacionTribunal.getTribunalId().getEvaluacionTribunalList()) {
                        if (ev.getCatalogoEvaluacionId().getId() == 1) {
                            promedioPrivada = ev.getNota();
                            break;
                        }
                    }
                    int contadorReportesMatricula = 0;
                    for (ReporteMatricula rm : reporteMatriculaFacadeLocal.buscarPorEstudianteCarrera(estudianteCarrera.getId())) {
                        if (rm.getNota() != 0) {
                            promedioEstudio += rm.getNota();
                            contadorReportesMatricula++;
                        }
                    }
                    usuario = usuarioFacadeLocal.find(Long.parseLong(usuarioId));
                    promedioEstudio = promedioEstudio / contadorReportesMatricula;
                    carrera = carreraFacadeLocal.find(Integer.parseInt(carreraId));
                    reportes.actaGrado("pdf", response, evaluacionTribunal, configuracionAreaFacadeLocal, configuracionCarreraFacadeLocal, actaFacadeLocal, categoriaActaFacadeLocal, carrera, presidente, presidenteCargo, datosMiembros, miembrosCargo, autor, autorCargo, notasPublica, promedioPrivada, promedioEstudio, carrera.getAreaId().getSecretario(), generoAutor, nacionalidadAutor, ciAutor, fechaInicioEstudio, fechaFinEstudio, path, pathSetting, lenguaje, usuario);
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void getResourceBundle(String lenguaje) {
        if (lenguaje == null) {
            return;
        }
        if (lenguaje.equalsIgnoreCase("es")) {
            resourceBundle = ResourceBundle.getBundle("i18n.mensajes.mensajes_es");
            return;
        }
        if (lenguaje.equalsIgnoreCase("en")) {
            resourceBundle = ResourceBundle.getBundle("i18n.mensajes.mensajes_en");
            return;
        }

    }

    private void inicio() {
        datosReporte = new HashMap();
        reportes = new AdministrarReportes();
        configuracionCarrera = new ConfiguracionCarrera();
        fechaOficioFormat = "";
        resolucion = "";
        fechaActual = Calendar.getInstance();
        path = configuracionGeneralFacadeLocal.find(2).getValor();
        pathSetting = configuracionGeneralFacadeLocal.find(1).getValor();
        nOficio = 0;
        fechaFormateada = configuracionGeneralFacadeLocal.dateFormat(fechaActual.getTime());
    }

    private void getMiembros(List<Miembro> miembrosList) {
        try {
            int contador = 0;
//            miembroFacadeLocal.buscarPorTribunal(miembro.getTribunalId().getId())
            for (Miembro m : miembrosList) {
                Docente docenteMiembro = docenteFacadeLocal.find(m.getDocenteId());
                Persona datosMiembro = personaFacadeLocal.find(docente.getId());
                if (m.getCargoId().getId() == 1) {
                    presidente += docenteMiembro.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase() + " " + datosMiembro.getNombres().toUpperCase() + " " + datosMiembro.getApellidos().toUpperCase();
                } else {
                    if (contador == 0) {
                        datosMiembros += "" + docenteMiembro.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase() + " " + datosMiembro.getNombres().toUpperCase() + " " + datosMiembro.getApellidos().toUpperCase();
                        contador++;
                    } else {
                        datosMiembros += ", " + docenteMiembro.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase() + " " + datosMiembro.getNombres().toUpperCase() + " " + datosMiembro.getApellidos().toUpperCase();
                        contador++;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private String getAutores(List<AutorProyecto> autorProyectos) {
        String datosAutores = "";
        int cont = 0;
        try {
            for (AutorProyecto autorProyecto : autorProyectos) {
//                if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
//                    EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
//                    datosAutor = personaFacadeLocal.find(estudianteCarrera.getEstudianteId().getId());
//                    if (cont == 0) {
////                        datosAutores += "" + estudianteCarrera.getEstadoId().getNombre() + " " + datosAutor.getNombres().toUpperCase() + " " + datosAutor.getApellidos().toUpperCase() + "";
//                        cont++;
//                    } else {
////                        datosAutores += ", " + estudianteCarrera.getEstadoId().getNombre() + " " + datosAutor.getNombres().toUpperCase() + " " + datosAutor.getApellidos().toUpperCase();
//                        cont++;
//                    }
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datosAutores;
    }
}
