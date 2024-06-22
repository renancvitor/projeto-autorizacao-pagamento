//package Entities;
//
//import Servicoes.SistemaSolicitacao;
//import Servicoes.Solicitacao;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ListView;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//
//public class TelaGestor {
//    private Gestor gestor;
//
//    public TelaGestor(Gestor gestor) {
//        this.gestor = gestor;
//    }
//
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Tela do Gestor");
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//
//        ListView<Solicitacao> listView = new ListView<>();
//        SistemaSolicitacao sistema = SistemaSolicitacao.getInstance();
//        listView.getItems().addAll(sistema.getSolicitacoesPendentes());
//
//        grid.add(listView, 0, 0, 2, 1);
//
//        Button aprovarButton = new Button("Aprovar");
//        grid.add(aprovarButton, 0, 1);
//
//        Button rejeitarButton = new Button("Rejeitar");
//        grid.add(rejeitarButton, 1, 1);
//
//        aprovarButton.setOnAction(e -> {
//            Solicitacao selecionada = listView.getSelectionModel().getSelectedItem();
//            if (selecionada != null) {
//                sistema.aprovarSolicitacao(selecionada);
//                listView.getItems().remove(selecionada);
//                System.out.println("Solicitação aprovada.");
//            } else {
//                System.out.println("Selecione uma solicitação para aprovar.");
//            }
//        });
//
//        rejeitarButton.setOnAction(e -> {
//            Solicitacao selecionada = listView.getSelectionModel().getSelectedItem();
//            if (selecionada != null) {
//                sistema.rejeitarSolicitacao(selecionada);
//                listView.getItems().remove(selecionada);
//                System.out.println("Solicitação rejeitada.");
//            } else {
//                System.out.println("Selecione uma solicitação para rejeitar.");
//            }
//        });
//
//        Scene scene = new Scene(grid, 400, 300);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//}
