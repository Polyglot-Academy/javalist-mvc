package com.universityProject.service;

import com.universityProject.model.Categoria;
import com.universityProject.model.Tarefa;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ApiService {
    private final String BASE_URL = "https://javalistapi-bahxb3fcbjazdtgv.brazilsouth-01.azurewebsites.net/api";
    private final ObjectMapper objectMapper;

    public ApiService() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private String readStream(InputStream stream) throws IOException {
        if (stream == null) {
            return "No response/error stream details available.";
        }
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }

    // --- Categoria Methods (Assumed to be okay, focus on Tarefa for status issue) ---
    public List<Categoria> getAllCategorias() throws Exception { // ... (existing code) ...
        URL url = new URL(BASE_URL + "/Categorias");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            throw new RuntimeException("Failed to get all categorias. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }
        String jsonResponse = readStream(conn.getInputStream()); // Read response first
        List<Categoria> categorias = objectMapper.readValue(jsonResponse, new TypeReference<List<Categoria>>() {});
        conn.disconnect();
        return categorias;
    }

    public Categoria getCategoriaById(int id) throws Exception { // ... (existing code) ...
        URL url = new URL(BASE_URL + "/Categorias/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                conn.disconnect();
                return null;
            }
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            throw new RuntimeException("Failed to get categoria by ID. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }
        String jsonResponse = readStream(conn.getInputStream()); // Read response first
        Categoria categoria = objectMapper.readValue(jsonResponse, Categoria.class);
        conn.disconnect();
        return categoria;
    }
    // ... (create, update, delete Categoria methods as before with improved error handling) ...
    public void createCategoria(Categoria categoria) throws Exception {
        URL url = new URL(BASE_URL + "/Categorias");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        String jsonInputString = objectMapper.writeValueAsString(categoria);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_CREATED && responseCode != HttpURLConnection.HTTP_OK) {
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            throw new RuntimeException("Failed to create categoria. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }
        conn.disconnect();
    }

    public void updateCategoria(int id, Categoria categoria) throws Exception {
        URL url = new URL(BASE_URL + "/Categorias/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        String jsonInputString = objectMapper.writeValueAsString(categoria);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_OK) {
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            throw new RuntimeException("Failed to update categoria. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }
        conn.disconnect();
    }

    public void deleteCategoria(int id) throws Exception {
        URL url = new URL(BASE_URL + "/Categorias/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_OK) {
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            throw new RuntimeException("Failed to delete categoria. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }
        conn.disconnect();
    }

    // --- Tarefa Methods ---

    public List<Tarefa> getAllTarefas() throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        System.out.println("ApiService DEBUG: Attempting to GET all tarefas from: " + url);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            System.err.println("ApiService DEBUG: Failed to get all tarefas. HTTP error: " + responseCode + " - Details: " + errorDetails);
            throw new RuntimeException("Failed to get all tarefas. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }

        String jsonResponseString = readStream(conn.getInputStream()); // Read the stream into a string first
        System.out.println("ApiService DEBUG: Raw JSON response for getAllTarefas: " + jsonResponseString);

        List<Tarefa> tarefas = objectMapper.readValue(jsonResponseString, new TypeReference<List<Tarefa>>() {});
        conn.disconnect();

        if (tarefas != null) {
            System.out.println("ApiService DEBUG: Deserialized " + tarefas.size() + " tarefas.");
            for (Tarefa t : tarefas) {
                System.out.println("  - Deserialized Tarefa (getAll): ID=" + t.getId_Tarefas() + ", Status=" + t.getStatus() + ", Titulo=" + t.getTitulo());
            }
        } else {
            System.out.println("ApiService DEBUG: Deserialization resulted in a null list of tarefas.");
        }
        return tarefas;
    }

    public Tarefa getTarefaById(int id) throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        System.out.println("ApiService DEBUG: Attempting to GET tarefa by ID from: " + url);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                conn.disconnect();
                System.out.println("ApiService DEBUG: Tarefa with ID " + id + " not found (404).");
                return null;
            }
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            System.err.println("ApiService DEBUG: Failed to get tarefa by ID. HTTP error: " + responseCode + " - Details: " + errorDetails);
            throw new RuntimeException("Failed to get tarefa by ID. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }

        String jsonResponseString = readStream(conn.getInputStream());
        System.out.println("ApiService DEBUG: Raw JSON response for getTarefaById(" + id + "): " + jsonResponseString);

        Tarefa tarefa = objectMapper.readValue(jsonResponseString, Tarefa.class);
        conn.disconnect();

        if (tarefa != null) {
            System.out.println("ApiService DEBUG: Deserialized Tarefa (getById): ID=" + tarefa.getId_Tarefas() + ", Status=" + tarefa.getStatus() + ", Titulo=" + tarefa.getTitulo());
        } else {
            System.out.println("ApiService DEBUG: Deserialization resulted in a null tarefa for ID " + id);
        }
        return tarefa;
    }

    public void createTarefa(Tarefa tarefa) throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        System.out.println("ApiService DEBUG: createTarefa - Tarefa object received by ApiService - Status: [" + tarefa.getStatus() + "]");
        String jsonInputString = objectMapper.writeValueAsString(tarefa);
        System.out.println("ApiService DEBUG: createTarefa - JSON being sent to .NET API: " + jsonInputString);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_CREATED && responseCode != HttpURLConnection.HTTP_OK) {
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            System.err.println("ApiService DEBUG: Failed to create tarefa. HTTP error: " + responseCode + " - Details: " + errorDetails);
            throw new RuntimeException("Failed to create tarefa. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }
        System.out.println("ApiService DEBUG: createTarefa - Successfully created. Response code: " + responseCode);
        conn.disconnect();
    }

    public void updateTarefa(int id, Tarefa tarefa) throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        System.out.println("ApiService DEBUG: updateTarefa - Tarefa object received by ApiService for ID " + id + " - Status: [" + tarefa.getStatus() + "]");
        String jsonInputString = objectMapper.writeValueAsString(tarefa);
        System.out.println("ApiService DEBUG: updateTarefa - JSON being sent to .NET API: " + jsonInputString);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_OK) {
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            System.err.println("ApiService DEBUG: Failed to update tarefa. HTTP error: " + responseCode + " - Details: " + errorDetails);
            throw new RuntimeException("Failed to update tarefa. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }
        System.out.println("ApiService DEBUG: updateTarefa - Successfully updated. Response code: " + responseCode);
        conn.disconnect();
    }

    public void deleteTarefa(int id) throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_OK) {
            String errorDetails = readStream(conn.getErrorStream());
            conn.disconnect();
            System.err.println("ApiService DEBUG: Failed to delete tarefa. HTTP error: " + responseCode + " - Details: " + errorDetails);
            throw new RuntimeException("Failed to delete tarefa. HTTP error: " + responseCode + " " + conn.getResponseMessage() + " - Details: " + errorDetails);
        }
        System.out.println("ApiService DEBUG: deleteTarefa - Successfully deleted. Response code: " + responseCode);
        conn.disconnect();
    }
}