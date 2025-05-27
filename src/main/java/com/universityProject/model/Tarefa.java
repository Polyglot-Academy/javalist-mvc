package com.universityProject.model;

import java.time.LocalDate; // Or java.util.Date if your API/JSON uses that

public class Tarefa {
    private int id_Tarefas;
    private String titulo;
    private String descricao;
    private LocalDate data_criacao;   // Use LocalDate for 'date' types
    private LocalDate data_conclusao; // Use LocalDate for 'date' types
    private String status;
    private int id_Categoria;
    private Categoria categoria; // Optional: to hold the category object

    // Constructors
    public Tarefa() {}

    // Getters and Setters
    public int getId_Tarefas() {
        return id_Tarefas;
    }

    public void setId_Tarefas(int id_Tarefas) {
        this.id_Tarefas = id_Tarefas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(LocalDate data_criacao) {
        this.data_criacao = data_criacao;
    }

    public LocalDate getData_conclusao() {
        return data_conclusao;
    }

    public void setData_conclusao(LocalDate data_conclusao) {
        this.data_conclusao = data_conclusao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId_Categoria() {
        return id_Categoria;
    }

    public void setId_Categoria(int id_Categoria) {
        this.id_Categoria = id_Categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "id_Tarefas=" + id_Tarefas +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", data_criacao=" + data_criacao +
                ", data_conclusao=" + data_conclusao +
                ", status='" + status + '\'' +
                ", id_Categoria=" + id_Categoria +
                '}';
    }
}