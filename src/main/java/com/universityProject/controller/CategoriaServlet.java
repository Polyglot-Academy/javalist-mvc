package com.universityProject.controller;

import com.universityProject.model.Categoria;
// Assuming Tarefa model is not directly needed here unless for future advanced features
import com.universityProject.service.ApiService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/categorias/*")
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
        if (action == null) action = "create";

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
            handleError(request, response, "Erro ao processar solicitação de Categoria: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        if (action == null) {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
                action = "list";
            } else if (pathInfo.equals("/new")) {
                action = "new";
            } else if (pathInfo.equals("/edit")) {
                action = "edit";
            } else if (pathInfo.equals("/delete")) {
                action = "delete";
            } else {
                action = "list";
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
            handleError(request, response, "Erro ao carregar dados de Categoria: " + e.getMessage(), e);
        }
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, String userMessage, Exception e) throws ServletException, IOException {
        System.err.println(userMessage); // Log the user-friendly message
        if (e != null) {
            e.printStackTrace(); // Log the full stack trace for debugging
        }
        // Attempt to show the list view with an error message
        try {
            List<Categoria> listCategorias = apiService.getAllCategorias(); // Attempt to load
            request.setAttribute("listCategorias", listCategorias);
        } catch (Exception listEx) {
            System.err.println("Could not fetch categorias for error display: " + listEx.getMessage());
        }
        request.setAttribute("errorMessage", userMessage);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/categoria-list.jsp");
        dispatcher.forward(request, response);
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
        request.setAttribute("categoria", new Categoria()); // For form binding
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/categoria-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            handleError(request, response, "ID da Categoria inválido.", e);
            return;
        }
        Categoria existingCategoria = apiService.getCategoriaById(id);
        if (existingCategoria == null) {
            handleError(request, response, "Categoria não encontrada para ID: " + id, null);
            return;
        }
        request.setAttribute("categoria", existingCategoria);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/categoria-form.jsp");
        dispatcher.forward(request, response);
    }

    private void createCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String nome = request.getParameter("nome_Categoria");
        String descricao = request.getParameter("descricao_Categoria");

        if (nome == null || nome.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Nome da categoria é obrigatório.");
            Categoria categoriaToDisplay = new Categoria(); // Use default constructor
            categoriaToDisplay.setDescricao_Categoria(descricao); // Preserve submitted description
            request.setAttribute("categoria", categoriaToDisplay);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/categoria-form.jsp");
            dispatcher.forward(request, response);
            return;
        }
        // Assumes Categoria model has appropriate setters or a constructor
        Categoria newCategoria = new Categoria(); // Use default constructor
        newCategoria.setNome_Categoria(nome);
        newCategoria.setDescricao_Categoria(descricao);
        // The Tarefas list in newCategoria will be empty/null by default, which is correct.
        apiService.createCategoria(newCategoria);
        response.sendRedirect(request.getContextPath() + "/categorias/list");
    }

    private void updateCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id_Categoria"));
        } catch (NumberFormatException e) {
            handleError(request, response, "ID da Categoria inválido para atualização.", e);
            return;
        }
        String nome = request.getParameter("nome_Categoria");
        String descricao = request.getParameter("descricao_Categoria");

        if (nome == null || nome.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Nome da categoria é obrigatório.");
            Categoria categoriaToDisplay = apiService.getCategoriaById(id); // Get original to preserve other fields potentially
            if(categoriaToDisplay == null) categoriaToDisplay = new Categoria(); // Fallback
            categoriaToDisplay.setId_Categoria(id); // Ensure ID is set
            // Submitted values:
            // Nome will be re-entered by user.
            categoriaToDisplay.setDescricao_Categoria(descricao);
            request.setAttribute("categoria", categoriaToDisplay);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/categoria-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Categoria categoria = new Categoria(); // Use default constructor
        categoria.setId_Categoria(id);
        categoria.setNome_Categoria(nome);
        categoria.setDescricao_Categoria(descricao);
        // Tarefas list will be empty/null, so not sending tasks for update, which is correct.
        apiService.updateCategoria(id, categoria);
        response.sendRedirect(request.getContextPath() + "/categorias/list");
    }

    private void deleteCategoria(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            handleError(request, response, "ID da Categoria inválido para exclusão.", e);
            return;
        }
        apiService.deleteCategoria(id);
        response.sendRedirect(request.getContextPath() + "/categorias/list");
    }
}