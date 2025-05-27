package com.universityProject.service;

import com.universityProject.model.Categoria;
import com.universityProject.model.Tarefa;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
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
        objectMapper.registerModule(new JavaTimeModule()); // For LocalDate support
    }

    // --- Categoria Methods ---

    public List<Categoria> getAllCategorias() throws Exception {
        URL url = new URL(BASE_URL + "/Categorias");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        List<Categoria> categorias = objectMapper.readValue(br, new TypeReference<List<Categoria>>() {});
        conn.disconnect();
        return categorias;
    }

    public Categoria getCategoriaById(int id) throws Exception {
        URL url = new URL(BASE_URL + "/Categorias/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            if (conn.getResponseCode() == 404) return null; // Or throw specific exception
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        Categoria categoria = objectMapper.readValue(br, Categoria.class);
        conn.disconnect();
        return categoria;
    }

    public void createCategoria(Categoria categoria) throws Exception {
        URL url = new URL(BASE_URL + "/Categorias");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        String jsonInputString = objectMapper.writeValueAsString(categoria);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED && conn.getResponseCode() != HttpURLConnection.HTTP_OK) { // API might return 200 or 201
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + " - " + conn.getResponseMessage());
        }
        conn.disconnect();
    }

    public void updateCategoria(int id, Categoria categoria) throws Exception {
        URL url = new URL(BASE_URL + "/Categorias/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");

        String jsonInputString = objectMapper.writeValueAsString(categoria);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT && conn.getResponseCode() != HttpURLConnection.HTTP_OK) { // API might return 204 or 200
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        conn.disconnect();
    }

    public void deleteCategoria(int id) throws Exception {
        URL url = new URL(BASE_URL + "/Categorias/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        if (conn.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT && conn.getResponseCode() != HttpURLConnection.HTTP_OK) { // API might return 204 or 200
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        conn.disconnect();
    }


    // --- Tarefa Methods ---

    public List<Tarefa> getAllTarefas() throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        List<Tarefa> tarefas = objectMapper.readValue(br, new TypeReference<List<Tarefa>>() {});
        conn.disconnect();
        return tarefas;
    }

    public Tarefa getTarefaById(int id) throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            if (conn.getResponseCode() == 404) return null;
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        Tarefa tarefa = objectMapper.readValue(br, Tarefa.class);
        conn.disconnect();
        return tarefa;
    }

    public void createTarefa(Tarefa tarefa) throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        String jsonInputString = objectMapper.writeValueAsString(tarefa);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + " - " + conn.getResponseMessage());
        }
        conn.disconnect();
    }

    public void updateTarefa(int id, Tarefa tarefa) throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");

        String jsonInputString = objectMapper.writeValueAsString(tarefa);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        conn.disconnect();
    }

    public void deleteTarefa(int id) throws Exception {
        URL url = new URL(BASE_URL + "/Tarefas/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        if (conn.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT && conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }
        conn.disconnect();
    }
}