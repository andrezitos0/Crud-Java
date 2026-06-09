package com.template;

public class FiguraDTO {
    
    private int id;
    private String nome;
    private int anoNascimento;
    private String nacionalidade;
    private String profissao;
    
    public FiguraDTO() {}
    
    public FiguraDTO(int id, String nome, int anoNascimento, String nacionalidade, String profissao) {
        this.id = id;
        this.nome = nome;
        this.anoNascimento = anoNascimento;
        this.nacionalidade = nacionalidade;
        this.profissao = profissao;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public int getAnoNascimento() { return anoNascimento; }
    public void setAnoNascimento(int anoNascimento) { this.anoNascimento = anoNascimento; }
    
    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }
    
    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
}
