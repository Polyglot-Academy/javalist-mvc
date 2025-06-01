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
import java.util.ArrayList;
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
                    System.out.println("TarefaServlet.doPost: Unknown action '" + action + "', redirecting to list.");
                    response.sendRedirect(request.getContextPath() + "/tarefas/list");
                    break;
            }
        } catch (Exception e) {
            System.err.println("TarefaServlet.doPost: Exception caught - " + e.getMessage());
            handleError(request, response, "Error processing Tarefa POST request: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String actionParam = request.getParameter("action");
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();

        System.out.println("\n------------------------------------------");
        System.out.println("TarefaServlet.doGet: START Request");
        System.out.println("  Request URI: " + requestURI);
        System.out.println("  Context Path: " + request.getContextPath());
        System.out.println("  Servlet Path: " + request.getServletPath());
        System.out.println("  PathInfo: " + pathInfo);
        System.out.println("  QueryString: " + queryString);
        System.out.println("  'action' parameter (from query string): " + actionParam);
        System.out.println("  'id' parameter (from query string): " + request.getParameter("id"));

        String resolvedAction = actionParam;

        if (resolvedAction == null) {
            System.out.println("TarefaServlet.doGet: 'action' parameter is null, attempting to infer from pathInfo.");
            if (pathInfo != null) {
                String cleanPathInfo = pathInfo.trim();
                if (cleanPathInfo.startsWith("/")) {
                    cleanPathInfo = cleanPathInfo.substring(1);
                }
                if (cleanPathInfo.endsWith("/")) {
                    cleanPathInfo = cleanPathInfo.substring(0, cleanPathInfo.length() - 1);
                }
                System.out.println("TarefaServlet.doGet: Cleaned PathInfo for action inference: [" + cleanPathInfo + "]");

                // Added "avancarStatus" case
                switch (cleanPathInfo) {
                    case "":
                    case "list":
                        resolvedAction = "list";
                        break;
                    case "new":
                        resolvedAction = "new";
                        break;
                    case "edit":
                        resolvedAction = "edit";
                        break;
                    case "delete":
                        resolvedAction = "delete";
                        break;
                    case "avancarStatus": // New action
                        resolvedAction = "avancarStatus";
                        break;
                    default:
                        System.out.println("TarefaServlet.doGet: Cleaned PathInfo [" + cleanPathInfo + "] not recognized, defaulting to 'list'.");
                        resolvedAction = "list";
                        break;
                }
            } else {
                System.out.println("TarefaServlet.doGet: Both 'action' parameter and pathInfo are null, defaulting to 'list'.");
                resolvedAction = "list";
            }
        }
        System.out.println("TarefaServlet.doGet: Resolved action: [" + resolvedAction + "]");

        try {
            switch (resolvedAction) {
                case "new":
                    System.out.println("TarefaServlet.doGet: Dispatching to showNewTarefaForm...");
                    showNewTarefaForm(request, response);
                    break;
                case "edit":
                    System.out.println("TarefaServlet.doGet: Dispatching to showEditTarefaForm...");
                    showEditTarefaForm(request, response);
                    break;
                case "delete":
                    System.out.println("TarefaServlet.doGet: Dispatching to deleteTarefa...");
                    deleteTarefa(request, response);
                    break;
                case "avancarStatus": // New action dispatch
                    System.out.println("TarefaServlet.doGet: Dispatching to avancarStatusTarefa...");
                    avancarStatusTarefa(request, response);
                    break;
                case "list":
                default:
                    System.out.println("TarefaServlet.doGet: Dispatching to listTarefas...");
                    listTarefas(request, response);
                    break;
            }
        } catch (Exception e) {
            System.err.println("TarefaServlet.doGet: Exception during action dispatch for action [" + resolvedAction + "] - " + e.getMessage());
            handleError(request, response, "Error processing Tarefa GET request: " + e.getMessage(), e);
        }
        System.out.println("TarefaServlet.doGet: END Request");
        System.out.println("------------------------------------------\n");
    }

    // --- New Method to Advance Status ---
    private void avancarStatusTarefa(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("TarefaServlet: Executing avancarStatusTarefa method.");
        String idParam = request.getParameter("id");
        int tarefaId = 0;

        if (idParam == null || idParam.trim().isEmpty()) {
            System.err.println("  avancarStatusTarefa: Tarefa ID not provided.");
            request.setAttribute("errorMessage", "ID da Tarefa não fornecido para avançar o status.");
            // Forward to home or list with error, or simply redirect if preferred
            response.sendRedirect(request.getContextPath() + "/home?error=IDNaoFornecido"); // Redirect to home with error hint
            return;
        }
        try {
            tarefaId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            System.err.println("  avancarStatusTarefa: Invalid Tarefa ID format: " + idParam);
            request.setAttribute("errorMessage", "ID da Tarefa inválido para avançar o status.");
            response.sendRedirect(request.getContextPath() + "/home?error=IDInvalido"); // Redirect to home with error hint
            return;
        }

        System.out.println("  avancarStatusTarefa: Attempting to advance status for Tarefa ID: " + tarefaId);
        Tarefa tarefa = apiService.getTarefaById(tarefaId);

        if (tarefa == null) {
            System.err.println("  avancarStatusTarefa: Tarefa not found for ID: " + tarefaId);
            request.setAttribute("errorMessage", "Tarefa não encontrada para avançar o status (ID: " + tarefaId + ").");
            response.sendRedirect(request.getContextPath() + "/home?error=TarefaNaoEncontrada");
            return;
        }

        int currentStatus = tarefa.getStatus();
        int nextStatus = currentStatus;

        // Assuming your statuses are: 1 (Não Iniciado), 2 (Em Andamento), 3 (Concluído)
        if (currentStatus == 1) { // Não Iniciado -> Em Andamento
            nextStatus = 2;
        } else if (currentStatus == 2) { // Em Andamento -> Concluído
            nextStatus = 3;
        }
        // If status is already 3 (Concluído) or an unknown status, it won't change.

        if (nextStatus != currentStatus) {
            tarefa.setStatus(nextStatus);
            System.out.println("  avancarStatusTarefa: Updating Tarefa ID " + tarefaId + " from status " + currentStatus + " to " + nextStatus);
            apiService.updateTarefa(tarefaId, tarefa); // Use your existing update method
        } else {
            System.out.println("  avancarStatusTarefa: Status for Tarefa ID " + tarefaId + " (" + currentStatus + ") cannot be advanced further or is unknown.");
        }

        // Redirect back to the home page (index page)
        response.sendRedirect(request.getContextPath() + "/");
    }


    // --- Existing private methods (handleError, listTarefas, showNewTarefaForm, etc.) ---
    // Ensure these are correctly defined as in your provided code.
    // For brevity, I'm not repeating all of them here if they are unchanged from your last paste.
    // Make sure your `handleError` and other methods are present.

    private void handleError(HttpServletRequest request, HttpServletResponse response, String userMessage, Exception e) throws ServletException, IOException {
        System.err.println("TarefaServlet handleError: " + userMessage);
        if (e != null) {
            e.printStackTrace();
        }
        try {
            List<Tarefa> listTarefas = apiService.getAllTarefas();
            request.setAttribute("listTarefas", listTarefas != null ? listTarefas : new ArrayList<>());
        } catch (Exception listEx) {
            System.err.println("TarefaServlet handleError: Could not fetch tarefas for error display: " + listEx.getMessage());
            request.setAttribute("listTarefas", new ArrayList<>());
        }
        try {
            List<Categoria> listCategorias = apiService.getAllCategorias();
            request.setAttribute("listCategorias", listCategorias != null ? listCategorias : new ArrayList<>());
        } catch (Exception catEx) {
            System.err.println("TarefaServlet handleError: Could not fetch categorias for error display: " + catEx.getMessage());
            request.setAttribute("listCategorias", new ArrayList<>());
        }
        request.setAttribute("errorMessage", userMessage);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tarefa-list.jsp");
        dispatcher.forward(request, response);
    }


    private void listTarefas(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("TarefaServlet: Executing listTarefas method.");
        List<Tarefa> listTarefas = apiService.getAllTarefas();
        List<Categoria> listCategorias = apiService.getAllCategorias();
        request.setAttribute("listTarefas", listTarefas != null ? listTarefas : new ArrayList<>());
        request.setAttribute("listCategorias", listCategorias != null ? listCategorias : new ArrayList<>());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tarefa-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewTarefaForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("TarefaServlet: Executing showNewTarefaForm method.");
        List<Categoria> listCategorias = apiService.getAllCategorias();
        request.setAttribute("listCategorias", listCategorias != null ? listCategorias : new ArrayList<>());
        request.setAttribute("tarefa", new Tarefa());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tarefa-form.jsp");
        dispatcher.forward(request, response);
    }


    private void showEditTarefaForm(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("TarefaServlet: Executing showEditTarefaForm method.");
        String idParam = request.getParameter("id");
        System.out.println("  showEditTarefaForm: idParam = " + idParam);
        int id = 0;
        if (idParam == null || idParam.trim().isEmpty()) {
            handleError(request, response, "ID da Tarefa não fornecido para edição.", null);
            return;
        }
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            handleError(request, response, "ID da Tarefa inválido para edição: '" + idParam + "'", e);
            return;
        }

        Tarefa existingTarefa = apiService.getTarefaById(id);
        if (existingTarefa == null) {
            handleError(request, response, "Tarefa não encontrada para ID: " + id, null);
            return;
        }
        List<Categoria> listCategorias = apiService.getAllCategorias();
        request.setAttribute("tarefa", existingTarefa);
        request.setAttribute("listCategorias", listCategorias != null ? listCategorias : new ArrayList<>());
        System.out.println("  showEditTarefaForm: Forwarding to tarefa-form.jsp for Tarefa ID: " + id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tarefa-form.jsp");
        dispatcher.forward(request, response);
    }

    private LocalDate parseLocalDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + dateString + " - " + e.getMessage());
            return null;
        }
    }

    private void createTarefa(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("TarefaServlet: Executing createTarefa method.");
        String titulo = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");
        LocalDate dataCriacao = parseLocalDate(request.getParameter("data_criacao"));
        LocalDate dataConclusao = parseLocalDate(request.getParameter("data_conclusao"));

        String statusParam = request.getParameter("status");
        System.out.println("  createTarefa - Received status parameter from form: [" + statusParam + "]");

        int statusInt = 0;
        try {
            statusInt = Integer.parseInt(statusParam);
        } catch (NumberFormatException e) {
            System.err.println("  createTarefa - Invalid status parameter: " + statusParam + ". Error: " + e.getMessage());
            request.setAttribute("errorMessage", "Status inválido.");
            repopulateAndShowFormOnError(request, response, "new", null, titulo, descricao, request.getParameter("data_criacao"), request.getParameter("data_conclusao"), 0 , null);
            return;
        }
        System.out.println("  createTarefa - Parsed statusInt: [" + statusInt + "]");

        Integer categoriaIdInt = null;
        String categoriaIdParam = request.getParameter("id_Categoria");

        if (categoriaIdParam == null || categoriaIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "Categoria é obrigatória.");
            repopulateAndShowFormOnError(request, response, "new", null, titulo, descricao, request.getParameter("data_criacao"), request.getParameter("data_conclusao"), statusInt, null);
            return;
        }
        try {
            categoriaIdInt = Integer.parseInt(categoriaIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID da Categoria inválido: '" + categoriaIdParam + "'");
            repopulateAndShowFormOnError(request, response, "new", null, titulo, descricao, request.getParameter("data_criacao"), request.getParameter("data_conclusao"), statusInt, null);
            return;
        }

        Tarefa newTarefa = new Tarefa();
        newTarefa.setTitulo(titulo);
        newTarefa.setDescricao(descricao);
        newTarefa.setDataCriacao(dataCriacao);
        newTarefa.setDataConclusao(dataConclusao);
        newTarefa.setStatus(statusInt);
        newTarefa.setCategoriaId(categoriaIdInt);

        System.out.println("  createTarefa - Status set on newTarefa object before sending to ApiService: [" + newTarefa.getStatus() + "]");

        apiService.createTarefa(newTarefa);
        response.sendRedirect(request.getContextPath() + "/tarefas/list");
    }

    private void updateTarefa(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("TarefaServlet: Executing updateTarefa method.");
        String idParam = request.getParameter("id_Tarefas");
        int id = 0;
        if (idParam == null || idParam.trim().isEmpty()) {
            handleError(request, response, "ID da Tarefa não fornecido para atualização.", null);
            return;
        }
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            handleError(request, response, "ID da Tarefa inválido para atualização: '" + idParam + "'", e);
            return;
        }

        String titulo = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");
        LocalDate dataCriacao = parseLocalDate(request.getParameter("data_criacao"));
        LocalDate dataConclusao = parseLocalDate(request.getParameter("data_conclusao"));

        String statusParam = request.getParameter("status");
        System.out.println("  updateTarefa - Received status parameter from form: [" + statusParam + "]");
        int statusInt = 0;
        try {
            statusInt = Integer.parseInt(statusParam);
        } catch (NumberFormatException e) {
            System.err.println("  updateTarefa - Invalid status parameter: " + statusParam + ". Error: " + e.getMessage());
            request.setAttribute("errorMessage", "Status inválido.");
            // For repopulate, might need original CategoriaId if parsing fails.
            Integer currentCategoriaId = null;
            try { currentCategoriaId = Integer.parseInt(request.getParameter("id_Categoria")); } catch (Exception ignored) {}
            repopulateAndShowFormOnError(request, response, "edit", id, titulo, descricao, request.getParameter("data_criacao"), request.getParameter("data_conclusao"), 0, currentCategoriaId);
            return;
        }
        System.out.println("  updateTarefa - Parsed statusInt: [" + statusInt + "]");

        Integer categoriaIdInt = null;
        String categoriaIdParam = request.getParameter("id_Categoria");

        if (categoriaIdParam == null || categoriaIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "Categoria é obrigatória.");
            repopulateAndShowFormOnError(request, response, "edit", id, titulo, descricao, request.getParameter("data_criacao"), request.getParameter("data_conclusao"), statusInt, null);
            return;
        }
        try {
            categoriaIdInt = Integer.parseInt(categoriaIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID da Categoria inválido: '" + categoriaIdParam + "'");
            repopulateAndShowFormOnError(request, response, "edit", id, titulo, descricao, request.getParameter("data_criacao"), request.getParameter("data_conclusao"), statusInt, null);
            return;
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setId_Tarefas(id);
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        tarefa.setDataCriacao(dataCriacao);
        tarefa.setDataConclusao(dataConclusao);
        tarefa.setStatus(statusInt);
        tarefa.setCategoriaId(categoriaIdInt);

        System.out.println("  updateTarefa - Status set on Tarefa object before sending to ApiService: [" + tarefa.getStatus() + "]");

        apiService.updateTarefa(id, tarefa);
        response.sendRedirect(request.getContextPath() + "/tarefas/list");
    }

    private void repopulateAndShowFormOnError(HttpServletRequest request, HttpServletResponse response, String mode,
                                              Integer tarefaIdToEdit, String titulo, String descricao,
                                              String dataCriacaoStr, String dataConclusaoStr,
                                              int status, Integer categoriaIdParamValue)
            throws Exception {
        System.out.println("TarefaServlet: Executing repopulateAndShowFormOnError for mode: " + mode);
        Tarefa tarefaToDisplay;
        if ("edit".equals(mode) && tarefaIdToEdit != null) {
            tarefaToDisplay = apiService.getTarefaById(tarefaIdToEdit);
            if (tarefaToDisplay == null) {
                System.out.println("  repopulateAndShowFormOnError: Original tarefa for edit (ID: " + tarefaIdToEdit + ") not found. Creating new Tarefa shell.");
                tarefaToDisplay = new Tarefa();
                tarefaToDisplay.setId_Tarefas(tarefaIdToEdit);
            } else {
                System.out.println("  repopulateAndShowFormOnError: Repopulating existing Tarefa ID: " + tarefaIdToEdit);
            }
        } else {
            System.out.println("  repopulateAndShowFormOnError: Creating new Tarefa shell for 'new' mode.");
            tarefaToDisplay = new Tarefa();
        }

        tarefaToDisplay.setTitulo(titulo);
        tarefaToDisplay.setDescricao(descricao);
        tarefaToDisplay.setStatus(status);
        tarefaToDisplay.setCategoriaId(categoriaIdParamValue);

        tarefaToDisplay.setDataCriacao(parseLocalDate(dataCriacaoStr));
        tarefaToDisplay.setDataConclusao(parseLocalDate(dataConclusaoStr));

        request.setAttribute("tarefa", tarefaToDisplay);
        List<Categoria> listCategorias = apiService.getAllCategorias();
        request.setAttribute("listCategorias", listCategorias != null ? listCategorias : new ArrayList<>());

        System.out.println("  repopulateAndShowFormOnError: Forwarding back to tarefa-form.jsp with error message and repopulated data.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/tarefa-form.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteTarefa(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        System.out.println("TarefaServlet: Executing deleteTarefa method.");
        String idParam = request.getParameter("id");
        int id = 0;
        if (idParam == null || idParam.trim().isEmpty()) {
            handleError(request, response, "ID da Tarefa não fornecido para exclusão.", null);
            return;
        }
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            handleError(request, response, "ID da Tarefa inválido para exclusão: '" + idParam + "'", e);
            return;
        }
        System.out.println("  deleteTarefa: Deleting Tarefa ID: " + id);
        apiService.deleteTarefa(id);
        response.sendRedirect(request.getContextPath() + "/tarefas/list");
    }
}