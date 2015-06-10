/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.seguimiento.controlador;

import com.jlmallas.comun.dao.PersonaDao;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import edu.jlmallas.academico.entity.Area;
import edu.unl.sigett.entity.AutorProyecto;
import edu.jlmallas.academico.entity.Carrera;
import edu.jlmallas.academico.entity.EstudianteCarrera;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.unl.sigett.entity.LineaInvestigacion;
import edu.unl.sigett.entity.LineaInvestigacionCarrera;
import edu.jlmallas.academico.entity.PeriodoAcademico;
import edu.unl.sigett.entity.Proyecto;
import edu.unl.sigett.entity.ProyectoCarreraOferta;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;
import edu.jlmallas.academico.service.AreaService;
import edu.jlmallas.academico.service.CarreraService;
import edu.jlmallas.academico.dao.EstudianteCarreraDao;
import edu.jlmallas.academico.service.OfertaAcademicaService;
import edu.unl.sigett.dao.ConfiguracionGeneralDao;
import edu.unl.sigett.dao.LineaInvestigacionCarreraDao;
import edu.unl.sigett.dao.ProyectoOfertaCarreraDao;
import edu.unl.sigett.dao.ProyectoDao;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jorge-luis
 */
@Named(value = "viewProyectos")
@SessionScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "directorio",
            pattern = "/directorio/",
            viewId = "/faces/pages/sigett/directorioProyectos.xhtml"
    ),
    @URLMapping(
            id = "viewProject",
            pattern = "/viewProject/#{sessionConsultarProyecto.proyecto.id}",
            viewId = "/faces/pages/sigett/verProyecto.xhtml"
    )
})
public class ViewProyectos implements Serializable {

//    @Inject
//    private AdministrarDirectoresProyecto directoresProyecto;
    private String filtro;
    private Carrera carrera;
    private PeriodoAcademico periodoAcademico;
    private String categoria;
    private String area;
    private LineaInvestigacion li;
    private String estado;
    private boolean selectByPeriodo;
    private boolean selectByCarrera;
    private boolean selectByLineaInvestigacion;
    private List<Carrera> carreras;
    private List<LineaInvestigacionCarrera> lineaInvestigacionCarreras;
    private List<Proyecto> proyectos;
    @EJB
    private CarreraService carreraFacadeLocal;
    @EJB
    private AreaService areaFacadeLocal;
    @EJB
    private ProyectoDao proyectoFacadeLocal;
    @EJB
    private OfertaAcademicaService ofertaAcademicaFacadeLocal;
    @EJB
    private EstudianteCarreraDao estudianteCarreraFacadeLocal;
    @EJB
    private PersonaDao personaFacadeLocal;
    @EJB
    private ConfiguracionGeneralDao configuracionGeneralFacadeLocal;
    @EJB
    private LineaInvestigacionCarreraDao lineaInvestigacionCarreraFacadeLocal;
    @EJB
    private ProyectoOfertaCarreraDao proyectoCarreraOfertaFacadeLocal;

    private String criterioLi;
    private String criterioCarrera;

    public ViewProyectos() {
    }

    public void buscarCarreras(String area, String criterio) {
        try {
            this.carreras = new ArrayList<>();
            int pos = area.indexOf(":");
            Area a = areaFacadeLocal.buscarPorId(Integer.parseInt(area.substring(0, pos)));
            if (a != null) {
                for (Carrera c : carreraFacadeLocal.buscarPorCriterio(carrera)) {
                    if (c.getNombre().toLowerCase().contains(criterio.toLowerCase())) {
                        carreras.add(c);
                    }
                }
            }
            buscarWebSemantica(a.getSigla());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int countProyectosCarrera(Carrera c) {
        int count = 0;
        try {
            count = proyectoFacadeLocal.buscarByCarrera(c.getId()).size();
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    public int countProyectosPeriodo(PeriodoAcademico p) {
        int count = 0;
        try {
            count = proyectoFacadeLocal.buscarByPeriodo(p.getId()).size();
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    public int countProyectosLineas(LineaInvestigacionCarrera lc) {
        int count = 0;
        try {
            count = proyectoFacadeLocal.buscarByLi(lc.getLineaInvestigacionId().getId()).size();
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    public List<PeriodoAcademico> listadoPeriodosAcademicos() {
        List<PeriodoAcademico> periodoAcademicos = new ArrayList<>();
        try {
            for (ProyectoCarreraOferta pco : proyectoCarreraOfertaFacadeLocal.findAll()) {
                OfertaAcademica ofertaAcademica = ofertaAcademicaFacadeLocal.find(pco.getOfertaAcademicaId());
                if (!periodoAcademicos.contains(ofertaAcademica.getPeriodoAcademicoId())) {
                    periodoAcademicos.add(ofertaAcademica.getPeriodoAcademicoId());
                }
            }
        } catch (Exception e) {
        }
        return periodoAcademicos;
    }

    public void editarLineaInvestigacion(SelectEvent event) {
        try {
            LineaInvestigacionCarrera lc = (LineaInvestigacionCarrera) event.getObject();
            li = lc.getLineaInvestigacionId();
            buscarWebSemantica(li.getNombre());
//            buscar(carrera, li, periodoAcademico, filtro, categoria, estado, selectByPeriodo, true, selectByCarrera);
        } catch (Exception e) {
        }
    }

    public void editarPeriodo(SelectEvent event) {
        try {
            periodoAcademico = (PeriodoAcademico) event.getObject();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaCreate = formatoFecha.format(periodoAcademico.getFechaInicio());
            buscarWebSemantica(fechaCreate);
        } catch (Exception e) {
        }
    }

    public boolean contienePeriodoAcademico(List<PeriodoAcademico> periodoAcademicos, PeriodoAcademico p) {
        boolean var = false;
        for (PeriodoAcademico pa : periodoAcademicos) {
            if (pa.equals(p)) {
                var = true;
                break;
            }
        }
        return var;
    }

    public String editar(Proyecto proyecto) {
        String navegacion = "";
        try {
//            sessionConsultarProyecto.setProyecto(proyecto);
//            autoresProyecto.listadoAutores("", proyecto);
//            directoresProyecto.historialDirectoresProyecto("", proyecto);
            navegacion = "pretty:viewProject";
        } catch (Exception e) {
        }
        return navegacion;
    }

    public String autores(Proyecto proyecto) {
        String autores = "";
        int contador = 0;
        for (AutorProyecto autorProyecto : proyecto.getAutorProyectoList()) {
            EstudianteCarrera estudianteCarrera = estudianteCarreraFacadeLocal.find(autorProyecto.getAspiranteId().getId());
//            if (autorProyecto.getRenunciaAutorList().isEmpty()) {
//                if (contador == 0) {
//                    autores += estudianteCarrera.getEstudianteId() + " ";
//                } else {
//                    autores += ", " + estudianteCarrera.getEstudianteId();
//                }
//                contador++;
//            }
        }
        return autores;
    }

    public void buscarLineasInvestigacionPorCarrera(Carrera carrera, String criterio) {
        try {
            this.lineaInvestigacionCarreras = new ArrayList<>();
            this.carrera = carrera;
            li = new LineaInvestigacion();
//            for (LineaInvestigacionCarrera li : lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(carrera.getId())) {
//                if (li.getLineaInvestigacionId().getNombre().toLowerCase().contains(criterio.toLowerCase())) {
//                    lineaInvestigacionCarreras.add(li);
//                }
//            }
//            buscar(carrera, li, periodoAcademico, filtro, categoria, estado, selectByPeriodo, selectByLineaInvestigacion, true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onRowSelectCarrera(SelectEvent event) {
        try {
            this.lineaInvestigacionCarreras = new ArrayList<>();
            carrera = ((Carrera) event.getObject());
            li = new LineaInvestigacion();
//            for (LineaInvestigacionCarrera li : lineaInvestigacionCarreraFacadeLocal.buscarPorCarrera(carrera.getId())) {
//                if (criterioLi != null) {
//                    if (li.getLineaInvestigacionId().getNombre().toLowerCase().contains(criterioLi.toLowerCase())) {
//                        lineaInvestigacionCarreras.add(li);
//                    }
//                } else {
//                    lineaInvestigacionCarreras.add(li);
//                }
//            }
            buscarWebSemantica(carrera.getNombre());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarWebSemantica(String filtro) {
        try {
            Map parametros = new HashMap();
//            ProyectoResource proyectoResource = new ProyectoResource(configuracionGeneralFacadeLocal.find(17).getValor());
            parametros.put("filtro", filtro);
//            List<String> valores = proyectoResource.buscarTodo(parametros);
//            for (String valor : valores) {
//                Proyecto proyecto = proyectoFacadeLocal.find(Long.parseLong(valor));
//                if (proyecto != null && !this.proyectos.contains(proyecto)) {
//                    this.proyectos.add(proyecto);
//                }
//            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isSelectByPeriodo() {
        return selectByPeriodo;
    }

    public void setSelectByPeriodo(boolean selectByPeriodo) {
        this.selectByPeriodo = selectByPeriodo;
    }

    public boolean isSelectByCarrera() {
        return selectByCarrera;
    }

    public void setSelectByCarrera(boolean selectByCarrera) {
        this.selectByCarrera = selectByCarrera;
    }

    public boolean isSelectByLineaInvestigacion() {
        return selectByLineaInvestigacion;
    }

    public void setSelectByLineaInvestigacion(boolean selectByLineaInvestigacion) {
        this.selectByLineaInvestigacion = selectByLineaInvestigacion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(List<Carrera> carreras) {
        this.carreras = carreras;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public PeriodoAcademico getPeriodoAcademico() {
        return periodoAcademico;
    }

    public void setPeriodoAcademico(PeriodoAcademico periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public LineaInvestigacion getLi() {
        return li;
    }

    public void setLi(LineaInvestigacion li) {
        this.li = li;
    }

    public List<LineaInvestigacionCarrera> getLineaInvestigacionCarreras() {
        return lineaInvestigacionCarreras;
    }

    public void setLineaInvestigacionCarreras(List<LineaInvestigacionCarrera> lineaInvestigacionCarreras) {
        this.lineaInvestigacionCarreras = lineaInvestigacionCarreras;
    }

    public String getCriterioLi() {
        return criterioLi;
    }

    public void setCriterioLi(String criterioLi) {
        this.criterioLi = criterioLi;
    }

    public String getCriterioCarrera() {
        return criterioCarrera;
    }

    public void setCriterioCarrera(String criterioCarrera) {
        this.criterioCarrera = criterioCarrera;
    }

}
