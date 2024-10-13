/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.marjax.finansys.util;

import com.marjax.finansys.dao.CartaoDAO;
import com.marjax.finansys.dao.CategoriaDAO;
import com.marjax.finansys.dao.FaturaDAO;
import com.marjax.finansys.dao.ResponsavelDAO;
import com.marjax.finansys.model.Cartao;
import com.marjax.finansys.model.Categoria;
import com.marjax.finansys.model.Fatura;
import com.marjax.finansys.model.Responsavel;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

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

        // Seleciona o mês atual
        int mesAtual = LocalDate.now().getMonthValue(); // Pega o valor do mês atual (1 a 12)
        comboBox.getSelectionModel().select(mesAtual - 1); // Subtrai 1 para ajustar ao índice da lista
    }

    public static void preencherAnos(ComboBox<Integer> comboBox) {
        int anoAtual = Year.now().getValue();
        int anoInicio = anoAtual - 5;
        int anoFim = anoAtual + 5;

        List<Integer> anos = IntStream.rangeClosed(anoInicio, anoFim)
                .boxed()
                .collect(Collectors.toList());

        comboBox.setItems(FXCollections.observableArrayList(anos));

        // Seleciona o ano atual
        comboBox.getSelectionModel().select(Integer.valueOf(anoAtual));
    }

    public static void agendarAtualizacaoAnos(ComboBox<Integer> comboBox) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Calcula o tempo até o próximo ano
        long delay = calcularTempoAteProximoAno();

        // Agenda a tarefa para rodar na virada de ano
        scheduler.scheduleAtFixedRate(() -> preencherAnos(comboBox), delay, 365, TimeUnit.DAYS);
    }

    private static long calcularTempoAteProximoAno() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime proximoAno = agora.plusYears(1).withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        return TimeUnit.SECONDS.convert(java.time.Duration.between(agora, proximoAno).toMillis(), TimeUnit.MILLISECONDS);
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

    public static void ComboBoxCartoes(ComboBox<Cartao> comboBox) {
        CartaoDAO dao = new CartaoDAO();
        comboBox.getItems().clear();

        // Cria um objeto Responsavel para a opção "Selecione"
        Cartao selecione = new Cartao();
        selecione.setNome("Selecione");

        comboBox.getItems().add(selecione);
        comboBox.getItems().addAll(dao.listarCartoes());

        // Define um CellFactory para exibir o nome do mês no ComboBox
        comboBox.setCellFactory(lv -> new ListCell<Cartao>() {
            @Override
            protected void updateItem(Cartao item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        // Define como o nome do responsavel deve ser exibido quando o item é selecionado
        comboBox.setButtonCell(new ListCell<Cartao>() {
            @Override
            protected void updateItem(Cartao item, boolean empty) {
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

    public static void comboBoxCartoesFatura(ComboBox<Cartao> comboBox) {
        FaturaDAO dao = new FaturaDAO();
        comboBox.getItems().clear();

        // Cria um objeto Responsavel para a opção "Selecione"
        Cartao selecione = new Cartao();
        selecione.setNome("Selecione");

        comboBox.getItems().add(selecione);

        // Adiciona os responsáveis ao ComboBox, exceto aqueles com nome "Sem Responsável"
        for (Cartao cartao : dao.buscarCartoesDistintos()) {
            comboBox.getItems().add(cartao);
        }

        // Define um CellFactory para exibir o nome do responsável no ComboBox
        comboBox.setCellFactory(lv -> new ListCell<Cartao>() {
            @Override
            protected void updateItem(Cartao item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });

        // Define como o nome do responsável deve ser exibido quando o item é selecionado
        comboBox.setButtonCell(new ListCell<Cartao>() {
            @Override
            protected void updateItem(Cartao item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNome());
            }
        });
    }

    public static void comboBoxFaturaCartao(ComboBox<Fatura> comboBox, int codigo) {
        // Buscar as faturas associadas ao cartão
        FaturaDAO dao = new FaturaDAO();
        List<Fatura> faturas = dao.buscarFaturasPorCartao(codigo);

        // Criar uma instância de Fatura para a opção "Selecione"
        Fatura selecioneFatura = new Fatura();
        selecioneFatura.setCodigo(0);  // Código especial para representar "Selecione"
        selecioneFatura.setPeriodo(null);  // Sem período

        // Adicionar "Selecione" como a primeira opção
        ObservableList<Fatura> faturasObservableList = FXCollections.observableArrayList();
        faturasObservableList.add(selecioneFatura);  // Adiciona "Selecione" como a primeira opção
        faturasObservableList.addAll(faturas);  // Adiciona as faturas reais depois

        // Setar a lista de faturas no ComboBox
        comboBox.setItems(faturasObservableList);

        // Configurar o StringConverter para exibir o período formatado das faturas
        comboBox.setConverter(new StringConverter<Fatura>() {
            private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

            @Override
            public String toString(Fatura fatura) {
                if (fatura != null) {
                    if (fatura.getCodigo() == 0) {
                        return "Selecione";  // Exibir "Selecione" para a fatura especial
                    } else if (fatura.getPeriodo() != null) {
                        return dateFormat.format(fatura.getPeriodo());
                    }
                }
                return "";
            }

            @Override
            public Fatura fromString(String string) {
                return null;  // Não necessário para o preenchimento
            }
        });

        // Definir "Selecione" como o valor inicial
        comboBox.getSelectionModel().selectFirst();
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
        String[] opcoes = {"Pessoa e Cartão", "Pessoa e Categoria", "Cartão e Categoria"};
        comboBox.getItems().add("Selecione");

        comboBox.getItems().addAll(opcoes);

        // Define um CellFactory para exibir o nome do responsável no ComboBox
        comboBox.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

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

    public static void comboBoxPessoaSituacao(ComboBox<String> comboBox) {
        ResponsavelDAO dao = new ResponsavelDAO();
        comboBox.getItems().clear();

        // Adiciona a opção "Selecione" como o primeiro item
        comboBox.getItems().add("Selecione");

        // Adiciona os nomes dos responsáveis ao ComboBox
        for (Responsavel responsavel : dao.listarResponsaveisComboBox()) {
            comboBox.getItems().add(responsavel.getNome());
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
        for (Cartao cartao : dao.listarCartoes()) {
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

    public static void comboBoxPessoaCartaoSituacao(ComboBox comboBox) {
        // Cria uma lista observável com as opções desejadas
        ObservableList<String> statusOptions = FXCollections.observableArrayList("Selecione", "Pessoa e Situação", "Cartão e Situação", "Pessoa, Cartão e Situação");

        // Define a lista de opções no ComboBox
        comboBox.setItems(statusOptions);

        // Define a opção padrão como 'Selecione'
        comboBox.getSelectionModel().selectFirst();
    }

    public static void comboBoxAnoCompra(ComboBox<Integer> comboBox) {
        // Obtém o ano atual
        int anoAtual = Year.now().getValue();

        // Preenche o ComboBox com anos até o ano atual
        IntStream.rangeClosed(2023, anoAtual).forEach(ano -> comboBox.getItems().add(ano));

        // Seleciona o ano atual por padrão
        comboBox.setValue(anoAtual);
    }

    public static void adicionarAnoComboBox(ComboBox<Integer> comboBox) {
        // Cria uma thread separada para verificar a data e hora continuamente
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // Obtém a data e hora atuais
                    LocalDateTime agora = LocalDateTime.now();

                    // Verifica se é o primeiro dia do ano e exatamente meia-noite
                    if (agora.getMonthValue() == 1 && agora.getDayOfMonth() == 1 && agora.getHour() == 0 && agora.getMinute() == 0 && agora.getSecond() == 0) {
                        int proximoAno = Year.now().getValue();

                        // Atualiza o ComboBox na thread da UI (JavaFX)
                        Platform.runLater(() -> {
                            if (!comboBox.getItems().contains(proximoAno)) {
                                comboBox.getItems().add(proximoAno);
                            }
                        });
                    }

                    // Espera 1 segundo antes de verificar novamente
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });

        // Inicia a thread
        thread.setDaemon(true); // Daemon thread para encerrar junto com a aplicação
        thread.start();
    }

    public static void preencherAnosComFaturas(ComboBox<Integer> comboBox) {
        FaturaDAO dao = new FaturaDAO();
        List<Fatura> faturas = dao.carregarComboBoxFaturas();
        
        // Extrair os anos das faturas
        List<Integer> anos = faturas.stream()
            .map(fatura -> fatura.getPeriodo().toLocalDate().getYear())
            .distinct() // Remover duplicatas
            .sorted() // Ordenar os anos
            .collect(Collectors.toList());

        // Preencher o ComboBox com os anos extraídos
        comboBox.setItems(FXCollections.observableArrayList(anos));

        // Selecionar o ano atual, se presente
        int anoAtual = Year.now().getValue();
        if (anos.contains(anoAtual)) {
            comboBox.getSelectionModel().select(Integer.valueOf(anoAtual));
        } else if (!anos.isEmpty()) {
            // Se o ano atual não estiver na lista, selecionar o primeiro ano da lista
            comboBox.getSelectionModel().select(0);
        }
    }
}
