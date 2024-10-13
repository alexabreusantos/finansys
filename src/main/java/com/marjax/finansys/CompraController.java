/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.dao.CompraDAO;
import com.marjax.finansys.dao.FaturaDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Categoria;
import com.marjax.finansys.model.Compra;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.model.Responsavel;
import com.marjax.finansys.util.AlertUtil;
import com.marjax.finansys.util.LocaleUtil;
import com.marjax.finansys.util.PreencherComboBox;
import com.marjax.finansys.util.SituacaoTableCellCompra;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class CompraController implements Initializable {

    @FXML
    private TextField pesquisarTextField;
    @FXML
    private Label totalCadastroLabel;
    @FXML
    private Label totalGeralLabel;
    @FXML
    private Label totalPessoaLabel;
    @FXML
    private Label totalCartaoLabel;
    @FXML
    private Label totalCategoriaLabel;
    @FXML
    private Label totalSituacaoLabel;
    @FXML
    private Label primeiroFiltroLabel;
    @FXML
    private Label segundoFiltroLabel;
    @FXML
    private Label totalFiltrosLabel;
    @FXML
    private Label escolhaPessoaLabel;
    @FXML
    private Label valorTotalFaturaLabel;
    @FXML
    private Label totalFaturaCartaoLabel;
    @FXML
    private Label valorFaturaCartaoLabel;
    @FXML
    private Label totalFaturaLabel;
    @FXML
    private Label totalAlimentacaoCartaoLabel;
    @FXML
    private Label primeiroFiltroLabelSituacao;
    @FXML
    private Label segundoFiltroLabelSituacao;
    @FXML
    private Label terceiroFiltroLabelSituacao;
    @FXML
    private Label totalFiltrosLabelSituacao;

    @FXML
    private ComboBox<Responsavel> totalPessoaComboBox;
    @FXML
    private ComboBox<Cartao> totalCartaoComboBox;
    @FXML
    private ComboBox<Categoria> totalCategoriaComboBox;
    @FXML
    private ComboBox<String> totalSituacaoComboBox;
    @FXML
    private ComboBox<String> filtroComboBox;
    @FXML
    private ComboBox<String> primeiroFiltroComboBox;
    @FXML
    private ComboBox<String> segundoFiltroComboBox;
    @FXML
    private ComboBox<String> quantidadePessoasComboBox;
    @FXML
    private ComboBox<String> alimentacaoPessoasComboBox;
    @FXML
    private ComboBox<String> totalFaturaCartaoComboBox;
    @FXML
    private ComboBox<String> filtroComboBoxSituacao;
    @FXML
    private ComboBox<String> primeiroFiltroComboBoxSituacao;
    @FXML
    private ComboBox<String> segundoFiltroComboBoxSituacao;
    @FXML
    private ComboBox<String> terceiroFiltroComboBoxSituacao;
    @FXML
    private ComboBox<String> mesFaturaComboBox;
    @FXML
    private ComboBox<Integer> anoFaturaComboBox;

    @FXML
    private FontAwesomeIcon iconePrimeiro;
    @FXML
    private FontAwesomeIcon iconeSegundo;
    @FXML
    private FontAwesomeIcon iconePrimeiroSituacao;
    @FXML
    private FontAwesomeIcon iconeSegundoSituacao;
    @FXML
    private AnchorPane alimentacaoTotalPane;

    @FXML
    private TableView<Compra> compraTableView;
    @FXML
    private TableView<String> alimentacaoTableView;

    @FXML
    private TableColumn<String, String> responsavelAlimentacaoColuna;
    @FXML
    private TableColumn<Compra, Integer> codigoColuna;
    @FXML
    private TableColumn<Compra, String> descricaoColuna;
    @FXML
    private TableColumn<Compra, String> responsavelColuna;
    @FXML
    private TableColumn<Compra, String> cartaoColuna;
    @FXML
    private TableColumn<Compra, String> categoriaColuna;
    @FXML
    private TableColumn<Compra, Double> valorColuna;
    @FXML
    private TableColumn<Compra, String> parcelaColuna;
    @FXML
    private TableColumn<Compra, String> dataColuna;
    @FXML
    private TableColumn<Compra, Double> totalColuna;
    @FXML
    private TableColumn<Compra, String> situacaoColuna;

    @FXML
    private Button colocarButton;
    @FXML
    private Button removerButton;
    @FXML
    private Button adicionarButton;
    @FXML
    private Button excluirButton;

    private CompraDAO dao;
    private CartaoDAO cartaoDAO;
    private Cartao cartao;
    private FaturaDAO faturaDAO;

    private ObservableList<Compra> compras = FXCollections.observableArrayList();
    //private ObservableList<String> responsavelList = FXCollections.observableArrayList();
    private final ObservableList<String> alimentacaoList = FXCollections.observableArrayList();

    private final String css = "/com/marjax/finansys/style/main.css";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        novaInstanciaDAO();
        carregaComboBox();
        clickComboBox();
        //carregarComprasPorPeriodo();
        parametroPesquisaCompra();
        //verificarCompraParcelada();
        carregarComprasPorPeriodo();
        atualizarSomaTotal();
        configurarTabela();
        labelVisible();
        listinerComboBox();

        YearMonth proximoMes = YearMonth.now().plusMonths(1);
        java.sql.Date periodo = Date.valueOf(proximoMes.atDay(1));

        boolean faturaExiste = faturaDAO.verificarFaturaExistente(7, periodo);

        if (faturaExiste) {
                      

        } else {
            
        }

        responsavelAlimentacaoColuna.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()));

        alimentacaoTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            removerButton.setDisable(newValue == null);
        });

        alimentacaoTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 || event.getClickCount() == 2) { // Verifica se é um clique único
                TablePosition<String, ?> pos = alimentacaoTableView.getSelectionModel().getSelectedCells().get(0);
                enableLabelAlimentacao();

            } else {
                disableLabelAlimentacao();
            }
        });

        alimentacaoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                atualizarValorTotalFaturaLabel(newSelection);
                totalAlimentacaoCartaoLabel.setVisible(false);
                valorFaturaCartaoLabel.setVisible(false);
                PreencherComboBox.comboBoxCartaoString(totalFaturaCartaoComboBox);
            }
        });

        adicionarButton.setOnAction(event -> abrirCadastrar());
    }

    private void atualizarValorTotalFaturaLabel(String responsavelSelecionado) {
        double totalAlimentacao = compraTableView.getItems().stream()
                .filter(compra -> "Alimentação".equals(compra.getCategoria().getNome())) // Filtra pela categoria "Alimentação"
                .mapToDouble(Compra::getValor)
                .sum();

        int quantidade = Integer.parseInt(quantidadePessoasComboBox.getValue());
        // Calcula a metade do total da categoria Alimentação
        double alimentacaoValor = totalAlimentacao / quantidade;

        totalFaturaLabel.setText("Total Fatura de " + responsavelSelecionado + " dividindo a Alimentação com " + quantidade + " pessoa(s):");

        double totalResponsavel = compraTableView.getItems().stream()
                .filter(compra -> compra.getResponsavel().getNome().equals(responsavelSelecionado)) // Filtra as compras pelo responsável
                .mapToDouble(Compra::getValor)
                .sum();

        double total = alimentacaoValor + totalResponsavel;
        // Atualiza o valorTotalResponsavelLabel com o total calculado
        LocaleUtil.moedaBrasilLabel(total, valorTotalFaturaLabel);
        //totalFaturaLabel.setText(String.format("Total Responsável: R$ %.2f", total));
    }

    private void atualizarValorFaturaPorCartao(String cartaoSelecionado) {
        String pessoa = alimentacaoTableView.getSelectionModel().getSelectedItem();
        int quantidade = Integer.parseInt(quantidadePessoasComboBox.getValue());

        double totalPessoa = compraTableView.getItems().stream()
                .filter(compra -> compra.getResponsavel().getNome().equals(pessoa)
                && compra.getFatura().getCartao().getNome().equals(cartaoSelecionado))
                .mapToDouble(Compra::getValor)
                .sum();

        // Calcula o total das compras da categoria Alimentação para o cartão selecionado
        double totalAlimentacaoPorCartao = compraTableView.getItems().stream()
                .filter(compra -> "Alimentação".equals(compra.getCategoria().getNome())) // Filtra pela categoria "Alimentação"
                .filter(compra -> compra.getFatura().getCartao().getNome().equals(cartaoSelecionado))
                // Filtra pelo cartão selecionado
                .mapToDouble(Compra::getValor)
                .sum();

        double valorFinal = totalAlimentacaoPorCartao / quantidade + (totalPessoa);
        valorFaturaCartaoLabel.setVisible(true);
        totalAlimentacaoCartaoLabel.setVisible(true);
        // Atualiza o valorFaturaCartaoLabel com o total calculado
        LocaleUtil.moedaBrasilLabel(valorFinal, valorFaturaCartaoLabel);
    }

    // Método para alterar o ícone
    private void mudarIcone(FontAwesomeIcon icon, String novoIcone) {
        icon.setGlyphName(novoIcone);
    }

    private void removerResponsavel() {
        String responsavelSelecionado = alimentacaoTableView.getSelectionModel().getSelectedItem();

        if (responsavelSelecionado != null) {
            // Remove o responsável da TableView
            alimentacaoList.remove(responsavelSelecionado);

            int quantidade = Integer.parseInt(quantidadePessoasComboBox.getValue());
            int rowCount = alimentacaoTableView.getItems().size();

            if (rowCount == 0) {
                alimentacaoTableView.setDisable(true);
                disableLabelAlimentacao();
                totalAlimentacaoCartaoLabel.setDisable(true);
                valorFaturaCartaoLabel.setDisable(true);
            }

            if (rowCount >= quantidade) {
                colocarButton.setDisable(true);
            } else {
                colocarButton.setDisable(false);
            }
        }
    }

    private void colocarResponsavel() {
        String responsavelSelecionado = alimentacaoPessoasComboBox.getSelectionModel().getSelectedItem();

        if (responsavelSelecionado != null) {
            if (!alimentacaoList.contains(responsavelSelecionado)) {
                // Adiciona o responsável à TableView
                alimentacaoList.add(responsavelSelecionado);
                alimentacaoTableView.setDisable(false);
                alimentacaoTableView.setItems(alimentacaoList);
            } else {
                AlertUtil.showWarningAlert("Atenção!", "Pessoa já foi escolhida", "Escolha outra pessoa para dividir a Alimentação.");
            }
        }

        int quantidade = Integer.parseInt(quantidadePessoasComboBox.getValue());
        int rowCount = alimentacaoTableView.getItems().size();

        if (rowCount >= quantidade) {
            colocarButton.setDisable(true);
        } else {
            colocarButton.setDisable(false);
        }
    }

    private void ativarBotaoMais() {
        String alimentacaoPessoasSelecionada = alimentacaoPessoasComboBox.getSelectionModel().getSelectedItem();

        // Desativar o botão se "alimentacaoPessoasComboBox" tiver a opção "Teste" selecionada ou estiver desativado
        if ("Selecione".equals(alimentacaoPessoasSelecionada) || alimentacaoPessoasSelecionada == null) {
            colocarButton.setDisable(true);
        } else {
            colocarButton.setDisable(false);
        }
    }

    private void exibirComboBoxAlimentacaoPessoa() {
        String quantidadePessoas = quantidadePessoasComboBox.getSelectionModel().getSelectedItem();
        if (!quantidadePessoas.equals("Selecione")) {
            escolhaPessoaLabel.setDisable(false);
            alimentacaoPessoasComboBox.setDisable(false);
        } else {
            escolhaPessoaLabel.setDisable(true);
            alimentacaoPessoasComboBox.setDisable(true);
            PreencherComboBox.comboBoxPessoas(alimentacaoPessoasComboBox);
        }
    }

    private void filtrarECalcularTotal() {
        String primeiroFiltro = primeiroFiltroComboBox.getSelectionModel().getSelectedItem();
        String segundoFiltro = segundoFiltroComboBox.getSelectionModel().getSelectedItem();
        String filtro = filtroComboBox.getSelectionModel().getSelectedItem();

        switch (filtro) {
            case "Pessoa e Cartão":
                if (primeiroFiltro != null && segundoFiltro != null) {
                    // Filtrar a tabela com base nos critérios selecionados
                    double total = compraTableView.getItems().stream()
                            .filter(compra -> compra.getResponsavel().getNome().equals(primeiroFiltro)
                            && compra.getFatura().getCartao().getNome().equals(segundoFiltro))
                            .mapToDouble(Compra::getValor)
                            .sum();

                    // Se o total for maior que 0, exibe o label e mostra o valor total
                    if (total > 0) {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabel);
                        totalFiltrosLabel.setVisible(true);
                    } else {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabel);
                        totalFiltrosLabel.setVisible(true);
                    }
                } else {
                    // Se um dos filtros não estiver selecionado, esconde o Label
                    totalFiltrosLabel.setVisible(false);
                }
                break;
            case "Pessoa e Categoria":
                if (primeiroFiltro != null && segundoFiltro != null) {
                    // Filtrar a tabela com base nos critérios selecionados
                    double total = compraTableView.getItems().stream()
                            .filter(compra -> compra.getResponsavel().getNome().equals(primeiroFiltro)
                            && compra.getCategoria().getNome().equals(segundoFiltro))
                            .mapToDouble(Compra::getValor)
                            .sum();

                    // Se o total for maior que 0, exibe o label e mostra o valor total
                    if (total > 0) {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabel);
                        totalFiltrosLabel.setVisible(true);
                    } else {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabel);
                        totalFiltrosLabel.setVisible(true);
                    }
                } else {
                    // Se um dos filtros não estiver selecionado, esconde o Label
                    totalFiltrosLabel.setVisible(false);
                }
                break;
            case "Cartão e Categoria":
                if (primeiroFiltro != null && segundoFiltro != null) {
                    // Filtrar a tabela com base nos critérios selecionados
                    double total = compraTableView.getItems().stream()
                            .filter(compra -> compra.getFatura().getCartao().getNome().equals(primeiroFiltro)
                            && compra.getCategoria().getNome().equals(segundoFiltro))
                            .mapToDouble(Compra::getValor)
                            .sum();

                    // Se o total for maior que 0, exibe o label e mostra o valor total
                    if (total > 0) {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabel);
                        totalFiltrosLabel.setVisible(true);
                    } else {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabel);
                        totalFiltrosLabel.setVisible(true);
                    }
                } else {
                    // Se um dos filtros não estiver selecionado, esconde o Label
                    totalFiltrosLabel.setVisible(false);
                }
                break;
            default:
                throw new AssertionError();
        }

    }

    private void listinerComboBox() {

        // Adicione um listener para atualizar a soma total quando os dados mudarem
        compras.addListener((ListChangeListener<Compra>) c -> atualizarSomaTotal());

        // Adiciona um listener para o ComboBox Pessoa
        totalPessoaComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            calcularTotalPorResponsavel(newValue);
        });

        // Adiciona um listener ao ComboBox Cartao para detectar alterações
        totalCartaoComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            calcularTotalPorCartao(newValue);
        });

        // Adiciona um listener ao ComboBox Categoria para detectar alterações
        totalCategoriaComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            calcularTotalPorCategoria(newValue);
        });

        // Adiciona um listener ao ComboBox Categoria para detectar alterações
        totalSituacaoComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            calcularTotalPorSituacao(newValue);
        });

        // Adiciona o listener de valor ao primeiroFiltroComboBox
        primeiroFiltroComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Lógica para mostrar/ocultar o segundo ComboBox com base na seleção
            String filtroSelecionado = filtroComboBox.getValue();
            if (filtroSelecionado != null) {
                switch (filtroSelecionado) {
                    case "Pessoa e Cartão":
                        if ("Selecione".equals(newValue)) {
                            segundoFiltroComboBox.setDisable(true);
                            segundoFiltroLabel.setVisible(false);
                            totalFiltrosLabel.setVisible(false);
                        } else {
                            segundoFiltroComboBox.setDisable(false);
                            segundoFiltroLabel.setVisible(true);
                            totalFiltrosLabel.setVisible(true);
                            segundoFiltroLabel.setText("Cartão");
                            mudarIcone(iconeSegundo, "CREDIT_CARD");
                            PreencherComboBox.comboBoxCartaoString(segundoFiltroComboBox);
                        }
                        break;
                    case "Pessoa e Categoria":
                        if ("Selecione".equals(newValue)) {
                            segundoFiltroComboBox.setDisable(true);
                            segundoFiltroLabel.setVisible(false);
                            totalFiltrosLabel.setVisible(false);
                        } else {
                            segundoFiltroComboBox.setDisable(false);
                            segundoFiltroLabel.setVisible(true);
                            totalFiltrosLabel.setVisible(true);
                            segundoFiltroLabel.setText("Categoria");
                            mudarIcone(iconeSegundo, "TH_LIST");
                            PreencherComboBox.comboBoxCategoriaString(segundoFiltroComboBox);
                        }
                        break;
                    case "Cartão e Categoria":
                        if ("Selecione".equals(newValue)) {
                            segundoFiltroComboBox.setDisable(true);
                            segundoFiltroLabel.setVisible(false);
                            totalFiltrosLabel.setVisible(false);
                        } else {
                            segundoFiltroComboBox.setDisable(false);
                            segundoFiltroLabel.setVisible(true);
                            totalFiltrosLabel.setVisible(true);
                            segundoFiltroLabel.setText("Categoria");
                            mudarIcone(iconeSegundo, "TH_LIST");
                            PreencherComboBox.comboBoxCategoriaString(segundoFiltroComboBox);
                        }
                        break;
                    default:
                        throw new AssertionError("Valor inesperado: " + filtroSelecionado);
                }
            }
        });

        // Adiciona um listener ao filtroComboBox para gerenciar a visibilidade dos filtros
        filtroComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case "Selecione":
                        primeiroFiltroComboBox.setDisable(true);
                        segundoFiltroComboBox.setDisable(true);
                        primeiroFiltroLabel.setVisible(false);
                        segundoFiltroLabel.setVisible(false);
                        totalFiltrosLabel.setVisible(false);
                        break;
                    case "Pessoa e Cartão":
                        primeiroFiltroComboBox.setDisable(false);
                        PreencherComboBox.comboBoxPessoas(primeiroFiltroComboBox);
                        primeiroFiltroLabel.setVisible(true);
                        primeiroFiltroLabel.setText("Pessoa");
                        mudarIcone(iconePrimeiro, "USER");

                        // Inicialmente oculta o segundo ComboBox e o Label
                        segundoFiltroComboBox.setDisable(true);
                        segundoFiltroLabel.setVisible(false);
                        totalFiltrosLabel.setVisible(false);
                        break;
                    case "Pessoa e Categoria":
                        primeiroFiltroComboBox.setDisable(false);
                        PreencherComboBox.comboBoxPessoas(primeiroFiltroComboBox);
                        primeiroFiltroLabel.setVisible(true);
                        primeiroFiltroLabel.setText("Pessoa");
                        mudarIcone(iconePrimeiro, "USER");

                        // Inicialmente oculta o segundo ComboBox e o Label
                        segundoFiltroComboBox.setDisable(true);
                        segundoFiltroLabel.setVisible(false);
                        totalFiltrosLabel.setVisible(false);
                        break;
                    case "Cartão e Categoria":
                        primeiroFiltroComboBox.setDisable(false);
                        PreencherComboBox.comboBoxCartaoString(primeiroFiltroComboBox);
                        primeiroFiltroLabel.setVisible(true);
                        primeiroFiltroLabel.setText("Cartão");
                        mudarIcone(iconePrimeiro, "CREDIT_CARD");

                        // Inicialmente oculta o segundo ComboBox e o Label
                        segundoFiltroComboBox.setDisable(true);
                        segundoFiltroLabel.setVisible(false);
                        totalFiltrosLabel.setVisible(false);
                        break;
                    default:
                        throw new AssertionError("Valor inesperado: " + newValue);
                }
            }
        });

        filtroComboBoxSituacao.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case "Selecione":
                        primeiroFiltroComboBoxSituacao.setDisable(true);
                        segundoFiltroComboBoxSituacao.setDisable(true);
                        terceiroFiltroComboBoxSituacao.setDisable(true);
                        primeiroFiltroLabelSituacao.setVisible(false);
                        segundoFiltroLabelSituacao.setVisible(false);
                        terceiroFiltroLabelSituacao.setVisible(false);
                        totalFiltrosLabelSituacao.setVisible(false);
                        break;
                    case "Pessoa e Situação":
                        primeiroFiltroComboBoxSituacao.setDisable(false);
                        PreencherComboBox.comboBoxPessoaSituacao(primeiroFiltroComboBoxSituacao);
                        primeiroFiltroLabelSituacao.setVisible(true);
                        primeiroFiltroLabelSituacao.setText("Pessoa");
                        mudarIcone(iconePrimeiroSituacao, "USER");

                        // Inicialmente oculta o segundo ComboBox e o Label
                        segundoFiltroComboBoxSituacao.setDisable(true);
                        segundoFiltroLabelSituacao.setVisible(false);
                        terceiroFiltroComboBoxSituacao.setDisable(true);
                        terceiroFiltroLabelSituacao.setVisible(false);
                        totalFiltrosLabelSituacao.setVisible(false);
                        break;
                    case "Cartão e Situação":
                        primeiroFiltroComboBoxSituacao.setDisable(false);
                        PreencherComboBox.comboBoxCartaoString(primeiroFiltroComboBoxSituacao);
                        primeiroFiltroLabelSituacao.setVisible(true);
                        primeiroFiltroLabelSituacao.setText("Cartão");
                        mudarIcone(iconePrimeiroSituacao, "CREDIT_CARD");

                        // Inicialmente oculta o segundo ComboBox e o Label
                        segundoFiltroComboBoxSituacao.setDisable(true);
                        segundoFiltroLabelSituacao.setVisible(false);
                        terceiroFiltroComboBoxSituacao.setDisable(true);
                        terceiroFiltroLabelSituacao.setVisible(false);
                        totalFiltrosLabelSituacao.setVisible(false);
                        break;
                    case "Pessoa, Cartão e Situação":
                        primeiroFiltroComboBoxSituacao.setDisable(false);
                        PreencherComboBox.comboBoxPessoaSituacao(primeiroFiltroComboBoxSituacao);
                        primeiroFiltroLabelSituacao.setVisible(true);
                        primeiroFiltroLabelSituacao.setText("Pessoa");
                        mudarIcone(iconePrimeiroSituacao, "USER");

                        // Inicialmente oculta o segundo ComboBox e o Label
                        segundoFiltroComboBoxSituacao.setDisable(true);
                        segundoFiltroLabelSituacao.setVisible(false);
                        terceiroFiltroComboBoxSituacao.setDisable(true);
                        terceiroFiltroLabelSituacao.setVisible(false);
                        totalFiltrosLabelSituacao.setVisible(false);
                        break;
                    default:
                        throw new AssertionError("Valor inesperado: " + newValue);
                }
            }
        });

        primeiroFiltroComboBoxSituacao.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Lógica para mostrar/ocultar o segundo ComboBox com base na seleção
            String filtroSelecionado = filtroComboBoxSituacao.getValue();
            if (filtroSelecionado != null) {
                switch (filtroSelecionado) {
                    case "Pessoa e Situação":
                        if ("Selecione".equals(newValue)) {
                            segundoFiltroComboBoxSituacao.setDisable(true);
                            segundoFiltroLabelSituacao.setVisible(false);
                            terceiroFiltroComboBoxSituacao.setDisable(true);
                            terceiroFiltroLabelSituacao.setVisible(false);
                            totalFiltrosLabelSituacao.setVisible(false);
                        } else {
                            segundoFiltroComboBoxSituacao.setDisable(false);
                            segundoFiltroLabelSituacao.setVisible(true);
                            segundoFiltroLabelSituacao.setText("Situação");
                            mudarIcone(iconeSegundoSituacao, "CHECK_SQUARE");
                            PreencherComboBox.comboBoxSituacao(segundoFiltroComboBoxSituacao);
                        }
                        break;
                    case "Cartão e Situação":
                        if ("Selecione".equals(newValue)) {
                            segundoFiltroComboBoxSituacao.setDisable(true);
                            segundoFiltroLabelSituacao.setVisible(false);
                            terceiroFiltroComboBoxSituacao.setDisable(true);
                            terceiroFiltroLabelSituacao.setVisible(false);
                            totalFiltrosLabelSituacao.setVisible(false);
                        } else {
                            segundoFiltroComboBoxSituacao.setDisable(false);
                            segundoFiltroLabelSituacao.setVisible(true);
                            segundoFiltroLabelSituacao.setText("Situação");
                            mudarIcone(iconeSegundoSituacao, "CHECK_SQUARE");
                            PreencherComboBox.comboBoxSituacao(segundoFiltroComboBoxSituacao);
                        }
                        break;
                    case "Pessoa, Cartão e Situação":
                        if ("Selecione".equals(newValue)) {
                            segundoFiltroComboBoxSituacao.setDisable(true);
                            segundoFiltroLabelSituacao.setVisible(false);
                            terceiroFiltroComboBoxSituacao.setDisable(true);
                            terceiroFiltroLabelSituacao.setVisible(false);
                            totalFiltrosLabelSituacao.setVisible(false);
                        } else {
                            segundoFiltroComboBoxSituacao.setDisable(false);
                            segundoFiltroLabelSituacao.setVisible(true);
                            segundoFiltroLabelSituacao.setText("Cartão");
                            mudarIcone(iconeSegundo, "CREDIT_CARD");
                            PreencherComboBox.comboBoxCartaoString(segundoFiltroComboBoxSituacao);
                        }
                        break;
                    default:
                        throw new AssertionError("Valor inesperado: " + filtroSelecionado);
                }
            }
        });

        segundoFiltroComboBoxSituacao.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Lógica para mostrar/ocultar o segundo ComboBox com base na seleção
            String filtroSelecionado = filtroComboBoxSituacao.getValue();
            if (filtroSelecionado != null) {
                switch (filtroSelecionado) {
                    case "Pessoa e Situação":
                        if ("Selecione".equals(newValue)) {
                            totalFiltrosLabelSituacao.setVisible(false);
                        } else {
                            totalFiltrosLabelSituacao.setVisible(true);
                            filtroCalculoSituacao();
                        }
                        break;
                    case "Cartão e Situação":
                        if ("Selecione".equals(newValue)) {
                            totalFiltrosLabelSituacao.setVisible(false);
                        } else {
                            totalFiltrosLabelSituacao.setVisible(true);
                        }
                        break;
                    case "Pessoa, Cartão e Situação":
                        if ("Selecione".equals(newValue)) {
                            terceiroFiltroComboBoxSituacao.setDisable(true);
                            terceiroFiltroLabelSituacao.setVisible(false);
                            terceiroFiltroLabelSituacao.setText("Situação");
                            totalFiltrosLabelSituacao.setVisible(false);
                        } else {
                            terceiroFiltroComboBoxSituacao.setDisable(false);
                            terceiroFiltroLabelSituacao.setVisible(true);
                            terceiroFiltroLabelSituacao.setText("Situação");
                            totalFiltrosLabelSituacao.setVisible(false);
                            mudarIcone(iconeSegundo, "CHECK_SQUARE");
                            PreencherComboBox.comboBoxSituacao(terceiroFiltroComboBoxSituacao);
                        }
                        break;
                    default:
                        throw new AssertionError("Valor inesperado: " + filtroSelecionado);
                }
            }
        });

        terceiroFiltroComboBoxSituacao.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Lógica para mostrar/ocultar o segundo ComboBox com base na seleção
            String filtroSelecionado = filtroComboBoxSituacao.getValue();
            if (filtroSelecionado != null) {
                switch (filtroSelecionado) {
                    case "Pessoa, Cartão e Situação":
                        if ("Selecione".equals(newValue)) {
                            totalFiltrosLabelSituacao.setVisible(false);
                        } else {
                            totalFiltrosLabelSituacao.setVisible(true);
                        }
                        break;
                    default:
                        throw new AssertionError("Valor inesperado: " + filtroSelecionado);
                }
            }
        });

        totalFaturaCartaoComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                newValue = totalFaturaCartaoComboBox.getSelectionModel().getSelectedItem();
                atualizarValorFaturaPorCartao(newValue);
            }
        });

        alimentacaoPessoasComboBox.valueProperty().addListener((obs, oldValue, newValue) -> ativarBotaoMais());
    }

    private void calcularTotalPorResponsavel(Responsavel responsavelSelecionado) {
        if (responsavelSelecionado == null || "Selecione".equals(responsavelSelecionado.getNome())) {
            totalPessoaLabel.setVisible(false); // Oculta o Label
            return;
        }

        double total = compraTableView.getItems().stream()
                .filter(compra -> {
                    boolean match = compra.getResponsavel().equals(responsavelSelecionado);
                    return match;
                }).mapToDouble(Compra::getValor).sum();

        LocaleUtil.moedaBrasilLabel(total, totalPessoaLabel);
    }

    private void calcularTotalPorCategoria(Categoria categoria) {
        if (categoria == null || "Selecione".equals(categoria.getNome())) {
            totalCategoriaLabel.setVisible(false); // Oculta o Label
            return;
        }

        double total = compraTableView.getItems().stream()
                .filter(compra -> {
                    boolean match = compra.getCategoria().equals(categoria);
                    return match;
                }).mapToDouble(Compra::getValor).sum();
        LocaleUtil.moedaBrasilLabel(total, totalCategoriaLabel);
    }

    private void calcularTotalPorCartao(Cartao cartao) {
        if (cartao == null || "Selecione".equals(cartao.getNome())) {
            totalCartaoLabel.setVisible(false); // Oculta o Label
            return;
        }

        double total = 0.0;

        // Percorre todos os itens da TableView
        for (Compra compra : compraTableView.getItems()) {
            // Obtém o objeto Fatura associado à compra
            Fatura fatura = compra.getFatura();
            if (fatura != null && fatura.getCartao().getNome().equals(cartao.getNome())) {
                // Adiciona o valor da compra ao total se o nome do cartão coincidir
                total += compra.getValor();
            }
        }
        // Define o total calculado no Label
        LocaleUtil.moedaBrasilLabel(total, totalCartaoLabel);
    }

    private void calcularTotalPorSituacao(String situacao) {
        if (situacao == null || "Selecione".equals(situacao)) {
            totalSituacaoLabel.setVisible(false); // Oculta o Label se "Selecione" ou null for escolhido
            return;
        }

        totalSituacaoLabel.setVisible(true); // Mostra o Label se uma situação válida for escolhida
        double total = 0.0;

        // Percorre todos os itens da TableView
        for (Compra compra : compraTableView.getItems()) {
            if (compra != null && compra.getSituacao() != null && compra.getSituacao().equals(situacao)) {
                // Adiciona o valor da compra ao total se a situação coincidir
                total += compra.getValor();
            }
        }
        // Define o total calculado no Label
        LocaleUtil.moedaBrasilLabel(total, totalSituacaoLabel);
    }

    private void novaInstanciaDAO() {
        dao = new CompraDAO();
        cartaoDAO = new CartaoDAO();
        cartao = new Cartao();
        faturaDAO = new FaturaDAO();
    }

    private void labelVisible() {
        totalPessoaComboBox.valueProperty().addListener((ObservableValue<? extends Responsavel> observable, Responsavel oldValue, Responsavel newValue) -> {
            // Se algo for selecionado, tornar o Label visível; caso contrário, mantê-lo invisível
            totalPessoaLabel.setVisible(newValue != null);
        });

        totalCartaoComboBox.valueProperty().addListener((ObservableValue<? extends Cartao> observable, Cartao oldValue, Cartao newValue) -> {
            // Se algo for selecionado, tornar o Label visível; caso contrário, mantê-lo invisível
            totalCartaoLabel.setVisible(newValue != null);
        });

        totalCategoriaComboBox.valueProperty().addListener((ObservableValue<? extends Categoria> observable, Categoria oldValue, Categoria newValue) -> {
            // Se algo for selecionado, tornar o Label visível; caso contrário, mantê-lo invisível
            totalCategoriaLabel.setVisible(newValue != null);
        });
    }

    private void configurarTabela() {

        // Configura as colunas do TableView
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descricaoColuna.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        responsavelColuna.setCellValueFactory(cellData -> {
            String nome = cellData.getValue().getResponsavel().getNome(); // Obtenha o nome do responsável
            if ("Sem Responsável".equals(nome)) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(nome);
            }
        });

        // Configura a coluna para exibir o nome do cartão
        cartaoColuna.setCellValueFactory(cellData -> {
            cartao = cellData.getValue().getFatura().getCartao();
            return new SimpleStringProperty(cartao.getNome());
        });

        valorColuna.setCellValueFactory(new PropertyValueFactory<>("valor"));
        LocaleUtil.moedaBrasilColuna(valorColuna);
        categoriaColuna.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoria().getNome()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataColuna.setCellValueFactory(cellData -> {
            // Converte o Date para LocalDateTime, formata e retorna como SimpleStringProperty
            LocalDate localDate = cellData.getValue().getDataCompra().toLocalDate();
            String formattedDate = localDate.format(formatter);
            return new SimpleStringProperty(formattedDate);
        });
        // Configura a coluna para exibir as parcelas no formato 1/12
        parcelaColuna.setCellValueFactory(cellData -> {
            Compra compra = cellData.getValue();
            int parcelaAtual = compra.getParcelaAtual();
            int parcelaTotal = compra.getParcelaTotal();

            String parcelaText;
            if (parcelaAtual == 0 && parcelaTotal == 0) {
                parcelaText = "";
            } else {
                parcelaText = parcelaAtual + "/" + parcelaTotal;
            }

            return new SimpleStringProperty(parcelaText);
        });
        totalColuna.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        LocaleUtil.moedaBrasilColuna(totalColuna);
        situacaoColuna.setCellValueFactory(new PropertyValueFactory<>("situacao"));
        situacaoColuna.setCellFactory(col -> new SituacaoTableCellCompra(dao));

    }

    private void calcularColunaValorTotal() {
        // Usando um Map para armazenar a soma dos valores por nome de cartão
        Map<String, Double> somaPorCartao = new HashMap<>();

        // Percorre todos os itens da TableView
        for (Compra compra : compraTableView.getItems()) {
            // Obtém o objeto Fatura associado à compra
            Fatura fatura = compra.getFatura();
            if (fatura != null) {
                String nomeCartao = fatura.getCartao().getNome();
                double valorCompra = compra.getValorTotal();
                // Atualiza a soma total para o nome de cartão correspondente
                somaPorCartao.put(nomeCartao, somaPorCartao.getOrDefault(nomeCartao, 0.0) + valorCompra);
            }
        }
        // Exibe a soma dos valores para cada cartão
        for (Map.Entry<String, Double> entrada : somaPorCartao.entrySet()) {
            String nomeCartao = entrada.getKey();
            Double soma = entrada.getValue();
        }
    }

    // Método para carregar compras de acordo com o período da fatura
    private void carregarComprasPorPeriodo() {
        String periodoCompra = parametroPesquisaCompra();

        compras = FXCollections.observableArrayList(dao.listarComprasPorPeriodo(periodoCompra));

        // Usar FilteredList para permitir a pesquisa
        FilteredList<Compra> filteredData = new FilteredList<>(compras, p -> true);

        // Adicionar um listener ao campo de pesquisa
        pesquisarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(compra -> {
                // Se o campo de pesquisa estiver vazio, mostra todas as faturas
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (compra.getDescricao().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pela descrição
                } else if (compra.getResponsavel().getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo responsavel
                } else if (compra.getFatura().getCartao().getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo nome do cartao
                } else if (compra.getCategoria().getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pelo nome da categoria
                } else if (compra.getSituacao().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtra pela situação
                } else if (compra.getDataCompra().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).contains(lowerCaseFilter)) {
                    return true; // Filtra pelo período formatado
                }
                return false; // Não corresponde a nenhum filtro
            });
        });

        SortedList<Compra> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(compraTableView.comparatorProperty());

        compraTableView.setItems(sortedData);
        atualizarSomaTotal();
        atualizarValorFatura();
        calcularColunaValorTotal();
        reloadComboBox();
    }

    // Método para calcular e atualizar a soma total
    private void atualizarSomaTotal() {
        double somaTotal = compraTableView.getItems().stream().mapToDouble(Compra::getValor).sum();
        LocaleUtil.moedaBrasilLabel(somaTotal, totalGeralLabel);
        int total = compraTableView.getItems().size();
        totalCadastroLabel.setText("Total de compras no lançamento selecionado: " + total);
    }

    private void carregaComboBox() {
        PreencherComboBox.ComboBoxCartoes(totalCartaoComboBox);
        PreencherComboBox.comboBoxResponsaveis(totalPessoaComboBox);
        PreencherComboBox.comboBoxCategorias(totalCategoriaComboBox);
        PreencherComboBox.comboBoxSituacao(totalSituacaoComboBox);
        PreencherComboBox.comboBoxFiltro(filtroComboBox);
        PreencherComboBox.comboBoxQuantidadePessoas(quantidadePessoasComboBox);
        PreencherComboBox.comboBoxCartaoString(totalFaturaCartaoComboBox);
        PreencherComboBox.comboBoxPessoas(alimentacaoPessoasComboBox);
        PreencherComboBox.comboBoxPessoaCartaoSituacao(filtroComboBoxSituacao);
        PreencherComboBox.preencherMeses(mesFaturaComboBox);
        PreencherComboBox.preencherAnosComFaturas(anoFaturaComboBox);
    }

    private void clickComboBox() {
        primeiroFiltroComboBox.setOnAction(event -> filtrarECalcularTotal());
        segundoFiltroComboBox.setOnAction(event -> filtrarECalcularTotal());
        quantidadePessoasComboBox.setOnAction(event -> exibirComboBoxAlimentacaoPessoa());
        colocarButton.setOnAction(e -> colocarResponsavel());
        removerButton.setOnAction(event -> removerResponsavel());

        segundoFiltroComboBoxSituacao.setOnAction(event -> {
            filtroCalculoSituacao();
        });

        terceiroFiltroComboBoxSituacao.setOnAction(event -> {
            filtroCalculoSituacao();
        });

        mesFaturaComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> obs, String oldFatura, String newFatura) -> {
            parametroPesquisaCompra();
            carregarComprasPorPeriodo();
        });

        anoFaturaComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Integer> obs, Integer oldAno, Integer newAno) -> {
            parametroPesquisaCompra();
            carregarComprasPorPeriodo();
        });
    }

    private void enableLabelAlimentacao() {
        valorTotalFaturaLabel.setVisible(true);
        totalFaturaCartaoLabel.setVisible(true);
        totalFaturaLabel.setVisible(true);
        totalFaturaCartaoComboBox.setVisible(true);
        alimentacaoTotalPane.setVisible(true);
    }

    private void disableLabelAlimentacao() {
        valorTotalFaturaLabel.setVisible(false);
        totalFaturaCartaoLabel.setVisible(false);
        totalFaturaLabel.setVisible(false);
        totalFaturaCartaoComboBox.setVisible(false);
        alimentacaoTotalPane.setVisible(false);
    }

    private void filtroCalculoSituacao() {
        String filtroSituacao = filtroComboBoxSituacao.getSelectionModel().getSelectedItem();
        String primeiroFiltro = primeiroFiltroComboBoxSituacao.getSelectionModel().getSelectedItem();
        String segundoFiltro = segundoFiltroComboBoxSituacao.getSelectionModel().getSelectedItem();
        String terceiroFiltro = terceiroFiltroComboBoxSituacao.getSelectionModel().getSelectedItem();

        switch (filtroSituacao) {
            case "Pessoa e Situação":
                if (primeiroFiltro != null && segundoFiltro != null) {
                    // Filtrar a tabela com base nos critérios selecionados
                    double total = compraTableView.getItems().stream()
                            .filter(compra -> compra.getResponsavel().getNome().equals(primeiroFiltro)
                            && compra.getSituacao().equals(segundoFiltro))
                            .mapToDouble(Compra::getValor)
                            .sum();

                    // Se o total for maior que 0, exibe o label e mostra o valor total
                    if (total > 0) {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabelSituacao);
                    } else {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabelSituacao);
                    }
                } else {
                    // Se um dos filtros não estiver selecionado, esconde o Label
                    totalFiltrosLabelSituacao.setVisible(false);
                }
                break;
            case "Cartão e Situação":
                if (primeiroFiltro != null && segundoFiltro != null) {
                    // Filtrar a tabela com base nos critérios selecionados
                    double total = compraTableView.getItems().stream()
                            .filter(compra -> compra.getFatura().getCartao().getNome().equals(primeiroFiltro)
                            && compra.getSituacao().equals(segundoFiltro))
                            .mapToDouble(Compra::getValor)
                            .sum();

                    // Se o total for maior que 0, exibe o label e mostra o valor total
                    if (total > 0) {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabelSituacao);
                    } else {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabelSituacao);
                    }
                } else {
                    // Se um dos filtros não estiver selecionado, esconde o Label
                    totalFiltrosLabelSituacao.setVisible(false);
                }
                break;
            case "Pessoa, Cartão e Situação":
                if (primeiroFiltro != null && segundoFiltro != null && terceiroFiltro != null) {
                    // Filtrar a tabela com base nos critérios selecionados
                    double total = compraTableView.getItems().stream()
                            .filter(compra -> compra.getResponsavel().getNome().equals(primeiroFiltro)
                            && compra.getFatura().getCartao().getNome().equals(segundoFiltro)
                            && compra.getSituacao().equals(terceiroFiltro))
                            .mapToDouble(Compra::getValor)
                            .sum();

                    // Se o total for maior que 0, exibe o label e mostra o valor total
                    if (total > 0) {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabelSituacao);
                    } else {
                        LocaleUtil.moedaBrasilLabel(total, totalFiltrosLabelSituacao);
                    }
                } else {
                    // Se um dos filtros não estiver selecionado, esconde o Label
                    totalFiltrosLabelSituacao.setVisible(false);
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    private void abrirCadastrar() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/compraCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Compra");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adicionarButton.getScene().getWindow());
            stage.setOnHidden(event -> carregarComprasPorPeriodo());
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    private void reloadComboBox() {
        PreencherComboBox.ComboBoxCartoes(totalCartaoComboBox);
        PreencherComboBox.comboBoxPessoaCartaoSituacao(filtroComboBoxSituacao);
        PreencherComboBox.comboBoxResponsaveis(totalPessoaComboBox);
        PreencherComboBox.comboBoxCategorias(totalCategoriaComboBox);
        PreencherComboBox.comboBoxSituacao(totalSituacaoComboBox);
        filtroComboBox.setValue("Selecione");
        PreencherComboBox.comboBoxQuantidadePessoas(quantidadePessoasComboBox);
        alimentacaoList.clear();
        alimentacaoTableView.setDisable(true);
    }

    private void atualizarValorFatura() {
        // Usando um Map para armazenar a soma dos valores por nome de cartão
        Map<String, Double> somaPorCartao = new HashMap<>();

        String periodoSelecionado = parametroPesquisaCompra(); // Exemplo: "2024-09"

        Date periodoExibido = null; // Variável para armazenar o período como java.sql.Date

        try {
            // Converte a String "2024-09" para um objeto java.sql.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            java.util.Date utilDate = dateFormat.parse(periodoSelecionado); // Converte para java.util.Date
            periodoExibido = new Date(utilDate.getTime()); // Converte para java.sql.Date
        } catch (ParseException e) {
            return; // Sai do método se ocorrer erro de conversão
        }

        // Percorre todos os itens da TableView
        for (Compra compra : compraTableView.getItems()) {
            // Obtém o objeto Fatura associado à compra
            Fatura fatura = compra.getFatura();
            if (fatura != null) {
                String nomeCartao = fatura.getCartao().getNome();
                double valorCompra = compra.getValor();

                // Atualiza a soma total para o nome de cartão correspondente
                somaPorCartao.put(nomeCartao, somaPorCartao.getOrDefault(nomeCartao, 0.0) + valorCompra);

                // Opcional: se precisar atualizar o períodoExibido com base na primeira fatura
                Date periodo = fatura.getPeriodo();
                if (periodoExibido == null) {
                    periodoExibido = periodo; // Armazena o período na primeira iteração
                }
            }
        }

        // Exibe a soma dos valores para cada cartão e atualiza a fatura no banco de dados
        for (Map.Entry<String, Double> entrada : somaPorCartao.entrySet()) {
            String nomeCartao = entrada.getKey();
            Double soma = entrada.getValue();

            // Busca o código do cartão no banco de dados
            int codigoCartao = cartaoDAO.buscaCodigoCartao(nomeCartao);

            // Busca a fatura pelo código do cartão e período
            Fatura novaFatura = faturaDAO.buscarFaturaPorCartao(codigoCartao, periodoExibido);

            // Atualiza o valor da fatura no banco de dados
            if (novaFatura != null) {
                faturaDAO.atualizarValor(soma, novaFatura.getCodigo());
            }
        }
    }

    private String parametroPesquisaCompra() {
        Map<String, String> mesesMap = new HashMap<>();
        mesesMap.put("Janeiro", "01");
        mesesMap.put("Fevereiro", "02");
        mesesMap.put("Março", "03");
        mesesMap.put("Abril", "04");
        mesesMap.put("Maio", "05");
        mesesMap.put("Junho", "06");
        mesesMap.put("Julho", "07");
        mesesMap.put("Agosto", "08");
        mesesMap.put("Setembro", "09");
        mesesMap.put("Outubro", "10");
        mesesMap.put("Novembro", "11");
        mesesMap.put("Dezembro", "12");

        // Pegando o valor selecionado do ComboBox
        int ano = anoFaturaComboBox.getValue(); // Exemplo: 2024
        String mesSelecionado = mesFaturaComboBox.getValue(); // Exemplo: "Setembro"
        String mesNumero = mesesMap.get(mesSelecionado); // Resultado: "09"

        // Criando a string no formato "2024-09"
        String periodoSelecionado = ano + "-" + mesNumero;

        return periodoSelecionado;
    }

}
