/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.sigett.servlet;

import edu.unl.sigett.documentoProyecto.SessionDocumentoProyecto;
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
    private SessionDocumentoProyecto sessionDocumentoProyecto;
    //</editor-fold>

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String entity = request.getParameter("entity");
            if (entity == null) {
                return;
            }
            switch (entity) {
                case "documentoProyecto":
                    if (sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    }
                    if (sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    }
                    if (sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents() == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                        return;
                    }
                    response.setCharacterEncoding("ISO-8859-1");
                    response.setContentType("application/pdf");
                    response.setContentLength(sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents().length);
                    response.getOutputStream().write(sessionDocumentoProyecto.getDocumentoProyectoDTOSeleccionado().getDocumento().getContents());
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    break;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
