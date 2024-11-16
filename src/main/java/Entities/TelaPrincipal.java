package Entities;

import DAO.SolicitacaoDAO;
import Servicoes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaPrincipal extends Application {
    private Usuario usuario;
    private Connection connection;
    private TableView<Solicitacao> table;
    private Label totalPendentesLabel;
    private Label totalAprovadasLabel;
    private Label totalRejeitadasLabel;
    private SolicitacaoService solicitacaoService;
    private ObservableList<Solicitacao> observableList;
    private PermissaoService permissaoService; // NOVO
    private UsuarioController usuarioController; // NOVO

    public TelaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema_pagamentos", "root", "123456789");
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            this.usuarioController = new UsuarioController(permissaoService); // NOVO
            this.permissaoService = new PermissaoService(); // NOVO
            this.solicitacaoService = new SolicitacaoService(solicitacaoDAO, permissaoService);
            this.observableList = FXCollections.observableArrayList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema Principal");

        VBox layout = new VBox(10);
        Label welcomeLabel = new Label("Bem-vindo, " + usuario.getLogin() + "!");
        layout.getChildren().add(welcomeLabel);

        Usuario usuarioAtual = UserService.getUsuarioLogado();

        if (permissaoService.gerenciarUsuarios(usuarioAtual)) {

            MenuBar menuBarUser = new MenuBar();
            Menu usuarioMenu = new Menu("Gerenciar Usuários");

            MenuItem cadastrarUsuarioItem = new MenuItem("Cadastrar Usuário");
            cadastrarUsuarioItem.setOnAction(e -> cadastrarUsuario());

            MenuItem editarPermissoesItem = new MenuItem("Editar Permissões");
            editarPermissoesItem.setOnAction(e -> editarPermissoes());

            usuarioMenu.getItems().addAll(cadastrarUsuarioItem, editarPermissoesItem);
            menuBarUser.getMenus().add(usuarioMenu);
            layout.getChildren().add(menuBarUser);

            MenuBar menuBarFunc = new MenuBar();
            Menu funcionarioMenu = new Menu("Gerenciar funcionários");

            MenuItem cadastrarDeparamentoItem = new MenuItem("Cadastrar Departamento");
            cadastrarDeparamentoItem.setOnAction(e -> cadastrarDepartamento());

            MenuItem cadastrarCargoItem = new MenuItem("Cadastrar Cargo");
            cadastrarCargoItem.setOnAction(e -> cadastrarCargo());

            funcionarioMenu.getItems().addAll(cadastrarDeparamentoItem, cadastrarCargoItem);
            menuBarFunc.getMenus().add(funcionarioMenu);
            layout.getChildren().add(menuBarFunc);

            MenuBar menuBarPessoa = new MenuBar();
            Menu pessoaMenu = new Menu("Cadastrar");

            MenuItem cadastrarPessoaItem = new MenuItem("Cadastrar Pessoa");
            cadastrarPessoaItem.setOnAction(e -> abrirCadastroPessoa(primaryStage));

            pessoaMenu.getItems().add(cadastrarPessoaItem);
            menuBarPessoa.getMenus().add(pessoaMenu);
            layout.getChildren().add(menuBarPessoa);

            HBox menuBarContainer = new HBox();
            menuBarContainer.setSpacing(10);

            menuBarContainer.getChildren().addAll(menuBarUser, menuBarFunc, menuBarPessoa);

            layout.getChildren().add(menuBarContainer);

        }

        Label resumoLabel = new Label(""); // Resumo Rápido:
        totalPendentesLabel = new Label();
        totalAprovadasLabel = new Label();
        totalRejeitadasLabel = new Label();
        atualizarResumoRapido();

        VBox vBox = new VBox();

        VBox.setMargin(resumoLabel, new javafx.geometry.Insets(10, 0, 10, 0));
        VBox.setMargin(totalPendentesLabel, new javafx.geometry.Insets(1, 0, 1, 0));
        VBox.setMargin(totalAprovadasLabel, new javafx.geometry.Insets(1, 0, 1, 0));
        VBox.setMargin(totalRejeitadasLabel, new javafx.geometry.Insets(1, 0, 1, 0));

        vBox.getChildren().addAll(resumoLabel, totalPendentesLabel, totalAprovadasLabel, totalRejeitadasLabel);
        layout.getChildren().add(vBox);

        Button novaSolicitacaoButton = new Button("Nova Solicitação");
        novaSolicitacaoButton.setOnAction(e -> {
            TelaSolicitacao telaSolicitacao = new TelaSolicitacao(connection, usuario, table);
            Stage stage = new Stage();
            telaSolicitacao.start(stage);
        });
        layout.getChildren().add(novaSolicitacaoButton);

        Button analisadosButton = new Button("Solicitações Analisadas");
        analisadosButton.setOnAction(e -> {
            TelaAnalisados telaAnalisados = new TelaAnalisados(connection);
            Stage stage = new Stage();
            telaAnalisados.start(stage);
        });
        layout.getChildren().add(analisadosButton);

        HBox topoLayout = new HBox(10, resumoLabel, novaSolicitacaoButton, analisadosButton);

        table = new TableView<>();
        TableColumn<Solicitacao, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);
        idCol.setCellFactory(column -> new TableCell<Solicitacao, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item.toString());
                    Tooltip tooltip = new Tooltip("ID: "+ item);
                    setTooltip(tooltip);
                }
            }
        });

        TableColumn<Solicitacao, String> fornecedorCol = new TableColumn<>("Fornecedor");
        fornecedorCol.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        fornecedorCol.setPrefWidth(150);
        fornecedorCol.setCellFactory(column -> new TableCell<Solicitacao, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item);
                    Tooltip tooltip = new Tooltip("Fornecedor: " + item);
                    setTooltip(tooltip);
                }
            }
        });

        TableColumn<Solicitacao, String> descricaoCol = new TableColumn<>("Descrição");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        descricaoCol.setPrefWidth(215);
        descricaoCol.setCellFactory(tc -> new TableCell<>() {
            private final Label label = new Label();
            private final Tooltip tooltip = new Tooltip();

            {
                label.setWrapText(true);
                label.setStyle("-fx-font-size: 12px; -fx-text-fill: #ffffff;");
                label.setMaxWidth(Double.MAX_VALUE);
                tooltip.setWrapText(true);
                tooltip.setStyle("-fx-font-size: 12px;");

                Tooltip.install(label, tooltip);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    tooltip.setText(item);
                    setGraphic(label);
                }
            }
        });

        TableColumn<Solicitacao, LocalDate> dataCriacaoCol = new TableColumn<>("Data de Criação");
        dataCriacaoCol.setCellValueFactory(new PropertyValueFactory<>("dataCriacao"));
        dataCriacaoCol.setPrefWidth(150);

        TableColumn<Solicitacao, String> dataPagamentoCol = new TableColumn<>("Data Pagamento");
        dataPagamentoCol.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
        dataPagamentoCol.setPrefWidth(120);

        TableColumn<Solicitacao, String> formaPagamentoCol = new TableColumn<>("Forma Pagamento");
        formaPagamentoCol.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));
        formaPagamentoCol.setPrefWidth(140);

        TableColumn<Solicitacao, Double> valorTotalCol = new TableColumn<>("Valor Total");
        valorTotalCol.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        valorTotalCol.setPrefWidth(100);

        TableColumn<Solicitacao, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            StatusSolicitacao status = cellData.getValue().getStatus();
            String statusStr = status == StatusSolicitacao.PENDENTE ? "PENDENTE" : status.toString();
            return new SimpleStringProperty(statusStr);
        });
        statusCol.setPrefWidth(80);

        TableColumn<Solicitacao, Void> approveCol = new TableColumn<>("Aprovar");
        Callback<TableColumn<Solicitacao, Void>, TableCell<Solicitacao, Void>> cellFactoryApprove = new Callback<>() {
            @Override
            public TableCell<Solicitacao, Void> call(final TableColumn<Solicitacao, Void> param) {
                final TableCell<Solicitacao, Void> cell = new TableCell<>() {
                    private final Button approveButton = new Button("✔");

                    {
                        approveButton.setOnAction(event -> {
                            Solicitacao solicitacao = getTableView().getItems().get(getIndex());
                            aprovarSolicitacao(usuario, permissaoService, solicitacao);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(approveButton);
                        }
                    }
                };
                return cell;
            }
        };
        approveCol.setCellFactory(cellFactoryApprove);
        approveCol.setPrefWidth(80);

        TableColumn<Solicitacao, Void> rejectCol = new TableColumn<>("Reprovar");
        Callback<TableColumn<Solicitacao, Void>, TableCell<Solicitacao, Void>> cellFactoryReject = new Callback<>() {
            @Override
            public TableCell<Solicitacao, Void> call(final TableColumn<Solicitacao, Void> param) {
                final TableCell<Solicitacao, Void> cell = new TableCell<>() {
                    private final Button rejectButton = new Button("✘");

                    {
                        rejectButton.setOnAction(event -> {
                            Solicitacao solicitacao = getTableView().getItems().get(getIndex());
                            // reprovarSolicitacao(solicitacao); ANTERIOR AO ABAIXO
                            reprovarSolicitacao(usuario, permissaoService, solicitacao);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(rejectButton);
                        }
                    }
                };
                return cell;
            }
        };
        rejectCol.setCellFactory(cellFactoryReject);
        rejectCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, fornecedorCol, descricaoCol, dataCriacaoCol, dataPagamentoCol, formaPagamentoCol, valorTotalCol, statusCol, approveCol, rejectCol);

        layout.getChildren().add(topoLayout);
        layout.getChildren().add(table);

        Scene scene = new Scene(layout, 1220, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.centerOnScreen();
        primaryStage.show();

        table.getItems().addListener(new ListChangeListener<Solicitacao>() {
            @Override
            public void onChanged(Change<? extends Solicitacao> c) {
                atualizarResumoRapido();
            }
        });

        refreshTable();

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    private void abrirCadastroPessoa(Stage primartStage) {
        Stage cadastroPessoaStage = new Stage();
        cadastroPessoaStage.initOwner(primartStage);

        TelaCadastroPessoa telaCadastroPessoa = new TelaCadastroPessoa(connection);
        telaCadastroPessoa.mostrarTela(cadastroPessoaStage); // Mostra tela cadastro pessoa

        cadastroPessoaStage.setTitle("Cadastro de Pessoa");
        cadastroPessoaStage.show();
    }

    private void atualizarResumoRapido() {
        try {
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            int pendentes = solicitacaoDAO.contarSolicitacoesPorStatus(StatusSolicitacao.PENDENTE);
            int aprovadas = solicitacaoDAO.contarSolicitacoesPorStatus(StatusSolicitacao.APROVADA);
            int reprovadas = solicitacaoDAO.contarSolicitacoesPorStatus(StatusSolicitacao.REPROVADA);

            totalPendentesLabel.setText("Total Pendentes: " + pendentes);
            totalAprovadasLabel.setText("Total Aprovadas: " + aprovadas);
            totalRejeitadasLabel.setText("Total Reprovadas: " + reprovadas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        System.out.println("Refreshing table...");
        try {
            List<Solicitacao> solicitacoesPendentes = solicitacaoService.getSolicitacoesPorStatus(StatusSolicitacao.PENDENTE);
            Platform.runLater(() -> {
                observableList.setAll(solicitacoesPendentes);
                table.setItems(observableList);
                atualizarResumoRapido();
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void aprovarSolicitacao(Usuario usuario, PermissaoService permissaoService, Solicitacao solicitacao) {
        solicitacaoService.atualizarStatusSolicitacao(usuario, permissaoService, solicitacao, StatusSolicitacao.APROVADA);
        refreshTable();
    }

    private void reprovarSolicitacao(Usuario usuario, PermissaoService permissaoService, Solicitacao solicitacao) {
        solicitacaoService.atualizarStatusSolicitacao(usuario, permissaoService, solicitacao, StatusSolicitacao.REPROVADA);
        refreshTable();
    }

    private void cadastrarUsuario() {
        Stage cadastroUsuarioStage = new Stage();
        TelaCadastroUsuario telaCadastroUsuario = new TelaCadastroUsuario(connection); // Construtor que aceita a conexão
        telaCadastroUsuario.setUsuarioController(usuarioController); // NOVO
        telaCadastroUsuario.start(cadastroUsuarioStage); // Exibe a tela de cadastro do usuário
    }

    private void editarPermissoes() {
        Stage editarPermissoesStage = new Stage();
        TelaEditarPermissoes telaEditarPermissoes = new TelaEditarPermissoes(connection); // Passa a conexão
        telaEditarPermissoes.start(editarPermissoesStage);
    }

    private void cadastrarDepartamento() {
        Stage cadastroDepartamentoStage = new Stage();
        TelaCadastroDepartamento telaCadastroDepartamento = new TelaCadastroDepartamento();
        telaCadastroDepartamento.setConnection(connection); // Configura a conexão
        telaCadastroDepartamento.start(cadastroDepartamentoStage); // Exibe a tela de cadastro do departamento
    }

    private void cadastrarCargo() {
        Stage cadastroCargoStage = new Stage();
        TelaCadastroCargo telaCadastroCargo = new TelaCadastroCargo();
        telaCadastroCargo.setConnection(connection);
        telaCadastroCargo.start(cadastroCargoStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
