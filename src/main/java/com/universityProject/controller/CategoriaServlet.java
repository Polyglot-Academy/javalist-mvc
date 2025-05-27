package com.universityProject.controller;

import com.universityProject.model.Categoria;
import com.universityProject.service.ApiService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/categorias/*") // Handles all paths starting with /categorias
public class CategoriaServlet extends HttpServlet {
    private ApiService apiService;

    @Override
    public void init() {
        apiService = new ApiService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "create"; // Default action for POST if not specified

        try {
            switch (action) {
                case "create":
                    createCategoria(request, response);
                    break;
                case "update":
                    updateCategoria(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/categorias/list");
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // e.g., /list, /edit, /delete, /new
        String action = request.getParameter("action");

        if (action == null) { // Infer action from path
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
                action = "list";
            } else if (pathInfo.equals("/new")) {
                action = "new";
            } else if (pathInfo.equals("/edit")) {
                action = "edit";
            } else if (pathInfo.equals("/delete")) {
                action = "delete";
            } else {
                action = "list"; // Default
            }
        }


        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCategoria(request, response);
                    break;
                case "list":
                default:
                    listCategorias(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listCategorias(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Categoria> listCategorias = apiService.getAllCategorias();
        request.setAttribute("listCategorias", listCategorias);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/categoria-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/categoria-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Categoria existingCategoria = apiService.getCategoriaById(id);
        request.setAttribute("categoria", existingCategoria);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/categoria-form.jsp");
        dispatcher.forward(request, response);
    }

    private void createCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String nome = request.getParameter("nome_Categoria");
        String descricao = request.getParameter("descricao_Categoria");
        Categoria newCategoria = new Categoria(0, nome, descricao); // API should handle ID generation
        apiService.createCategoria(newCategoria);
        response.sendRedirect(request.getContextPath() + "/categorias/list");
    }

    private void updateCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id_Categoria"));
        String nome = request.getParameter("nome_Categoria");
        String descricao = request.getParameter("descricao_Categoria");
        Categoria categoria = new Categoria(id, nome, descricao);
        apiService.updateCategoria(id, categoria);
        response.sendRedirect(request.getContextPath() + "/categorias/list");
    }

    private void deleteCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        apiService.deleteCategoria(id);
        response.sendRedirect(request.getContextPath() + "/categorias/list");
    }
}