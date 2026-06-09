package com.template;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FiguraDAO {

    private static final Logger logger = Logger.getLogger(FiguraDAO.class.getName());

    public void inserir(FiguraDTO figura) {
        String sql = "INSERT INTO figuras_historicas (nome, ano_nascimento, nacionalidade, profissao) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, figura.getNome());
            stmt.setInt(2, figura.getAnoNascimento());
            stmt.setString(3, figura.getNacionalidade());
            stmt.setString(4, figura.getProfissao());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao inserir figura", e);
        }
    }

    public List<FiguraDTO> listar() {
        String sql = "SELECT * FROM figuras_historicas";
        List<FiguraDTO> lista = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FiguraDTO figura = new FiguraDTO();
                figura.setId(rs.getInt("id"));
                figura.setNome(rs.getString("nome"));
                figura.setAnoNascimento(rs.getInt("ano_nascimento"));
                figura.setNacionalidade(rs.getString("nacionalidade"));
                figura.setProfissao(rs.getString("profissao"));

                lista.add(figura);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao listar", e);
        }

        return lista;
    }

    public void atualizar(FiguraDTO figura) {
        String sql = "UPDATE figuras_historicas SET nome = ?, ano_nascimento = ?, nacionalidade = ?, profissao = ? WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, figura.getNome());
            stmt.setInt(2, figura.getAnoNascimento());
            stmt.setString(3, figura.getNacionalidade());
            stmt.setString(4, figura.getProfissao());
            stmt.setInt(5, figura.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao atualizar figura", e);
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM figuras_historicas WHERE id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao remover figura", e);
        }
    }
}