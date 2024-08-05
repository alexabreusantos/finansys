/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.marjax.finansys;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.util.AlertUtil;
import com.marjax.finansys.util.LocaleUtil;
import com.marjax.finansys.util.MaskFieldUtil;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class CartaoController implements Initializable {

    @FXML
    TextField pesquisarTextField;

    @FXML
    Label totalCadastroLabel;

    @FXML
    private TableView<Cartao> cartaoTableView;

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

    @FXML
    private Button adicionarButton;

    @FXML
    private Button alterarButton;

    @FXML
    private Button excluirButton;

    private CartaoDAO dao = new CartaoDAO();

    private String css = "/com/marjax/finansys/style/main.css";

    private ObservableList<Cartao> listaCartoes;

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

        AtivarBotoes();

        adicionarButton.setOnAction(event -> AbrirCadastrarAction());
        alterarButton.setOnAction(event -> editar());

        atualizarTableView();
        atualizarTotalCartoes();        
    }

    public void atualizarTableView() {
        listaCartoes = FXCollections.observableArrayList(dao.getAllCartoes());

        // Usar FilteredList para permitir a pesquisa
        FilteredList<Cartao> filteredData = new FilteredList<>(listaCartoes, p -> true);

        // Adicionar um listener ao campo de pesquisa
        pesquisarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cartao -> {
                // Se o campo de pesquisa estiver vazio, exibir todas as categorias
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Comparar o nome da categoria com o texto da pesquisa para uma correspondência exata, ignorando maiúsculas/minúsculas
                String filter = newValue.toLowerCase();
                return cartao.getNome().toLowerCase().equals(filter);
            });
        });

        cartaoTableView.setItems(filteredData);
    }

    public void AtivarBotoes() {
        // Adicionar listener para ativar/desativar botão Excluir
        cartaoTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Cartao>() {
            @Override
            public void changed(ObservableValue<? extends Cartao> observable, Cartao oldValue, Cartao newValue) {
                excluirButton.setDisable(newValue == null);
                alterarButton.setDisable(newValue == null);
            }
        }
        );
    }

    @FXML
    public void AbrirCadastrarAction() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/cartaoCadastrar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Cadastrar Cartão");
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            CartaoCadastrarController controller = fxmlLoader.getController();
            controller.setCartaoDAO(dao);
            controller.setCartaoController(this);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(adicionarButton.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void excluirCategoriaSelecionada() {
        Cartao cartao = cartaoTableView.getSelectionModel().getSelectedItem();
        if (cartao != null) {
            // Mostrar popup de confirmação
            Optional<ButtonType> result = AlertUtil.showConfirmationAlert(
                    "Confirmação de Exclusão",
                    "Excluir Cartao",
                    "Tem certeza que deseja excluir o cartão " + cartao.getNome() + "?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Se o usuário confirmar, excluir a categoria
                boolean success = dao.excluirCartao(cartao.getCodigo());
                if (success) {
                    listaCartoes.remove(cartao); // Remover da lista original
                    AlertUtil.showInformationAlert("Sucesso", null, "Categoria excluída com sucesso.");
                    atualizarTableView(); // Atualizar a TableView
                    atualizarTotalCartoes();
                } else {
                    AlertUtil.showErrorAlert("Erro", null, "Erro ao excluir a categoria.");
                }
            }
        } else {
            AlertUtil.showWarningAlert("Aviso", null, "Nenhuma categoria selecionada.");
        }
    }

    public void atualizarTotalCartoes() {
        int total = dao.getTotalCartoes();
        totalCadastroLabel.setText(total + " cartões cadastrados!");
    }

    @FXML
    private void editar() {
        Cartao cartaoSelecionado = cartaoTableView.getSelectionModel().getSelectedItem();
        if (cartaoSelecionado != null) {
            abrirTelaEdicao(cartaoSelecionado);
        } else {
            AlertUtil.showWarningAlert("Seleção Inválida", "Nenhum cartão selecionado", "Por favor, selecione um cartão para editar.");
        }
    }

    private void abrirTelaEdicao(Cartao cartao) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/cartaoEditar.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            root.getStylesheets().add(css);
            stage.setTitle("Editar " + cartao.getNome());
            stage.setScene(new Scene(root));
            stage.setMaximized(false);
            stage.setResizable(false);

            CartaoEditarController controller = fxmlLoader.getController();
            controller.setCartaoDAO(dao);
            controller.setCartaoController(this);            
            controller.setCartao(cartao);

            // Define o estágio secundário como modal e bloqueia a interação com outras janelas
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(alterarButton.getScene().getWindow());
            stage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
