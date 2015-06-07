/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.servlet;

import edu.unl.sigett.documentoProyecto.DocumentoProyectoDM;
import edu.unl.sigett.pertinencia.PertinenciaDM;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JorgeLuis
 */
@WebServlet(name = "AppServlet", urlPatterns = {"/AppServlet/*"})
public class AppServlet extends HttpServlet {

    //<editor-fold defaultstate="collapsed" desc="MANAGED BEANS">
    @Inject
    private PertinenciaDM pertinenciaDM;
    @Inject
    private DocumentoProyectoDM documentoProyectoDM;
    //</editor-fold>

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String entity = request.getParameter("entity");
            if (entity == null) {
                return;
            }
            switch (entity) {
                case "pertinencia":
                    if (pertinenciaDM.getDocumentoCarreraDTO() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    if (pertinenciaDM.getDocumentoCarreraDTO().getDocumento().getContents() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    response.setCharacterEncoding("ISO-8859-1");
                    response.setContentType("application/pdf");
                    response.setContentLength(pertinenciaDM.getDocumentoCarreraDTO().getDocumento().getContents().length);
                    response.getOutputStream().write(pertinenciaDM.getDocumentoCarreraDTO().getDocumento().getContents(), 0,
                            pertinenciaDM.getDocumentoCarreraDTO().getDocumento().getContents().length);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    break;
                case "documentoProyecto":
                    if (documentoProyectoDM.getDocumentoProyectoDTOSeleccionado() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    if (documentoProyectoDM.getDocumentoProyectoDTOSeleccionado().getDocumento() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    if (documentoProyectoDM.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    }
                    response.setCharacterEncoding("ISO-8859-1");
                    response.setContentType("application/pdf");
                    response.setContentLength(documentoProyectoDM.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents().length);
                    response.getOutputStream().write(documentoProyectoDM.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents());
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    break;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
