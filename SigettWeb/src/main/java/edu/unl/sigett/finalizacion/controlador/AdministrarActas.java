/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.finalizacion.controlador;

import org.jlmallas.poi.GeneraDoc;
import org.jlmallas.poi.GeneraPdf;
import com.jlmallas.comun.entity.Persona;
import com.jlmallas.comun.dao.PersonaDao;
import edu.unl.sigett.entity.Acta;
import edu.unl.sigett.entity.AutorProyecto;
import edu.unl.sigett.entity.CalificacionMiembro;
import edu.unl.sigett.entity.CalificacionParametro;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.Docente;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.unl.sigett.entity.ConfiguracionCarrera;
import edu.unl.sigett.entity.EvaluacionTribunal;
import edu.unl.sigett.entity.Miembro;
import edu.unl.sigett.entity.Parametro;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import edu.unl.sigett.entity.SugerenciaCalificacionMiembro;
import edu.unl.sigett.entity.Tribunal;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import edu.unl.sigett.dao.ActaFacadeLocal;
import edu.unl.sigett.dao.AutorProyectoDao;
import edu.jlmallas.academico.service.CarreraService;
import edu.jlmallas.academico.dao.DocenteDao;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.unl.sigett.enumeration.EstadoAutorEnum;
import edu.unl.sigett.dao.ConfiguracionCarreraDao;
import edu.unl.sigett.dao.EvaluacionTribunalFacadeLocal;
import edu.unl.sigett.dao.MiembroFacadeLocal;

/**
 *
 * @author jorge-luis
 */
@Named(value = "administrarActas")
@SessionScoped
public class AdministrarActas implements Serializable {

    @EJB
    private AutorProyectoDao autorProyectoFacadeLocal;
    @EJB
    private MiembroFacadeLocal miembroFacadeLocal;
    @EJB
    private EvaluacionTribunalFacadeLocal evaluacionTribunalFacadeLocal;
    @EJB
    private ActaFacadeLocal actaFacadeLocal;
    @EJB
    private ConfiguracionCarreraDao configuracionCarreraFacadeLocal;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private DocenteDao docenteFacadeLocal;

    private boolean renderedDlgActaPrivada;
    private boolean renderedDlgActaGrado;
    private Long evaluacionId;
    private String lenguaje;
    private int tipoActaEnPrivada = 0;
    private int carreraId;
    private Long autorId;

    public AdministrarActas() {
    }

    public void imprimirActaGrado(EvaluacionTribunal evaluacionTribunal, AutorProyecto autorProyecto) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            this.lenguaje = bundle.getLocale().getLanguage();
            evaluacionId = evaluacionTribunal.getId();
            autorId = autorProyecto.getId();
            for (ProyectoCarreraOferta proyectoCarreraOferta : evaluacionTribunal.getTribunalId().getProyectoId().getProyectoCarreraOfertaList()) {
                carreraId = proyectoCarreraOferta.getCarreraId();
                break;
            }
            renderedDlgActaGrado = true;
            RequestContext.getCurrentInstance().execute("PF('dlgActaGrado').show()");

        } catch (Exception e) {
        }
    }

    public void imprimirActaPrivada(String tipo, EvaluacionTribunal evaluacionTribunal) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
            this.lenguaje = bundle.getLocale().getLanguage();
            evaluacionId = evaluacionTribunal.getId();
            for (ProyectoCarreraOferta proyectoCarreraOferta : evaluacionTribunal.getTribunalId().getProyectoId().getProyectoCarreraOfertaList()) {
                carreraId = proyectoCarreraOferta.getCarreraId();
                break;
            }
            if (tipo.equalsIgnoreCase("preliminar")) {
                renderedDlgActaPrivada = true;
                tipoActaEnPrivada = 1;
                RequestContext.getCurrentInstance().execute("PF('dlgActaPrivada').show()");
            } else {
                if (tipo.equalsIgnoreCase("privada")) {
                    renderedDlgActaPrivada = true;
                    RequestContext.getCurrentInstance().execute("PF('dlgActaPrivada').show()");
                    tipoActaEnPrivada = 2;
                }
            }
        } catch (Exception e) {
        }
    }

    public String[][] construirMatriz(List<String> contenidos, int numeroFilas, int numeroColumnas) {
        String contenidosTabla[][] = new String[numeroFilas][numeroColumnas];
        int aux = 0;
        for (int i = 0; i < numeroFilas; i++) {
            for (int j = 0; j < numeroColumnas; j++) {
                contenidosTabla[i][j] = contenidos.get(aux);
                aux++;
            }
        }
        return contenidosTabla;
    }

    public void grabar(Acta acta, ConfiguracionCarrera configuracionCarrera) {
        try {
            if (acta.getId() == null) {
                actaFacadeLocal.create(acta);
                if (configuracionCarrera != null) {
                    configuracionCarreraFacadeLocal.edit(configuracionCarrera);
                }
            } else {
                actaFacadeLocal.edit(acta);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generarActaPrivada(int tipoActa, String tipo, HttpServletResponse respuesta, HttpServletRequest request,
            String lenguaje, EvaluacionTribunal evaluacionTribunal, Carrera carrera) {
//        try {
//            Acta acta = new Acta();
//            CatalogoActa catalogoActa = new CatalogoActa();
//
//            ResourceBundle bundle = null;
//            String path = "";
//            HttpServletResponse response = null;
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            /*I18n*/
//            if (facesContext == null) {
//                response = respuesta;
//                path = request.getRealPath("");
//                if (lenguaje.equalsIgnoreCase("es")) {
//                    bundle = ResourceBundle.getBundle("edu.unl.gestionproyectos.internacionalizacion.mensajes_es");
//                } else {
//                    if (lenguaje.equalsIgnoreCase("en")) {
//                        bundle = ResourceBundle.getBundle("edu.unl.gestionproyectos.internacionalizacion.mensajes_en");
//                    }
//                }
//            } else {
//                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//                response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
//                path = servletContext.getRealPath("");
//                bundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
//            }
//
//            if (evaluacionTribunal == null) {
//                evaluacionTribunal = evaluacionTribunalFacadeLocal.find(evaluacionId);
//            }
//            /*Formato de Fechas*/
//            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM");
//            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy");
//
//            /*Logo Universidad*/
//            String logoInstitucion = path + File.separator + "resources/img" + File.separator + "selloInstitucion.png";
//            /*Variables*/
//            Tribunal tribunal = null;
//            String tema = "";
//            String autores = "";
//            String parametros = "";
//            String asunto = "";
//            String analisis = "";
//            String nota = "";
//            boolean imprimirCambios = false;
//            String resolucion = "";
//            String egresado = "";
//            int contador = 0;
//            String miembros = "";
//            Proyecto proyecto = null;
//            String titulo = "";
//            String mes = "";
//            String anio = "";
//            Date fechaPlazo = null;
//            int numeroDiasPlazo = 0;
//            String numeroActa = "";
//            List<String> autoresTabla = new ArrayList<>();
//            List<String> autoresCabeceraTabla = new ArrayList<>();
//            List<String> miembrosTabla = new ArrayList<>();
//            List<String> miembrosCabeceraTabla = new ArrayList<>();
//            List<String> cambiosCabeceraTabla = new ArrayList<>();
//            List<String> cambiosTabla = new ArrayList<>();
//
//            /*Número de Acta*/
//            ConfiguracionCarrera configuracionCarrera = new ConfiguracionCarrera();
//            if (carrera == null) {
//                carrera = carreraFacadeLocal.find(carreraId);
//            }
//            if (carrera != null) {
////                configuracionCarrera = configuracionCarreraFacadeLocal.buscarPorCarreraId(carrera.getId(), "NA");
//                if (configuracionCarrera != null) {
//                    numeroActa = configuracionCarrera.getValor();
//                }
//            }
//            /*Calculo de Días Plazo para cambios*/
//            DateResource calculo = new DateResource();
//            if (evaluacionTribunal != null) {
//                fechaPlazo = !evaluacionTribunal.getPlazoEvaluacionTribunalList().isEmpty() ? evaluacionTribunal.getPlazoEvaluacionTribunalList().get(0).getFechaPlazo() : null;
//                if (fechaPlazo != null) {
//                    numeroDiasPlazo = calculo.calculaDuracionEnDias(evaluacionTribunal.getFechaFin(), fechaPlazo, 2);
//                }
//                mes = formatMes.format(evaluacionTribunal.getFechaInicio());
//                anio = formatAnio.format(evaluacionTribunal.getFechaInicio());
//                if (evaluacionTribunal.getTribunalId() != null) {
//                    tribunal = evaluacionTribunal.getTribunalId();
//                    if (tribunal.getProyectoId() != null) {
//                        proyecto = tribunal.getProyectoId();
//                    }
//                }
//                /*Resolución de Miembros de tribunal*/
//                if (tipoActa == 2) {
//                    titulo = bundle.getString("lbl.acta_privada") + " " + numeroActa;
//                    catalogoActa = categoriaActaFacadeLocal.find(2);
//                    imprimirCambios = false;
//                    if (evaluacionTribunal.getEsAptoCalificar()) {
//                        if (evaluacionTribunal.getRangoEquivalenciaId().getEquivalenciaId().getId() == 1) {
//                            resolucion = bundle.getString("lbl.resolucion_privada_no_apta");
//                        } else {
//                            resolucion = bundle.getString("lbl.resolucion_privada_apta");
//                        }
//                    } else {
//                        catalogoActa = categoriaActaFacadeLocal.find(2);
//                        resolucion = bundle.getString("lbl_nota_no_asentada_privada");
//                    }
//                } else {
//                    resolucion = bundle.getString("lbl.resolucion_privada_preliminar");
//                    titulo = bundle.getString("lbl.acta_preliminar") + " " + numeroActa;
//                    catalogoActa = categoriaActaFacadeLocal.find(1);
//                    imprimirCambios = true;
//                }
//            }
//            /*Autores*/
//            if (proyecto != null) {
//                tema = proyecto.getTemaActual();
//                int tamanioAutores = 0;
//                tamanioAutores = autorProyectoFacadeLocal.buscarPorProyecto(proyecto.getId()).size();
//                for (int i = 0; i < tamanioAutores; i++) {
//                    autoresCabeceraTabla.add("");
//                }
//                for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
////                    if (!autorProyecto.getEstadoAutorId().getCodigo().equalsIgnoreCase(EstadoAutorEnum.ABANDONADO.getTipo())) {
////                        EstudianteCarrera ec = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
//////                        Persona datosAutor = personaFacadeLocal.find(ec.getEstadoId().getId());
//////                        autoresTabla.add(datosAutor.getNombres().toUpperCase() + " " + datosAutor.getApellidos().toUpperCase());
//////                        if (contador == 0) {
//////                            autores += "" + datosAutor.getNombres().toUpperCase() + " " + datosAutor.getApellidos().toUpperCase();
//////                            contador++;
//////                        } else {
//////                            autores += ", " + datosAutor.getNombres().toUpperCase() + " " + datosAutor.getApellidos().toUpperCase();
//////                            contador++;
//////                        }
////                    }
//                }
//            }
//            if (contador > 1) {
//                egresado = bundle.getString("lbl.presenta_egresados");
//            } else {
//                egresado = bundle.getString("lbl.presenta_egresado");
//            }
//            /*Miembros*/
//            if (tribunal != null) {
//                int contador1 = 0;
//                int tamanioMiembros = 0;
//                tamanioMiembros = miembroFacadeLocal.buscarPorTribunal(tribunal.getId()).size();
//                for (int i = 0; i < tamanioMiembros; i++) {
//                    miembrosCabeceraTabla.add("");
//                }
//                if (tribunal.getMiembroList() != null) {
//                    for (Miembro miembro : tribunal.getMiembroList()) {
//                        if (miembro.getEsActivo()) {
//                            Docente docenteMiembro = docenteFacadeLocal.find(miembro.getDocenteId());
//                            Persona datosMiembro = personaFacadeLocal.find(docenteMiembro.getId());
//                            miembrosTabla.add(docenteMiembro.getTituloDocenteId().getTituloId().getAbreviacion().toUpperCase() + " " + datosMiembro.getNombres().toUpperCase() + " " + datosMiembro.getApellidos().toUpperCase());
//                            if (contador1 == 0) {
//                                miembros += "" + docenteMiembro.getTituloDocenteId().getTituloId().getAbreviacion() + " " + datosMiembro.getNombres() + " " + datosMiembro.getApellidos() + " (" + miembro.getCargoId().getNombre() + ")";
//                                contador1++;
//                            } else {
//                                miembros += ", " + docenteMiembro.getTituloDocenteId().getTituloId().getAbreviacion() + " " + datosMiembro.getNombres() + " " + datosMiembro.getApellidos() + " (" + miembro.getCargoId().getNombre() + ") ";
//                                contador1++;
//                            }
//                        }
//                    }
//                }
//            }
//            /*Nota*/
//            if (imprimirCambios) {
//                nota = bundle.getString("lbl.plazo_cambios") + " " + numeroDiasPlazo + " " + bundle.getString("lbl.dias_plazo_cambios") + ". " + bundle.getString("lbl.nota_privada_no_apta");
//                cambiosCabeceraTabla.add(bundle.getString("lbl.cambios"));
//                if (evaluacionTribunal != null) {
//                    if (evaluacionTribunal.getCalificacionMiembroList() != null) {
//                        for (CalificacionMiembro calificacionMiembro : evaluacionTribunal.getCalificacionMiembroList()) {
//                            if (calificacionMiembro.getSugerenciaCalificacionMiembroList() != null) {
//                                for (SugerenciaCalificacionMiembro sugerenciaCalificacionMiembro : calificacionMiembro.getSugerenciaCalificacionMiembroList()) {
//                                    cambiosTabla.add(sugerenciaCalificacionMiembro.getDescripcion());
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            /*Parámetros*/
//            if (evaluacionTribunal != null) {
//                int contador2 = 0;
//                List<Parametro> parametros1 = new ArrayList<>();
//                for (CalificacionMiembro calificacionMiembro : evaluacionTribunal.getCalificacionMiembroList()) {
//                    for (CalificacionParametro calificacionParametro : calificacionMiembro.getCalificacionParametroList()) {
//                        if (!parametros1.contains(calificacionParametro.getParametroCatEvId().getParametroId())) {
//                            parametros1.add(calificacionParametro.getParametroCatEvId().getParametroId());
//                            if (contador2 == 0) {
//                                parametros += "" + calificacionParametro.getParametroCatEvId().getParametroId().getNombre() + "";
//                                contador2++;
//                            } else {
//                                parametros += ", " + calificacionParametro.getParametroCatEvId().getParametroId().getNombre() + "";
//                            }
//                        }
//                    }
//                }
//                /*Análisis de la Sustentación*/
//                if (tipoActa == 2) {
//                    if (evaluacionTribunal.getEsAptoCalificar()) {
//                        analisis = bundle.getString("lbl.asunto_privada");
//                    } else {
//                        analisis = "";
//                    }
//                } else {
//                    analisis = bundle.getString("lbl.asunto_privada_preliminar") + " " + bundle.getString("lbl_los_siguientes") + " " + bundle.getString("lbl.parametros") + ": " + parametros;
//                }
//            }
//
//            asunto = bundle.getString("lbl.a_los") + " " + evaluacionTribunal.getFechaInicio().getDate() + " " + bundle.getString("lbl.dias") + " " + bundle.getString("lbl.del_mes") + " " + mes + " "
//                    + "" + bundle.getString("lbl.de") + " " + anio + " " + bundle.getString("lbl.en_reunion_reservada") + " " + bundle.getString("lbl.presidida_tribunal") + " "
//                    + " " + miembros + ", " + egresado + " " + autores + " " + bundle.getString("lbl_a_defender_trabajo") + ": " + tema + ", " + analisis + ". " + resolucion + " ";
//
//            if (tipo.equalsIgnoreCase("pdf")) {
//                GeneraPdf generaPdf = new GeneraPdf();
//                generaPdf.crear("A4");
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                generaPdf.printerWriter(baos);
//                generaPdf.open();
//                generaPdf.addElemento(generaPdf.logoPath(logoInstitucion, 45f, 800f, 45));
//                generaPdf.addElemento(generaPdf.logoBytes(carrera.getLogo(), 500f, 800f, 50));
//                generaPdf.addElemento(generaPdf.parrafo(carrera.getAreaId().getNombre(), "Arial", 16, 15f, 15f, true, 1));
//                generaPdf.addElemento(generaPdf.parrafo(titulo, "Arial", 16, 10f, 10f, true, 1));
//                generaPdf.addElemento(generaPdf.parrafo(asunto, "Arial", 11, 10f, 10f, false, 2));
//                if (cambiosTabla.size() > 0) {
//                    generaPdf.addElemento(generaPdf.tabla(cambiosCabeceraTabla, cambiosTabla, "Arial", 14, 11, true, false, 5, 1, 2, 10f, 10f));
//                }
//                generaPdf.addElemento(generaPdf.parrafo(nota, "Arial", 11, 20f, 10f, false, 2));
//                generaPdf.addElemento(generaPdf.tabla(miembrosCabeceraTabla, miembrosTabla, "Arial", 14, 11, true, false, 20, 1, 1, 10f, 10f));
//                generaPdf.addElemento(generaPdf.tabla(autoresCabeceraTabla, autoresTabla, "Arial", 14, 11, true, false, 20, 1, 1, 10f, 10f));
//                generaPdf.close();
//                response.setCharacterEncoding("ISO-8859-1");
//                response.setContentType("application/pdf");
//                response.setContentLength(baos.size());
//                ServletOutputStream os = response.getOutputStream();
//                baos.writeTo(os);
//                os.flush();
//                os.close();
//                /*Grabar Acta*/
//                acta = actaFacadeLocal.buscarPorEvaluacionCategoria(evaluacionTribunal.getId(), catalogoActa.getId());
//                if (acta == null) {
//                    acta = new Acta();
//                    acta.setEvaluacionTribunalId(evaluacionTribunal);
//                    acta.setDocumento(baos.toByteArray());
//                    acta.setCatalogoActaId(catalogoActa);
//                    acta.setNumeracion(numeroActa);
//                    if (configuracionCarrera != null) {
//                        int valor = Integer.parseInt(numeroActa) + 1;
//                        configuracionCarrera.setValor(valor + "");
//                    }
//                    grabar(acta, configuracionCarrera);
//                } else {
//                    acta.setDocumento(baos.toByteArray());
//                    if (configuracionCarrera != null) {
//                        int valor = Integer.parseInt(numeroActa) + 1;
//                        configuracionCarrera.setValor(valor + "");
//                    }
//                    grabar(acta, configuracionCarrera);
//                }
//            } else {
//                if (tipo.equalsIgnoreCase("docx")) {
//                    GeneraDoc generaDoc = new GeneraDoc();
//                    generaDoc.crear();
//                    generaDoc.parrafo(carrera.getAreaId().getNombre(), 1, 16, true, 20, 20);
//                    generaDoc.parrafo(titulo, 1, 16, true, 20, 20);
//                    generaDoc.parrafo(asunto, 2, 11, false, 20, 20);
//                    if (cambiosTabla.size() > 0) {
//                        generaDoc.tabla(construirMatriz(cambiosCabeceraTabla, cambiosCabeceraTabla.size(), 1), cambiosCabeceraTabla.size(), 1, 5, 14, 1, true);
//                        generaDoc.tabla(construirMatriz(cambiosTabla, cambiosTabla.size(), 1), cambiosTabla.size(), cambiosCabeceraTabla.size(), 5, 11, 2, false);
//                    }
//                    generaDoc.parrafo(nota, 2, 11, false, 20, 20);
//                    generaDoc.tabla(construirMatriz(miembrosCabeceraTabla, 1, miembrosCabeceraTabla.size()), 1, miembrosCabeceraTabla.size(), 15, 14, 1, true);
//                    generaDoc.tabla(construirMatriz(miembrosTabla, 1, miembrosTabla.size()), 1, miembrosTabla.size(), 15, 11, 1, false);
//                    generaDoc.parrafo(" ", 2, 11, false, 20, 20);
//                    generaDoc.tabla(construirMatriz(autoresCabeceraTabla, 1, autoresCabeceraTabla.size()), 1, autoresCabeceraTabla.size(), 15, 14, 1, true);
//                    generaDoc.tabla(construirMatriz(autoresTabla, 1, autoresTabla.size()), 1, autoresTabla.size(), 15, 11, 1, false);
//                    response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//                    response.setHeader("Content-Disposition",
//                            "attachment; filename=Acta.docx");
//                    response.setCharacterEncoding("ISO-8859-1");
//                    ServletOutputStream outputStream = response.getOutputStream();
//                    generaDoc.write(outputStream);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }

    }

    public boolean isRenderedDlgActaPrivada() {
        return renderedDlgActaPrivada;
    }

    public void setRenderedDlgActaPrivada(boolean renderedDlgActaPrivada) {
        this.renderedDlgActaPrivada = renderedDlgActaPrivada;
    }

    public Long getEvaluacionId() {
        return evaluacionId;
    }

    public void setEvaluacionId(Long evaluacionId) {
        this.evaluacionId = evaluacionId;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public int getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(int carreraId) {
        this.carreraId = carreraId;
    }

    public int getTipoActaEnPrivada() {
        return tipoActaEnPrivada;
    }

    public void setTipoActaEnPrivada(int tipoActaEnPrivada) {
        this.tipoActaEnPrivada = tipoActaEnPrivada;
    }

    public boolean isRenderedDlgActaGrado() {
        return renderedDlgActaGrado;
    }

    public void setRenderedDlgActaGrado(boolean renderedDlgActaGrado) {
        this.renderedDlgActaGrado = renderedDlgActaGrado;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

}
