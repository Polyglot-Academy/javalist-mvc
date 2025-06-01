package com.universityProject.controller;

import com.universityProject.model.Categoria;
import com.universityProject.model.Tarefa;
import com.universityProject.service.ApiService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; // Make sure this is imported
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Change the mapping from "/home" to "" (empty string)
@WebServlet("") // This will map the servlet to the context root (e.g., /JavaList/)
public class HomeServlet extends HttpServlet {
    private ApiService apiService;

    @Override
    public void init() throws ServletException {
        apiService = new ApiService();
        System.out.println("HomeServlet initialized and mapped to context root.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Tarefa> pendingTarefas = new ArrayList<>();
        List<Categoria> listCategorias = new ArrayList<>();

        try {
            // ... (your existing data fetching and filtering logic remains the same) ...
            System.out.println("HomeServlet: doGet - START (mapped to root)");

            System.out.println("HomeServlet: Attempting to fetch all tarefas from ApiService...");
            List<Tarefa> allTarefas = apiService.getAllTarefas();

            if (allTarefas == null) {
                System.out.println("HomeServlet: apiService.getAllTarefas() returned NULL.");
                allTarefas = new ArrayList<>();
            } else {
                System.out.println("HomeServlet: Fetched " + allTarefas.size() + " tarefas in total from ApiService.");
                // Optional: Log individual tasks if still debugging
                // for (Tarefa t : allTarefas) {
                //     System.out.println("  - Raw Tarefa from ApiService: ID=" + t.getId_Tarefas() + ", Status=" + t.getStatus() + ", Titulo=" + t.getTitulo());
                // }
            }

            System.out.println("HomeServlet: Attempting to fetch all categorias from ApiService...");
            listCategorias = apiService.getAllCategorias();
            if (listCategorias == null) {
                System.out.println("HomeServlet: apiService.getAllCategorias() returned NULL.");
                listCategorias = new ArrayList<>();
            } else {
                System.out.println("HomeServlet: Fetched " + listCategorias.size() + " categorias from ApiService.");
            }

            System.out.println("HomeServlet: Filtering tarefas for status 1 (Não Iniciado) or 2 (Em Andamento)...");
            pendingTarefas = allTarefas.stream()
                    .filter(tarefa -> (tarefa.getStatus() == 1 || tarefa.getStatus() == 2)) // Assuming 1=NI, 2=EA as per last discussion
                    .collect(Collectors.toList());

            System.out.println("HomeServlet: Found " + pendingTarefas.size() + " pending tarefas after filtering.");
            // Optional: Log pending tasks if still debugging
            // if (!pendingTarefas.isEmpty()) {
            //     System.out.println("HomeServlet: Details of pending tarefas:");
            //     for (Tarefa t : pendingTarefas) {
            //          System.out.println("  - Pending Tarefa: ID=" + t.getId_Tarefas() + ", Status=" + t.getStatus() + ", Titulo=" + t.getTitulo());
            //     }
            // }

            request.setAttribute("pendingTarefas", pendingTarefas);
            request.setAttribute("listCategorias", listCategorias);
            System.out.println("HomeServlet: Attributes set. Forwarding to index.jsp...");

        } catch (Exception e) {
            System.err.println("HomeServlet: ERROR in doGet - " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erro ao carregar dados para a página inicial: " + e.getMessage());
            request.setAttribute("pendingTarefas", pendingTarefas);
            request.setAttribute("listCategorias", listCategorias);
        }

        // This path should point to where your index.jsp is located within WEB-INF
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/index.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}