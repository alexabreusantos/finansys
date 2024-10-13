/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CompraDAO;
import com.marjax.finansys.dao.FaturaDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Categoria;
import com.marjax.finansys.model.Compra;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.model.Responsavel;
import com.marjax.finansys.util.AlertUtil;
import com.marjax.finansys.util.ConverterDate;
import com.marjax.finansys.util.LocaleUtil;
import com.marjax.finansys.util.PreencherComboBox;
import com.marjax.finansys.util.ValidationUtil;
import com.marjax.finansys.util.ValorConverter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class CompraCadastrarController implements Initializable {

    @FXML
    private ComboBox<Responsavel> responsavelComboBox;
    @FXML
    private ComboBox<Categoria> categoriaComboBox;
    @FXML
    private ComboBox<Cartao> cartaoComboBox;
    @FXML
    private ComboBox<Fatura> faturaComboBox;

    @FXML
    private TextField descricaoTextField;
    @FXML
    private TextField valorTextField;

    @FXML
    private DatePicker dataDatePicker;

    @FXML
    private Label quantidadeParcelaLabel;
    @FXML
    private Label faturaLabel;
    @FXML
    private Spinner<Integer> quantidadeParcelaSpinner;

    @FXML
    private ToggleGroup situacao;

    @FXML
    private RadioButton unicaRadioButton;
    @FXML
    private RadioButton parceladaRadioButton;
    @FXML
    private RadioButton mensalRadioButton;
    @FXML
    private ToggleGroup tipoCompra;

    @FXML
    private Button salvarButton;
    @FXML
    private Button adicionarCategoriaButton;
    @FXML
    private Button adicionarResponsavelButton;
    @FXML
    private Button adicionarCartaoButton;
    @FXML
    private Button adicionarFaturaButton;

    private CompraDAO compraDAO = new CompraDAO();
    private FaturaDAO faturaDAO = new FaturaDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarComboBox();
        listener();
        validarData();
        compraDAO = new CompraDAO();
        LocaleUtil.applyBrazilianCurrencyFormat(valorTextField);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 2);
        quantidadeParcelaSpinner.setValueFactory(valueFactory);

        dataDatePicker.setDayCellFactory(LocaleUtil.getDayCellFactory());

        salvarButton.setOnAction((var event) -> salvar());
        adicionarCategoriaButton.setOnAction(event -> abrirCadastrarCategoriaAction());
        adicionarResponsavelButton.setOnAction(event -> abrirCadastrarResponsavelAction());
        adicionarCartaoButton.setOnAction(event -> abrirCadastrarCartaoAction());
        adicionarFaturaButton.setOnAction(event -> abrirCadastrarFaturaAction());
    }

    private void salvarCompraMensal(int meses) {
        ValorConverter converter = new ValorConverter();

        // Obter os valores da compra
        Compra compraBase = new Compra();
        compraBase.setDescricao(descricaoTextField.getText());
        Double valorConvertido = converter.valor(valorTextField);
        compraBase.setValor(valorConvertido);

        Categoria categoria = categoriaComboBox.getValue();
        compraBase.setCategoria(categoria);

        Responsavel responsavel = responsavelComboBox.getValue();
        compraBase.setResponsavel(responsavel);

        Cartao cartao = cartaoComboBox.getValue();
        compraBase.setFatura(new Fatura()); // A fatura será associada no loop abaixo

        RadioButton selectedRadioButton = (RadioButton) situacao.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedText = selectedRadioButton.getText();
            compraBase.setSituacao(selectedText);
        }

        RadioButton selectedTipoCompra = (RadioButton) tipoCompra.getSelectedToggle();
        if (selectedTipoCompra != null) {
            String selectedText = selectedTipoCompra.getText();
            compraBase.setTipoCompra(selectedText);
        }

        // Obter a data inicial da primeira compra
        LocalDate dataInicial = dataDatePicker.getValue();

        List<Fatura> faturas = new ArrayList<>(); // Lista para armazenar as faturas
        List<Compra> comprasMensais = new ArrayList<>(); // Lista para armazenar as compras mensais

        // Para cada mês, criar uma nova compra
        for (int mesAtual = 0; mesAtual < meses; mesAtual++) {
            Compra compraMensal = new Compra();
            // Copiar os valores da compraBase para a compraMensal
            compraMensal.setDescricao(compraBase.getDescricao());
            compraMensal.setValor(compraBase.getValor());
            compraMensal.setCategoria(compraBase.getCategoria());
            compraMensal.setResponsavel(compraBase.getResponsavel());
            compraMensal.setSituacao(compraBase.getSituacao());
            compraMensal.setTipoCompra(compraBase.getTipoCompra());

            // Definir a data para o mês atual
            LocalDate dataCompraMensal = dataInicial.plusMonths(mesAtual);
            Date dataSQL = Date.valueOf(dataCompraMensal);
            
            // Verificar se já existe uma fatura para o mês/ano atual e o cartão
            Fatura faturaExistente = faturaDAO.buscarPorCartaoEPeriodo(cartao, dataCompraMensal);
            
            YearMonth proximoMes = YearMonth.from(dataCompraMensal);
            Date periodoFatura = ConverterDate.formatarData(proximoMes, cartao.getFechamento());
            faturas = faturaDAO.listarFaturasCartaoPeriodo(cartao, periodoFatura, meses);
            //parei aqui...
            
            System.out.println("Parâmetros: ");
            System.out.println("Cód. Cartao: " + cartao.getCodigo());
            System.out.println("Período: " + periodoFatura);
            System.out.println("Meses: " + meses);            
            System.out.println("---------------------------------------------------");

            if (faturaExistente != null) {
                // Exibir apenas faturas encontradas
//                compraMensal.setFatura(faturaExistente); // Associar fatura existente
//                System.out.println("Fatura Encontrada: ");
//                System.out.println("Período: " + faturaExistente.getCodigo());
//                System.out.println("Período: " + faturaExistente.getPeriodo());
//                System.out.println("Valor: " + faturaExistente.getValor());
//                System.out.println("Situação: " + faturaExistente.getSituacao());
//                System.out.println("Cartão: " + faturaExistente.getCartao().getNome());
//                System.out.println("---------------------------------------------------");
            } else {
                // Não exibir nada se a fatura não for encontrada (simplesmente ignorar)
                System.out.println("Nenhuma fatura encontrada para o mês: " + dataCompraMensal);
            }

            comprasMensais.add(compraMensal); // Adiciona a compra à lista de compras mensais
        }

        // Salvar todas as faturas de uma só vez
        if (!faturas.isEmpty()) {
            //faturaDAO.salvarLista(faturas); // Aqui a mensagem de sucesso será exibida apenas uma vez

//            // Imprimir as faturas no console
//            System.out.println("Faturas cadastradas:");
//            for (Fatura fatura : faturas) {
//                System.out.println("Código: " + fatura.getCodigo());
//                System.out.println("Período: " + fatura.getPeriodo());
//                System.out.println("Valor: " + fatura.getValor());
//                System.out.println("Situação: " + fatura.getSituacao());
//                System.out.println("Cód. cartão: " + fatura.getCartao().getCodigo());
//                System.out.println("Nome cartão: " + fatura.getCartao().getNome());
//                System.out.println("---------------------------------------------------- ");
//            }
        } else {
            System.out.println("Lista vazia");
        }

        // Salvar as compras mensais
        for (Compra compra : comprasMensais) {
            //compraDAO.salvar(compra); // Salva as compras mensais individualmente
        }

        // Fechar a janela após o salvamento
        ((Stage) salvarButton.getScene().getWindow()).close();
    }

    private void salvarCompraUnica() {
        ValorConverter converter = new ValorConverter();

        Compra compra = new Compra();
        compra.setDescricao(descricaoTextField.getText());
        Double valorConvertido = converter.valor(valorTextField);
        compra.setValor(valorConvertido);

        Categoria categoria = categoriaComboBox.getValue();
        compra.setCategoria(categoria);

        Responsavel responsavel = responsavelComboBox.getValue();
        compra.setResponsavel(responsavel);

        Fatura fatura = faturaComboBox.getValue();
        Cartao cartao = cartaoComboBox.getValue();
        fatura.setCartao(cartao);
        compra.setFatura(fatura);

        RadioButton selectedSituacao = (RadioButton) situacao.getSelectedToggle();
        if (selectedSituacao != null) {
            String selectedText = selectedSituacao.getText();
            compra.setSituacao(selectedText);
        }

        RadioButton selectedTipoCompra = (RadioButton) tipoCompra.getSelectedToggle();
        if (selectedTipoCompra != null) {
            String selectedText = selectedTipoCompra.getText();
            compra.setTipoCompra(selectedText);
        }

        // Obtém a data selecionada no DatePicker
        LocalDate dataSelecionada = dataDatePicker.getValue();
        Date dataSQL = Date.valueOf(dataSelecionada);
        compra.setDataCompra(dataSQL);

        compra.setValorTotal(valorConvertido * 1);

        if (compraDAO.existe(compra)) {
            AlertUtil.showErrorAlert("Atenção", "Compra já existente", "Já existe uma compra para a fatura cadastrada.");
        } else {
            compraDAO.salvarCompraUnica(compra);
            ((Stage) salvarButton.getScene().getWindow()).close();
        }
    }

    private void salvarCompraParcelada() {
        ValorConverter converter = new ValorConverter();

        // Obter os valores da compra
        Compra compraBase = new Compra();
        compraBase.setDescricao(descricaoTextField.getText());
        Double valorConvertido = converter.valor(valorTextField);
        compraBase.setValor(valorConvertido);

        Categoria categoria = categoriaComboBox.getValue();
        compraBase.setCategoria(categoria);

        Responsavel responsavel = responsavelComboBox.getValue();
        compraBase.setResponsavel(responsavel);

        Cartao cartao = cartaoComboBox.getValue();
        compraBase.setFatura(new Fatura()); // Associaremos a fatura no loop abaixo

        RadioButton selectedRadioButton = (RadioButton) situacao.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedText = selectedRadioButton.getText();
            compraBase.setSituacao(selectedText);
        }

        RadioButton selectedTipoCompra = (RadioButton) tipoCompra.getSelectedToggle();
        if (selectedTipoCompra != null) {
            String selectedText = selectedTipoCompra.getText();
            compraBase.setTipoCompra(selectedText);
        }

        // Obter data da primeira parcela
        LocalDate dataSelecionada = dataDatePicker.getValue();

        // Total de parcelas e inicializar a parcela atual
        int totalParcelas = quantidadeParcelaSpinner.getValue();
        compraBase.setParcelaTotal(totalParcelas);

        List<Compra> comprasParceladas = new ArrayList<>(); // Lista para armazenar as parcelas

        // Para cada parcela, criar uma nova compra
        for (int parcelaAtual = 1; parcelaAtual <= totalParcelas; parcelaAtual++) {
            Compra compraParcela = new Compra();

            // Cópia manual dos valores de compraBase para compraParcela
            compraParcela.setDescricao(compraBase.getDescricao());
            compraParcela.setValor(compraBase.getValor());
            compraParcela.setCategoria(compraBase.getCategoria());
            compraParcela.setResponsavel(compraBase.getResponsavel());
            compraParcela.setSituacao(compraBase.getSituacao());
            compraParcela.setParcelaTotal(compraBase.getParcelaTotal());
            compraParcela.setTipoCompra(compraBase.getTipoCompra());

            LocalDate dataParcela = dataSelecionada.plusMonths(parcelaAtual - 1); // Parcela do mês seguinte
            Date dataSQL = Date.valueOf(dataParcela);

            // Verifica se já existe uma fatura para o mês/ano atual e o cartão
            Fatura faturaExistente = faturaDAO.buscarPorCartaoEPeriodo(cartao, dataParcela);
            if (faturaExistente != null) {
                compraParcela.setFatura(faturaExistente);
            } else {
                Fatura novaFatura = new Fatura();
                novaFatura.setCartao(cartao);
                novaFatura.setPeriodo(Date.valueOf(dataParcela));
                novaFatura.setValor(0.0);
                novaFatura.setSituacao("Pendente");
                faturaDAO.salvar(novaFatura);
                compraParcela.setFatura(novaFatura);
            }

            // Define o valor total da parcela
            compraParcela.setValorTotal(valorConvertido * totalParcelas);
            compraParcela.setParcelaAtual(parcelaAtual);
            compraParcela.setDataCompra(dataSQL);

            // Adiciona a parcela à lista
            comprasParceladas.add(compraParcela);
        }

        // Verifica se todas as parcelas são válidas (se não existem duplicadas)
        boolean allSaved = true;
        for (Compra compra : comprasParceladas) {
            if (compraDAO.existe(compra)) {
                AlertUtil.showErrorAlert("Atenção", "Compra já existente", "Já existe uma compra para a fatura no período " + compra.getDataCompra().toLocalDate().getMonth() + "/" + compra.getDataCompra().toLocalDate().getYear());
                allSaved = false;
                break; // Sai do loop se uma compra já existir
            }
        }

        // Se todas as parcelas forem válidas, salva todas de uma vez
        if (allSaved) {
            compraDAO.salvarCompraParcelada(comprasParceladas);
            AlertUtil.showInformationAlert("Sucesso", "Compra cadastrada!", "Todas as parcelas da compra foram cadastradas com sucesso.");
        }

        // Fecha a janela
        ((Stage) salvarButton.getScene().getWindow()).close();
    }

    private void salvar() {
        boolean[] hasError = {false};

        boolean validaDescricao = ValidationUtil.validateNonEmpty(descricaoTextField, "Descrição", hasError);
        if (!validaDescricao) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        boolean validaValor = ValidationUtil.validateNonEmpty(valorTextField, "Valor", hasError);
        if (!validaValor) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        boolean validaCategoria = ValidationUtil.validateComboBoxCategoria(categoriaComboBox, "Categoria", hasError);
        if (!validaCategoria) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        boolean validaResponsavel = ValidationUtil.validateComboBoxResponsavel(responsavelComboBox, "Responsável", hasError);
        if (!validaResponsavel) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        boolean validaCartao = ValidationUtil.validateComboBoxCartao(cartaoComboBox, "Cartao", hasError);
        if (!validaCartao) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        boolean validaFatura = ValidationUtil.validateComboBoxFatura(faturaComboBox, "Fatura", hasError);
        if (!validaFatura) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        boolean validaData = ValidationUtil.validateData(dataDatePicker, "Data", hasError);
        if (!validaData) {
            return; // Interrompe a validação se o fechamento for inválido
        }

        if (!hasError[0]) {
            RadioButton selectedRadioButton = (RadioButton) tipoCompra.getSelectedToggle();
            if ("Única".equals(selectedRadioButton.getText())) {
                salvarCompraUnica();
            }
            if ("Mensal".equals(selectedRadioButton.getText())) {
                salvarCompraMensal(4);
            }
            if ("Parcelada".equals(selectedRadioButton.getText())) {
                salvarCompraParcelada();
            }
        }
    }

    private void carregarComboBox() {
        PreencherComboBox.comboBoxResponsaveis(responsavelComboBox);
        PreencherComboBox.comboBoxCategorias(categoriaComboBox);
        PreencherComboBox.comboBoxCartoesFatura(cartaoComboBox);
    }

    private void listener() {
        // Adiciona um listener para verificar qual RadioButton está selecionado
        tipoCompra.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (parceladaRadioButton.isSelected()) {
                // Ativa quando parcelado for selecionado
                quantidadeParcelaLabel.setDisable(false);
                quantidadeParcelaSpinner.setDisable(false);
            } else if (unicaRadioButton.isSelected() || mensalRadioButton.isSelected()) {
                // Desativa quando não parcelado for selecionado
                quantidadeParcelaLabel.setDisable(true);
                quantidadeParcelaSpinner.setDisable(true);
            }
        });

        cartaoComboBox.valueProperty().addListener((obs, oldCartao, newCartao) -> {
            if (newCartao != null) {
                if (!"Selecione".equals(newCartao.getNome())) {
                    faturaLabel.setDisable(false);
                    faturaComboBox.setDisable(false);
                    adicionarFaturaButton.setDisable(false);
                    PreencherComboBox.comboBoxFaturaCartao(faturaComboBox, newCartao.getCodigo());
                } else {
                    faturaLabel.setDisable(true);
                    faturaComboBox.setDisable(true);
                    adicionarFaturaButton.setDisable(true);
                    faturaComboBox.getItems().clear();
                }

            }
        });
    }

    private void abrirCadastrarCategoriaAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/categoriaCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            //root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Categoria");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adicionarCategoriaButton.getScene().getWindow());
            stage.setOnHidden(event -> PreencherComboBox.comboBoxCategorias(categoriaComboBox));
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    private void abrirCadastrarResponsavelAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/responsavelCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            //root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Responsável");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adicionarResponsavelButton.getScene().getWindow());
            stage.setOnHidden(event -> PreencherComboBox.comboBoxResponsaveis(responsavelComboBox));
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    private void abrirCadastrarCartaoAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/cartaoCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            //root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Cartão");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adicionarCartaoButton.getScene().getWindow());
            stage.setOnHidden(event -> PreencherComboBox.comboBoxCartoesFatura(cartaoComboBox));
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    private void abrirCadastrarFaturaAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/faturaCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            // Obtém o controller da nova janela
            FaturaCadastrarController faturaCadastrarController = fxmlLoader.getController();

            // Pega o cartão selecionado
            Cartao cartaoSelecionado = cartaoComboBox.getSelectionModel().getSelectedItem();

            // Passa o cartão selecionado para o controller da fatura
            faturaCadastrarController.setCartaoSelecionado(cartaoSelecionado);

            Stage stage = new Stage();
            //root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Fatura");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adicionarFaturaButton.getScene().getWindow());
            stage.setOnHidden(event -> PreencherComboBox.comboBoxFaturaCartao(faturaComboBox, cartaoSelecionado.getCodigo()));
            stage.showAndWait();
        } catch (IOException e) {
        }
    }

    private void validarData() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Listener para permitir apenas números e barras
        dataDatePicker.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            // Permitir apenas números
            newValue = newValue.replaceAll("[^0-9]", "");

            // Limitar a entrada a 8 dígitos (ddMMyyyy)
            if (newValue.length() > 8) {
                newValue = newValue.substring(0, 8);
            }

            // Formatar automaticamente a data (dd/MM/yyyy)
            StringBuilder formattedDate = new StringBuilder(newValue);
            if (newValue.length() >= 3) {
                formattedDate.insert(2, "/");
            }
            if (newValue.length() >= 6) {
                formattedDate.insert(5, "/");
            }

            // Atualizar o valor do TextField com a data formatada
            dataDatePicker.getEditor().setText(formattedDate.toString());

            // Colocar o cursor no final do texto
            dataDatePicker.getEditor().positionCaret(formattedDate.length());
        });
        // Listener para validar a data ao perder o foco
        dataDatePicker.getEditor().focusedProperty().addListener((observable, oldValue, isFocused) -> {
            if (!isFocused) { // Se o campo perdeu o foco
                String input = dataDatePicker.getEditor().getText();
                try {
                    LocalDate date = LocalDate.parse(input, dateFormatter);
                    dataDatePicker.setValue(date); // Define a data se for válida
                } catch (DateTimeParseException e) {
                    AlertUtil.showErrorAlert("Erro!", "Erro de data", "Data inválida: " + input);
                    dataDatePicker.getEditor().setText(""); // Limpa o campo se a data for inválida
                }
            }
        });
    }
}
