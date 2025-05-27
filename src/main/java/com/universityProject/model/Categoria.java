package com.universityProject.model;

public class Categoria {
    private int id_Categoria;
    private String nome_Categoria;
    private String descricao_Categoria;

    // Constructors (default and parameterized)
    public Categoria() {}

    public Categoria(int id_Categoria, String nome_Categoria, String descricao_Categoria) {
        this.id_Categoria = id_Categoria;
        this.nome_Categoria = nome_Categoria;
        this.descricao_Categoria = descricao_Categoria;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "Categoria{" +
                "id_Categoria=" + id_Categoria +
                ", nome_Categoria='" + nome_Categoria + '\'' +
                ", descricao_Categoria='" + descricao_Categoria + '\'' +
                '}';
    }
}