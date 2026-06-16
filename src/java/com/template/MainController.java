package com.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MainController {

    @FXML private TextField txtId;
    @FXML private TextField txtNome;
    @FXML private TextField txtAnoNascimento;
    @FXML private TextField txtNacionalidade;
    @FXML private TextField txtProfissao;

    @FXML private Button btnSalvar;
    @FXML private Button btnEditar;
    @FXML private Button btnDeletar;
    @FXML private Button btnLimpar;

    @FXML private TableView<FiguraDTO> tblFiguraHistorica;
    @FXML private TableColumn<FiguraDTO, Integer> colId;
    @FXML private TableColumn<FiguraDTO, String> colNome;
    @FXML private TableColumn<FiguraDTO, Integer> colAno;
    @FXML private TableColumn<FiguraDTO, String> colNacionalidade;
    @FXML private TableColumn<FiguraDTO, String> colProfissao;

    private final FiguraDAO figuraDAO = new FiguraDAO();
    private final ObservableList<FiguraDTO> obsFiguras = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        btnEditar.setDisable(true);
        btnDeletar.setDisable(true);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoNascimento"));
        colNacionalidade.setCellValueFactory(new PropertyValueFactory<>("nacionalidade"));
        colProfissao.setCellValueFactory(new PropertyValueFactory<>("profissao"));

        tblFiguraHistorica.setItems(obsFiguras);
        atualizarTabela();

        tblFiguraHistorica.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                btnEditar.setDisable(false);
                btnDeletar.setDisable(false);

                txtNome.setText(selecionado.getNome());
                txtAnoNascimento.setText(String.valueOf(selecionado.getAnoNascimento()));
                txtNacionalidade.setText(selecionado.getNacionalidade());
                txtProfissao.setText(selecionado.getProfissao());
                txtId.setText(String.valueOf(selecionado.getId()));
            }
        });

        txtAnoNascimento.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtAnoNascimento.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    private void btnSalvar() {
        try {
            String nome = txtNome.getText().trim();
            String nacionalidade = txtNacionalidade.getText().trim();
            String profissao = txtProfissao.getText().trim();

            if (nome.isEmpty() || txtAnoNascimento.getText().trim().isEmpty() || nacionalidade.isEmpty() || profissao.isEmpty()) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Preencha os campos");
                return;
            }

            int ano = Integer.parseInt(txtAnoNascimento.getText().trim());

            FiguraDTO novaFigura = new FiguraDTO();
            novaFigura.setNome(nome);
            novaFigura.setAnoNascimento(ano);
            novaFigura.setNacionalidade(nacionalidade);
            novaFigura.setProfissao(profissao);

            figuraDAO.inserir(novaFigura);

            limparCampos();
            atualizarTabela();
            exibirAlerta(AlertType.INFORMATION, "Sucesso", "Figura salva com sucesso");

        } catch (NumberFormatException e) {
            exibirAlerta(AlertType.ERROR, "Erro", "Insira as informacoes nos campos corretos");
        }
    }

    @FXML
    private void btnEditar() {
        if (txtId.getText().isEmpty()) {
            exibirAlerta(AlertType.WARNING, "Erro", "Selecione uma figura");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());
            String nome = txtNome.getText().trim();
            String nacionalidade = txtNacionalidade.getText().trim();
            String profissao = txtProfissao.getText().trim();

            if (nome.isEmpty() || txtAnoNascimento.getText().trim().isEmpty() || nacionalidade.isEmpty() || profissao.isEmpty()) {
                exibirAlerta(AlertType.WARNING, "Erro", "Campos Vazios");
                return;
            }

            int ano = Integer.parseInt(txtAnoNascimento.getText().trim());

            FiguraDTO figuraEditada = new FiguraDTO(id, nome, ano, nacionalidade, profissao);

            figuraDAO.atualizar(figuraEditada);

            limparCampos();
            atualizarTabela();
            exibirAlerta(AlertType.INFORMATION, "Sucesso", "Figura editada com sucesso");

        } catch (NumberFormatException e) {
            exibirAlerta(AlertType.ERROR, "Erro", "Insira as informacoes nos campos corretos");
        }
    }

    @FXML
    private void btnDeletar() {
        if (txtId.getText().isEmpty()) {
            exibirAlerta(AlertType.WARNING, "Seleção necessária", "Selecione uma figura para excluir");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        figuraDAO.remover(id);

        limparCampos();
        atualizarTabela();
        exibirAlerta(AlertType.INFORMATION, "Sucesso", "Figura deletada com sucesso");
    }

    @FXML
    private void btnLimpar() {
        limparCampos();
    }

    private void atualizarTabela() {
        obsFiguras.clear();
        List<FiguraDTO> listaDoBanco = figuraDAO.listar();
        obsFiguras.addAll(listaDoBanco);
    }

    private void limparCampos() {
        txtId.clear();
        txtNome.clear();
        txtAnoNascimento.clear();
        txtNacionalidade.clear();
        txtProfissao.clear();
        tblFiguraHistorica.getSelectionModel().clearSelection();

        btnEditar.setDisable(true);
        btnDeletar.setDisable(true);

        txtNome.requestFocus();
    }

    private void exibirAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}