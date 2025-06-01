package com.universityProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // Import this

public class Tarefa {

    @JsonProperty("id")
    private int id_Tarefas;

    private String titulo;
    private String descricao;

    private LocalDate dataCriacao;
    private LocalDate dataConclusao;

    private int status;

    @JsonProperty("categoriaId")
    private Integer categoriaId;

    // Constructors
    public Tarefa() {}

    // Existing Getters and Setters (should match field types)
    public int getId_Tarefas() { return id_Tarefas; }
    public void setId_Tarefas(int id_Tarefas) { this.id_Tarefas = id_Tarefas; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDate getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    // --- New methods for formatted dates ---

    // For display in lists (e.g., dd/MM/yyyy)
    public String getDataCriacaoFormatted_Display() {
        if (this.dataCriacao == null) {
            return "";
        }
        return this.dataCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getDataConclusaoFormatted_Display() {
        if (this.dataConclusao == null) {
            return "";
        }
        return this.dataConclusao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // For HTML input type="date" (needs yyyy-MM-dd) - used in tarefa-form.jsp
    public String getDataCriacaoForForm() {
        if (this.dataCriacao == null) {
            return "";
        }
        return this.dataCriacao.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getDataConclusaoForForm() {
        if (this.dataConclusao == null) {
            return "";
        }
        return this.dataConclusao.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "id_Tarefas=" + id_Tarefas +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataConclusao=" + dataConclusao +
                ", status=" + status +
                ", categoriaId=" + categoriaId +
                '}';
    }
}