/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.autorProyecto;

import com.jlmallas.comun.entity.Item;
import edu.jlmallas.academico.entity.OfertaAcademica;
import edu.unl.sigett.directorProyecto.DirectorProyectoDTO;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorge-luis
 */
@Named(value = "sessionAutorProyecto")
@SessionScoped
public class SessionAutorProyecto implements Serializable {

    private AutorProyectoDTO autorProyectoDTO;

    private List<AutorProyectoDTO> autorProyectos;
    private List<DirectorProyectoDTO> directoresProyectoDTO;
    private List<Item> estados;
    private List<Item> categorias;
    private List<Item> tipos;
    private List<AutorProyectoDTO> filterAutorProyectos;

    public SessionAutorProyecto() {
        this.estados=new ArrayList<>();
        this.categorias=new ArrayList<>();
        this.tipos=new ArrayList<>();
        this.directoresProyectoDTO=new ArrayList<>();
        this.estados=new ArrayList<>();
        this.categorias=new ArrayList<>();
        this.filterAutorProyectos = new ArrayList<>();
        this.autorProyectos = new ArrayList<>();
        this.autorProyectoDTO = new AutorProyectoDTO();
    }

    public AutorProyectoDTO getAutorProyectoDTO() {
        return autorProyectoDTO;
    }

    public void setAutorProyectoDTO(AutorProyectoDTO autorProyectoDTO) {
        this.autorProyectoDTO = autorProyectoDTO;
    }

    public List<AutorProyectoDTO> getAutorProyectos() {
        return autorProyectos;
    }

    public void setAutorProyectos(List<AutorProyectoDTO> autorProyectos) {
        this.autorProyectos = autorProyectos;
    }

    public List<AutorProyectoDTO> getFilterAutorProyectos() {
        return filterAutorProyectos;
    }

    public void setFilterAutorProyectos(List<AutorProyectoDTO> filterAutorProyectos) {
        this.filterAutorProyectos = filterAutorProyectos;
    }

    public List<Item> getEstados() {
        return estados;
    }

    public void setEstados(List<Item> estados) {
        this.estados = estados;
    }

    public List<Item> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Item> categorias) {
        this.categorias = categorias;
    }

    public List<Item> getTipos() {
        return tipos;
    }

    public void setTipos(List<Item> tipos) {
        this.tipos = tipos;
    }

    public List<DirectorProyectoDTO> getDirectoresProyectoDTO() {
        return directoresProyectoDTO;
    }

    public void setDirectoresProyectoDTO(List<DirectorProyectoDTO> directoresProyectoDTO) {
        this.directoresProyectoDTO = directoresProyectoDTO;
    }

}
