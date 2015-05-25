/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.autor;

import edu.unl.sigett.autor.dto.AspiranteDTO;
import edu.unl.sigett.entity.AutorProyecto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JorgeLuis
 */
@Named
@SessionScoped
public class SessionAutorProyecto implements Serializable {

    private AutorProyecto autorProyecto;
    private AspiranteDTO aspiranteDTOSeleccionado;

    private List<AspiranteDTO> aspirantesDTO;
    private List<AspiranteDTO> filterAspirantesDTO;
    
    private Boolean renderedBuscarAspirantes;
    private Boolean renderedSeleccionar;

    public SessionAutorProyecto() {
        this.aspiranteDTOSeleccionado=new AspiranteDTO();
        this.filterAspirantesDTO = new ArrayList<>();
        this.aspirantesDTO = new ArrayList<>();
        this.autorProyecto = new AutorProyecto();
    }

    public AutorProyecto getAutorProyecto() {
        return autorProyecto;
    }

    public void setAutorProyecto(AutorProyecto autorProyecto) {
        this.autorProyecto = autorProyecto;
    }

    public Boolean getRenderedBuscarAspirantes() {
        return renderedBuscarAspirantes;
    }

    public void setRenderedBuscarAspirantes(Boolean renderedBuscarAspirantes) {
        this.renderedBuscarAspirantes = renderedBuscarAspirantes;
    }

    public List<AspiranteDTO> getAspirantesDTO() {
        return aspirantesDTO;
    }

    public void setAspirantesDTO(List<AspiranteDTO> aspirantesDTO) {
        this.aspirantesDTO = aspirantesDTO;
    }

    public List<AspiranteDTO> getFilterAspirantesDTO() {
        return filterAspirantesDTO;
    }

    public void setFilterAspirantesDTO(List<AspiranteDTO> filterAspirantesDTO) {
        this.filterAspirantesDTO = filterAspirantesDTO;
    }

    public Boolean getRenderedSeleccionar() {
        return renderedSeleccionar;
    }

    public void setRenderedSeleccionar(Boolean renderedSeleccionar) {
        this.renderedSeleccionar = renderedSeleccionar;
    }

    public AspiranteDTO getAspiranteDTOSeleccionado() {
        return aspiranteDTOSeleccionado;
    }

    public void setAspiranteDTOSeleccionado(AspiranteDTO aspiranteDTOSeleccionado) {
        this.aspiranteDTOSeleccionado = aspiranteDTOSeleccionado;
    }

}
