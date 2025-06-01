package com.universityProject.model; // Or your actual package

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

public class Categoria {

    @JsonProperty("id")
    private int id_Categoria;

    @JsonProperty("nome")
    private String nome_Categoria;

    @JsonProperty("descricao")
    private String descricao_Categoria;

    // This annotation tells Jackson to use this field when reading JSON (deserialization)
    // but to IGNORE it when writing JSON (serialization for POST/PUT).
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Tarefa> tarefas;

    public Categoria() {
        this.tarefas = new ArrayList<>();
    }

    public Categoria(int id_Categoria, String nome_Categoria, String descricao_Categoria) {
        this.id_Categoria = id_Categoria;
        this.nome_Categoria = nome_Categoria;
        this.descricao_Categoria = descricao_Categoria;
        this.tarefas = new ArrayList<>();
    }

    // Getters and Setters for all fields (id_Categoria, nome_Categoria, descricao_Categoria, tarefas)
    public int getId_Categoria() {
        return id_Categoria;
    }

    public void setId_Categoria(int id_Categoria) {
        this.id_Categoria = id_Categoria;
    }

    public String getNome_Categoria() {
        return nome_Categoria;
    }

    public void setNome_Categoria(String nome_Categoria) {
        this.nome_Categoria = nome_Categoria;
    }

    public String getDescricao_Categoria() {
        return descricao_Categoria;
    }

    public void setDescricao_Categoria(String descricao_Categoria) {
        this.descricao_Categoria = descricao_Categoria;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id_Categoria=" + id_Categoria +
                ", nome_Categoria='" + nome_Categoria + '\'' +
                ", descricao_Categoria='" + descricao_Categoria + '\'' +
                // Avoid printing all tasks in toString if list can be large
                ", tarefasCount=" + (tarefas != null ? tarefas.size() : "null") +
                '}';
    }
}