package com.universityProject.controller;

import com.universityProject.model.Categoria;
import com.universityProject.model.Tarefa;
import com.universityProject.service.ApiService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/tarefas/*")
public class TarefaServlet extends HttpServlet {
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
                    createTarefa(request, response);
                    break;
                case "update":
                    updateTarefa(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/tarefas/list");
                    break;
            }
        } catch (Exception e) {
            // Add proper error handling: log the error, set an error attribute, forward to an error page
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            e.printStackTrace(); // For debugging
            // For a user-friendly error page, you might forward instead of just throwing ServletException
            listTarefasWithError(request, response, "Error processing request: " + e.getMessage());
            // throw new ServletException(e);
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
                    showNewTarefaForm(request, response);
                    break;
                case "edit":
                    showEditTarefaForm(request, response);
                    break;
                case "delete":
                    deleteTarefa(request, response);
                    break;
                case "list":
                default:
                    listTarefas(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            e.printStackTrace();
            listTarefasWithError(request, response, "Error processing request: " + e.getMessage());
            // throw new ServletException(e);
        }
    }

    private void listTarefasWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        try {
            List<Tarefa> listTarefas = apiService.getAllTarefas();
            request.setAttribute("listTarefas", listTarefas);
            List<Categoria> listCategorias = apiService.getAllCategorias(); // For dropdown
            request.setAttribute("listCategorias", listCategorias);
            request.setAttribute("errorMessage", errorMessage); // Pass the error message
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Could not fetch tarefas: " + e.getMessage());
            e.printStackTrace();
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tarefa-list.jsp");
        dispatcher.forward(request, response);
    }


    private void listTarefas(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Tarefa> listTarefas = apiService.getAllTarefas();
        request.setAttribute("listTarefas", listTarefas);
        // Optionally, load categorias for display purposes if needed on the list page
        // List<Categoria> listCategorias = apiService.getAllCategorias();
        // request.setAttribute("listCategorias", listCategorias);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tarefa-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewTarefaForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Categoria> listCategorias = apiService.getAllCategorias();
        request.setAttribute("listCategorias", listCategorias);
        request.setAttribute("statusOptions", new String[]{"Não Iniciado", "Em Andamento", "Concluído"});
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tarefa-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditTarefaForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Tarefa existingTarefa = apiService.getTarefaById(id);
        List<Categoria> listCategorias = apiService.getAllCategorias();
        request.setAttribute("tarefa", existingTarefa);
        request.setAttribute("listCategorias", listCategorias);
        request.setAttribute("statusOptions", new String[]{"Não Iniciado", "Em Andamento", "Concluído"});
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tarefa-form.jsp");
        dispatcher.forward(request, response);
    }

    private LocalDate parseLocalDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString); // Assumes YYYY-MM-DD
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + dateString + " - " + e.getMessage());
            return null; // Or handle error appropriately
        }
    }

    private void createTarefa(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String titulo = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");
        LocalDate dataCriacao = parseLocalDate(request.getParameter("data_criacao"));
        LocalDate dataConclusao = parseLocalDate(request.getParameter("data_conclusao"));
        String status = request.getParameter("status");
        int idCategoria = Integer.parseInt(request.getParameter("id_Categoria"));

        Tarefa newTarefa = new Tarefa();
        newTarefa.setTitulo(titulo);
        newTarefa.setDescricao(descricao);
        newTarefa.setData_criacao(dataCriacao);
        newTarefa.setData_conclusao(dataConclusao);
        newTarefa.setStatus(status);
        newTarefa.setId_Categoria(idCategoria);

        apiService.createTarefa(newTarefa);
        response.sendRedirect(request.getContextPath() + "/tarefas/list");
    }

    private void updateTarefa(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id_Tarefas"));
        String titulo = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");
        LocalDate dataCriacao = parseLocalDate(request.getParameter("data_criacao"));
        LocalDate dataConclusao = parseLocalDate(request.getParameter("data_conclusao"));
        String status = request.getParameter("status");
        int idCategoria = Integer.parseInt(request.getParameter("id_Categoria"));

        Tarefa tarefa = new Tarefa();
        tarefa.setId_Tarefas(id);
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        tarefa.setData_criacao(dataCriacao);
        tarefa.setData_conclusao(dataConclusao);
        tarefa.setStatus(status);
        tarefa.setId_Categoria(idCategoria);

        apiService.updateTarefa(id, tarefa);
        response.sendRedirect(request.getContextPath() + "/tarefas/list");
    }

    private void deleteTarefa(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        apiService.deleteTarefa(id);
        response.sendRedirect(request.getContextPath() + "/tarefas/list");
    }
}