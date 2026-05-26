package com.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class MainController {

    // Componentes de Entrada de Texto (Fx:id no SceneBuilder)
    @FXML private TextField txtId;
    @FXML private TextField txtNome;
    @FXML private TextField txtAnoNascimento;
    @FXML private TextField txtNacionalidade;

    // Componentes da Tabela (Fx:id no SceneBuilder)
    @FXML private TableView<FiguraDTO> tblFiguraHistorica;
    @FXML private TableColumn<FiguraDTO, Integer> colId;
    @FXML private TableColumn<FiguraDTO, String> colNome;
    @FXML private TableColumn<FiguraDTO, Integer> colAno;
    @FXML private TableColumn<FiguraDTO, String> colNacionalidade;

    // Instâncias de Controle de Dados
    private final FiguraDAO figuraDAO = new FiguraDAO();
    private final ObservableList<FiguraDTO> obsFiguras = FXCollections.observableArrayList();

    /**
     * Método inicializador do JavaFX. Executa logo após o FXML ser carregado.
     */
    @FXML
    private void initialize() {
        System.out.println("FXML carregado com sucesso!");

        // Vincula as colunas da TableView aos atributos da classe FiguraDTO
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("anoNascimento"));
        colNacionalidade.setCellValueFactory(new PropertyValueFactory<>("nacionalidade"));

        // Associa a lista observável à tabela e carrega os dados do banco
        tblFiguraHistorica.setItems(obsFiguras);
        atualizarTabela();

        // Monitora a seleção de linhas na tabela. Quando o usuário clica em uma linha,
        // os dados são jogados de volta para os campos de texto da tela.
        tblFiguraHistorica.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                txtId.setText(String.valueOf(novo.getId()));
                txtNome.setText(novo.getNome());
                txtAnoNascimento.setText(String.valueOf(novo.getAnoNascimento()));
                txtNacionalidade.setText(novo.getNacionalidade());
            }
        });
    }

    /**
     * Ação do botão Salvar (On Action: #btnSalvar)
     * Insere um novo registro no banco de dados.
     */
    @FXML
    private void btnSalvar() {
        try {
            String nome = txtNome.getText().trim();
            String nacionalidade = txtNacionalidade.getText().trim();

            // Validação de campos vazios
            if (nome.isEmpty() || txtAnoNascimento.getText().trim().isEmpty() || nacionalidade.isEmpty()) {
                exibirAlerta(AlertType.WARNING, "Campos Vazios", "Por favor, preencha todos os campos.");
                return;
            }

            int ano = Integer.parseInt(txtAnoNascimento.getText().trim());

            // Cria o objeto DTO sem ID (o banco gera o ID automaticamente via SERIAL)
            FiguraDTO novaFigura = new FiguraDTO();
            novaFigura.setNome(nome);
            novaFigura.setAnoNascimento(ano);
            novaFigura.setNacionalidade(nacionalidade);

            // Salva no banco de dados
            figuraDAO.inserir(novaFigura);

            // Atualiza a interface
            limparCampos();
            atualizarTabela();
            exibirAlerta(AlertType.INFORMATION, "Sucesso", "Figura histórica salva com sucesso!");

        } catch (NumberFormatException e) {
            exibirAlerta(AlertType.ERROR, "Erro de Formato", "O ano de nascimento deve ser um número válido.");
        }
    }

    /**
     * Ação do botão Editar (On Action: #btnEditar)
     * Atualiza os dados de uma figura histórica selecionada.
     */
    @FXML
    private void btnEditar() {
        // Validação se há um ID carregado no campo de texto
        if (txtId.getText().isEmpty()) {
            exibirAlerta(AlertType.WARNING, "Seleção necessária", "Selecione uma figura na tabela para poder editá-la.");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());
            String nome = txtNome.getText().trim();
            String nacionalidade = txtNacionalidade.getText().trim();

            if (nome.isEmpty() || txtAnoNascimento.getText().trim().isEmpty() || nacionalidade.isEmpty()) {
                exibirAlerta(AlertType.WARNING, "Campos Vazios", "Por favor, preencha todos os campos.");
                return;
            }

            int ano = Integer.parseInt(txtAnoNascimento.getText().trim());

            // Cria o DTO contendo o ID para que o banco saiba qual linha atualizar
            FiguraDTO figuraEditada = new FiguraDTO(id, nome, ano, nacionalidade);

            // Atualiza no banco de dados
            figuraDAO.atualizar(figuraEditada);

            // Atualiza a interface
            limparCampos();
            atualizarTabela();
            exibirAlerta(AlertType.INFORMATION, "Sucesso", "Figura histórica atualizada com sucesso!");

        } catch (NumberFormatException e) {
            exibirAlerta(AlertType.ERROR, "Erro de Formato", "Verifique se o ano é um número válido.");
        }
    }

    /**
     * Ação do botão Deletar (On Action: #btnDeletar)
     * Remove um registro com base no ID selecionado.
     */
    @FXML
    private void btnDeletar() {
        if (txtId.getText().isEmpty()) {
            exibirAlerta(AlertType.WARNING, "Seleção necessária", "Selecione uma figura na tabela para poder excluí-la.");
            return;
        }

        int id = Integer.parseInt(txtId.getText());

        // Remove do banco de dados
        figuraDAO.remover(id);

        // Atualiza a interface
        limparCampos();
        atualizarTabela();
        exibirAlerta(AlertType.INFORMATION, "Sucesso", "Figura removida com sucesso!");
    }

    /**
     * Atualiza os dados da TableView buscando a lista mais recente do banco de dados.
     */
    private void atualizarTabela() {
        obsFiguras.clear();
        List<FiguraDTO> listaDoBanco = figuraDAO.listar();
        obsFiguras.addAll(listaDoBanco);
    }

    /**
     * Limpa os dados de todos os TextFields e remove a seleção visual da tabela.
     */
    private void limparCampos() {
        txtId.clear();
        txtNome.clear();
        txtAnoNascimento.clear();
        txtNacionalidade.clear();
        tblFiguraHistorica.getSelectionModel().clearSelection();
    }

    /**
     * Método utilitário para exibição de caixas de diálogo (Alertas).
     */
    private void exibirAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}