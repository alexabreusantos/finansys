/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.dao.CategoriaDAO;
import com.marjax.finansys.dao.ResponsavelDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Categoria;
import com.marjax.finansys.model.Responsavel;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 *
 * @author Alex de Abreu dos Santos <alexdeabreudossantos@gmail.com>
 */
public class PreencherComboBox {

    public static void ComboBoxDias(ComboBox dias) {
        // Preenche o ComboBox com valores de 1 a 31
        for (int i = 1; i <= 31; i++) {
            dias.getItems().add(i);
        }
    }

    public static void preencherMeses(ComboBox<String> comboBox) {
        Locale localeBR = Locale.forLanguageTag("pt-BR");

        List<String> meses = IntStream.rangeClosed(1, 12)
                .mapToObj(i -> {
                    String mes = Month.of(i).getDisplayName(TextStyle.FULL, localeBR);
                    // Converte a primeira letra para maiúscula e o restante para minúsculas
                    return mes.substring(0, 1).toUpperCase() + mes.substring(1).toLowerCase();
                })
                .collect(Collectors.toList());

        comboBox.setItems(FXCollections.observableArrayList(meses));
    }

    public static void preencherAnos(ComboBox<Integer> comboBox) {
        int anoAtual = Year.now().getValue();
        int anoInicio = anoAtual - 2;
        int anoFim = anoAtual + 5;

        List<Integer> anos = IntStream.rangeClosed(anoInicio, anoFim)
                .boxed()
                .collect(Collectors.toList());

        comboBox.setItems(FXCollections.observableArrayList(anos));
    }

    public static Integer obterNumeroMes(String nomeMes) {
        // Usando switch para mapear o nome do mês para o número do mês
        switch (nomeMes) {
            case "Janeiro":
                return 1;
            case "Fevereiro":
                return 2;
            case "Março":
                return 3;
            case "Abril":
                return 4;
            case "Maio":
                return 5;
            case "Junho":
                return 6;
            case "Julho":
                return 7;
            case "Agosto":
                return 8;
            case "Setembro":
                return 9;
            case "Outubro":
                return 10;
            case "Novembro":
                return 11;
            case "Dezembro":
                return 12;
            default:
                return null; // Ou você pode lançar uma exceção se preferir
        }
    }

    public static void ComboBoxCartoes(ComboBox<Cartao> cartaoComboBox) {
        CartaoDAO dao = new CartaoDAO();
        cartaoComboBox.getItems().clear();

        // Cria um objeto Responsavel para a opção "Selecione"
        Cartao selecione = new Cartao();
        selecione.setNome("Selecione");

        cartaoComboBox.getItems().add(selecione);
        cartaoComboBox.getItems().addAll(dao.buscarCartoes());

        // Define um CellFactory para exibir o nome do mês no ComboBox
        cartaoComboBox.setCellFactory(lv -> new ListCell<Cartao>() {
            @Override
            protected void updateItem(Cartao item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        // Define como o nome do cartao deve ser exibido quando o item é selecionado
        cartaoComboBox.setButtonCell(new ListCell<Cartao>() {
            @Override
            protected void updateItem(Cartao item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
    }

    public static void comboBoxResponsaveis(ComboBox<Responsavel> comboBox) {
        ResponsavelDAO dao = new ResponsavelDAO();
        comboBox.getItems().clear();

        // Cria um objeto Responsavel para a opção "Selecione"
        Responsavel selecione = new Responsavel();
        selecione.setNome("Selecione");

        comboBox.getItems().add(selecione);

        // Adiciona os responsáveis ao ComboBox, exceto aqueles com nome "Sem Responsável"
        for (Responsavel responsavel : dao.listarResponsaveisComboBox()) {
            if (!"Sem Responsável".equals(responsavel.getNome())) {
                comboBox.getItems().add(responsavel);
            }
        }

        // Define um CellFactory para exibir o nome do responsável no ComboBox
        comboBox.setCellFactory(lv -> new ListCell<Responsavel>() {
            @Override
            protected void updateItem(Responsavel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        // Define como o nome do responsável deve ser exibido quando o item é selecionado
        comboBox.setButtonCell(new ListCell<Responsavel>() {
            @Override
            protected void updateItem(Responsavel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
    }

    public static void comboBoxCategorias(ComboBox<Categoria> comboBox) {
        CategoriaDAO dao = new CategoriaDAO();
        comboBox.getItems().clear();

        // Cria um objeto Responsavel para a opção "Selecione"
        Categoria selecione = new Categoria();
        selecione.setNome("Selecione");

        comboBox.getItems().add(selecione);
        comboBox.getItems().addAll(dao.listarCategoriasComboBox());

        // Define um CellFactory para exibir o nome do mês no ComboBox
        comboBox.setCellFactory(lv -> new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        // Define como o nome do responsavel deve ser exibido quando o item é selecionado
        comboBox.setButtonCell(new ListCell<Categoria>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
    }

    public static void comboBoxSituacao(ComboBox comboBox) {
        // Cria uma lista observável com as opções desejadas
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Selecione", "Paga", "Pendente");

        // Define a lista de opções no ComboBox
        comboBox.setItems(statusOptions);

        // Define a opção padrão como 'Selecione'
        comboBox.getSelectionModel().selectFirst();
    }

    public static void comboBoxFiltro(ComboBox comboBox) {
        // Cria uma lista observável com as opções desejadas
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Selecione", "Pessoa e Cartão", "Pessoa e Categoria", "Cartão e Categoria");

        // Define a lista de opções no ComboBox
        comboBox.setItems(statusOptions);

        // Define a opção padrão como 'Selecione'
        comboBox.getSelectionModel().selectFirst();
    }

    public static void comboBoxPessoas(ComboBox<String> comboBox) {
        ResponsavelDAO dao = new ResponsavelDAO();
        comboBox.getItems().clear();

        // Adiciona a opção "Selecione" como o primeiro item
        comboBox.getItems().add("Selecione");

        // Adiciona os nomes dos responsáveis ao ComboBox
        for (Responsavel responsavel : dao.listarResponsaveisComboBox()) {
            if (!"Sem Responsável".equals(responsavel.getNome())) {
                comboBox.getItems().add(responsavel.getNome());
            }
        }

        // Define um CellFactory para exibir o nome do responsável no ComboBox
        comboBox.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

        // Define como o nome do responsável deve ser exibido quando o item é selecionado
        comboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
    }

    public static void comboBoxCartaoString(ComboBox<String> comboBox) {
        CartaoDAO dao = new CartaoDAO();
        comboBox.getItems().clear();

        // Adiciona a opção "Selecione" como o primeiro item
        comboBox.getItems().add("Selecione");

        // Adiciona os nomes dos responsáveis ao ComboBox
        for (Cartao cartao : dao.buscarCartoes()) {
            comboBox.getItems().add(cartao.getNome());
        }

        // Define um CellFactory para exibir o nome do responsável no ComboBox
        comboBox.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

        // Define como o nome do responsável deve ser exibido quando o item é selecionado
        comboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
    }

    public static void comboBoxCategoriaString(ComboBox<String> comboBox) {
        CategoriaDAO dao = new CategoriaDAO();
        comboBox.getItems().clear();

        // Adiciona a opção "Selecione" como o primeiro item
        comboBox.getItems().add("Selecione");

        // Adiciona os nomes dos responsáveis ao ComboBox
        for (Categoria categoria : dao.listarCategoriasComboBox()) {
            comboBox.getItems().add(categoria.getNome());
        }

        // Define um CellFactory para exibir o nome do responsável no ComboBox
        comboBox.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

        // Define como o nome do responsável deve ser exibido quando o item é selecionado
        comboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
    }

    public static void comboBoxQuantidadePessoas(ComboBox<String> quantidadeComboBox) {
        ResponsavelDAO dao = new ResponsavelDAO();

        // Adiciona a opção "Selecione" ao ComboBox
        quantidadeComboBox.getItems().add("Selecione");

        // Obtém a quantidade de responsáveis cadastrados
        int quantidade = dao.getTotalResponsaveis();

        // Adiciona os números de 1 até a quantidade de responsáveis no ComboBox
        for (int i = 1; i <= quantidade; i++) {
            quantidadeComboBox.getItems().add(String.valueOf(i));
        }

        // Define a seleção padrão como "Selecione"
        quantidadeComboBox.getSelectionModel().selectFirst();
    }

    public static ObservableList<String> comboBoxAlimentacaoResponsaveis(ComboBox<String> comboBox) {
        ResponsavelDAO dao = new ResponsavelDAO();
        ObservableList<String> responsaveis = FXCollections.observableArrayList();

        // Adiciona os outros responsáveis
        responsaveis.addAll(dao.alimentacaoResponsaveis());
        
        // Remove itens com nome "Sem Responsável" da lista
        responsaveis.removeIf(nome -> "Sem Responsável".equals(nome));

        // Limpa o ComboBox e adiciona os itens
        comboBox.getItems().clear();

        // Adiciona os responsáveis ao ComboBox, exceto aqueles com nome "Sem Responsável"
        for (Responsavel responsavel : dao.listarResponsaveisComboBox()) {
            if (!"Sem Responsável".equals(responsavel.getNome())) {
                comboBox.getItems().addAll(responsaveis);
            }
        }

        // Define um CellFactory para exibir o nome do responsável no ComboBox
        comboBox.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

        // Define como o nome do responsável deve ser exibido quando o item é selecionado
        comboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

        return responsaveis;
    }
}
