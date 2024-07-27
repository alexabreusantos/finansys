/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.util.LocaleUtil;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class CartaoController implements Initializable {

    TextField txtPesquisar;

    @FXML
    private TableView<Cartao> tabelaCartoes;

    @FXML
    private TableColumn<Cartao, Integer> codigoColuna;

    @FXML
    private TableColumn<Cartao, String> nomeColuna;

    @FXML
    private TableColumn<Cartao, Double> limiteColuna;

    @FXML
    private TableColumn<Cartao, Double> limiteDisponivelColuna;

    @FXML
    private TableColumn<Cartao, Double> limiteUsadoColuna;

    @FXML
    private TableColumn<Cartao, Integer> fechamentoColuna;

    @FXML
    private TableColumn<Cartao, Integer> vencimentoColuna;

    private final ObservableList<Cartao> cartoes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codigoColuna.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        limiteColuna.setCellValueFactory(new PropertyValueFactory<>("limite"));
        LocaleUtil.MoedaBrazil(limiteColuna);
        limiteDisponivelColuna.setCellValueFactory(new PropertyValueFactory<>("limiteDisponivel"));
        LocaleUtil.MoedaBrazil(limiteDisponivelColuna);
        limiteUsadoColuna.setCellValueFactory(new PropertyValueFactory<>("limiteUsado"));
        LocaleUtil.MoedaBrazil(limiteUsadoColuna);
        fechamentoColuna.setCellValueFactory(new PropertyValueFactory<>("fechamento"));
        vencimentoColuna.setCellValueFactory(new PropertyValueFactory<>("vencimento"));
        tabelaCartoes.setItems(cartoes);

        loadData();
    }

    private void loadData() {
        CartaoDAO responsavelDAO = new CartaoDAO();
        try {
            List<Cartao> listaCartoes = responsavelDAO.getAllResponsaveis();
            cartoes.setAll(listaCartoes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
