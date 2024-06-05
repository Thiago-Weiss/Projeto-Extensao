import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ControllerMenu extends ControllerUtils {
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////

    private ObservableList<Pedido> listPedidoTable = FXCollections.observableArrayList();

    @FXML
    private TableView<Pedido> tabelaPedidos;

    @FXML
    private TableColumn<Pedido, String> colunaDescricao;

    @FXML
    private TableColumn<Pedido, String> colunaTelefones;

    @FXML
    private TableColumn<Pedido, String> colunaFaltaPagar;

    @FXML
    private TableColumn<Pedido, String> colunaNomes;

    @FXML
    private TableColumn<Pedido, String> colunaTempo;

    @FXML
    private TextField filtroNome;

    @FXML
    private TextField filtroCpf;

    @FXML
    private DatePicker filtroData;

    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////

    private final int TAMANHO_CELULA = 45;

    private Map<String, TaskWeek> taskMap = new HashMap<>();

    private ObservableList<String> daysOfWeek = FXCollections.observableArrayList("Dia",
            "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado", "Domingo");

    private ObservableList<String> taskCores = FXCollections.observableArrayList("Branco", "Cinza", "Laranja", "Rosa",
            "Ciano", "Amarelo", "Vermelho", "Rosa-Claro", "Verde-Claro", "Verde", "Violeta");

    private static final Map<String, VBox> mapDay = new HashMap<>();
    private static final Map<VBox, String> mapBox = new HashMap<>();
    private static final Map<String, Color> mapColor = new HashMap<>();

    @FXML
    private ComboBox<String> taskCorComboBox;

    @FXML
    private ComboBox<String> taskDiaComboBox;

    @FXML
    private TextArea taskText;

    @FXML
    private Label excluirButton;

    @FXML
    private VBox dia1;

    @FXML
    private VBox dia2;

    @FXML
    private VBox dia3;

    @FXML
    private VBox dia4;

    @FXML
    private VBox dia5;

    @FXML
    private VBox dia6;

    @FXML
    private VBox dia7;

    @FXML
    private VBox dia8;

    /////////////////////// initialize ///////////////////////
    /////////////////////// initialize ///////////////////////
    /////////////////////// initialize ///////////////////////
    @FXML
    void initialize() {
        excluirButton.getStyleClass().add("task-excluir");

        setUpTablePedido();
        setUpTask();
    }
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////

    private void setUpTablePedido() {
        // lista de pedidos da tabela
        listPedidoTable.setAll(Data.getListaPedidos());
        tabelaPedidos.setItems(listPedidoTable);
        // tabelaPedidos.getStylesheets().add(getClass().getResource("interfaces/tabela.css").toExternalForm());

        // seta os campos da tabela
        colunaNomes.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colunaTelefones.setCellValueFactory(new PropertyValueFactory<>("telefoneCliente"));
        colunaDescricao.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDescricaoPedidoLinha());
        });
        colunaTempo.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getTempoRestante());
        });
        colunaFaltaPagar.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDescricaoFaltaPagar());
        });

        // cria o botao do dublo click pra abrir o pedido
        tabelaPedidos.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // se for o cabeçalho da tabela retorna nada
                if (event.getTarget() instanceof TableColumnHeader) {
                    return;
                }
                // Obtém o item selecionado na tabela
                Pedido pedidoSelecionado = tabelaPedidos.getSelectionModel().getSelectedItem();
                // Verifica se um item foi selecionado e se o clique foi duplo
                if (pedidoSelecionado != null) {
                    abrirPedido(pedidoSelecionado, event);
                }
            }
        });

        setLimitCaracters(filtroNome, 20);
        setLimitCaracters(filtroCpf, 20);
    }

    private void abrirPedido(Pedido pedido, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("interfaces/Pedido.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Pedido " + pedido.getNomeCliente());
            stage.setScene(scene);
            ControllerPedido controlador = loader.getController();
            controlador.initialize(pedido, null, null);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// purgatorio

    @FXML
    void financeiroFunc(ActionEvent event) {
        URL janela = getClass().getResource("interfaces/Financa.fxml");
        String janelaName = "Financeiro";
        switchWindow(event, janela, janelaName);
    }

    @FXML
    void cadastrarFunc(ActionEvent event) {
        URL janela = getClass().getResource("interfaces/CriarPedido.fxml");
        String janelaName = "Cadastrar Pedido";
        switchWindow(event, janela, janelaName);
    }

    @FXML
    void filtrarPedidosFunc(ActionEvent event) {
        LocalDate data = filtroData.getValue();
        String nome = filtroNome.getText().trim();
        String cpf = filtroCpf.getText().trim();

        if (data != null) {
            listPedidoTable.setAll(Data.getFiltroData(data));
            filtroData.setValue(null);
        } else if (!nome.isEmpty()) {
            listPedidoTable.setAll(Data.getFiltroNome(nome));
        } else if (!cpf.isEmpty()) {
            listPedidoTable.setAll(Data.getFiltroCpf(cpf));
        } else {
            listPedidoTable.setAll(Data.getListaPedidos());
        }

    }

    @FXML
    void fecharAppFunc(ActionEvent event) {
        Platform.exit();
    }

    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////

    private void setUpTask() {
        setUpMapeamento();
        setUpVbox();
        setUpControler_Task();

    }// purgatorio

    private void setUpMapeamento() {
        mapDay.put("Sábado", dia1);
        mapDay.put("Segunda-feira", dia2);
        mapDay.put("Terça-feira", dia3);
        mapDay.put("Quarta-feira", dia4);
        mapDay.put("Quinta-feira", dia5);
        mapDay.put("Sexta-feira", dia6);
        mapDay.put("Domingo", dia7);

        mapBox.put(dia1, "Sábado");
        mapBox.put(dia2, "Segunda-feira");
        mapBox.put(dia3, "Terça-feira");
        mapBox.put(dia4, "Quarta-feira");
        mapBox.put(dia5, "Quinta-feira");
        mapBox.put(dia6, "Sexta-feira");
        mapBox.put(dia7, "Domingo");

        mapColor.put("Cinza", Color.rgb(238, 238, 238));
        mapColor.put("Branco", Color.WHITE);
        mapColor.put("Laranja", Color.ORANGE);
        mapColor.put("Rosa", Color.PINK);
        mapColor.put("Ciano", Color.CYAN);
        mapColor.put("Amarelo", Color.YELLOW);
        mapColor.put("Vermelho", Color.RED);
        mapColor.put("Rosa-Claro", Color.LIGHTPINK);
        mapColor.put("Verde-Claro", Color.LIGHTGREEN);
        mapColor.put("Verde", Color.GREEN);
        mapColor.put("Violeta", Color.VIOLET);

    }

    /////////////////////// DRAG DROP ///////////////////////
    /////////////////////// DRAG DROP ///////////////////////
    /////////////////////// DRAG DROP ///////////////////////
    /////////////////////// DRAG DROP ///////////////////////
    /////////////////////// DRAG DROP ///////////////////////
    /////////////////////// DRAG DROP ///////////////////////

    private void setUpVbox() {
        setDragDrop(dia1);
        setDragDrop(dia2);
        setDragDrop(dia3);
        setDragDrop(dia4);
        setDragDrop(dia5);
        setDragDrop(dia6);
        setDragDrop(dia7);
        setExcluirDragDrop(excluirButton);
    }

    private void setDragDrop(VBox box) {
        box.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        box.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String taskId = db.getString();
                TaskWeek task = taskMap.get(taskId);
                if (task != null) {
                    task.getBox().getChildren().remove(task.getLabel());
                    int index = (int) ((int) event.getY() / (box.getHeight() / (box.getChildren().size() + 1)));
                    if (index > box.getChildren().size()) {
                        index = box.getChildren().size();
                    }
                    box.getChildren().add(index, task.getLabel());
                    task.setBox(box);
                    task.setDiaSemana(mapBox.get(box));
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
// purgatorio
    private void setExcluirDragDrop(Label box) {
        box.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
                box.setStyle("-fx-background-color: #ff0000; -fx-background-insets: 2px;");
            }
            event.consume();
        });

        box.setOnDragExited(event -> {
            box.setStyle(null);
            box.getStyleClass().add("task-excluir");
        });

        box.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String taskId = db.getString();
                TaskWeek task = taskMap.get(taskId);
                if (task != null) {
                    try {
                        task.getBox().getChildren().remove(task.getLabel());
                        Data.removeTask(task);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Erro ao apagar a tarefa do banco de dados");
                        System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
                    }
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setUpControler_Task() {
        // define o ENTER como hotkey pra criar a task
        taskText.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();// purgatorio
                criarTaskFunc(null);
            }
        });
        taskDiaComboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                criarTaskFunc(null);
            }
        });
        taskCorComboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                criarTaskFunc(null);
            }
        });

        setLimitCaracters(taskText, 50);
        taskDiaComboBox.setItems(daysOfWeek);
        taskCorComboBox.setItems(taskCores);
        taskCorComboBox.setValue(taskCores.get(0));

        // faz o combo box ser colorido
        taskCorComboBox.setCellFactory(param -> new ColoredListCell());
        taskCorComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            Color cor = mapColor.get(newVal);
            if (cor != null) {
                taskCorComboBox.setBackground(new Background(new BackgroundFill(cor, new CornerRadii(2), null)));
                taskCorComboBox
                        .setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(1),
                                new BorderWidths(1))));
            }
        });

        // cria as labels das tarefas
        for (TaskWeek task : Data.getListaTask()) {
            VBox box = mapDay.get(task.getDia());
            Label label = criarLabel(task);
            box.getChildren().add(label);
            task.setBox(box);
            task.setLabel(label);
        }
    }

    @FXML
    void criarTaskFunc(ActionEvent event) {

        boolean tudoCerto = true;
        if (taskDiaComboBox.getValue() == null || taskDiaComboBox.getValue().equals(daysOfWeek.get(0))) {
            tudoCerto = false;
            alertaCampoErrado(taskDiaComboBox);
        }
        if (taskText.getText().trim().isEmpty()) {
            tudoCerto = false;
            alertaCampoErrado(taskText);
        }
        if (tudoCerto) {
            String dia = taskDiaComboBox.getValue();
            String cor = taskCorComboBox.getValue();
            String texto = taskText.getText().trim();

            VBox box = mapDay.get(dia);
            box.getChildren().add(criarTask_Label(texto, cor, box, dia));

            taskDiaComboBox.setValue(daysOfWeek.get(0));
            taskText.clear();
            taskCorComboBox.setValue(taskCores.get(1));
        }// purgatorio
    }

    private Label criarTask_Label(String texto, String cor, VBox box, String diaSemana) {
        try {
            Label label = new Label(texto);
            TaskWeek task = new TaskWeek(texto, cor, label, box, diaSemana);
            Data.criarTask(task);
            label.setPadding(new Insets(2, 5, 2, 5)); // Padding de 10 pixels em todos os lados
            label.setWrapText(true);
            label.setPrefWidth(200);
            label.setPrefHeight(TAMANHO_CELULA);
            label.setBackground(new Background(new BackgroundFill(mapColor.get(cor), CornerRadii.EMPTY, null)));
            label.setAlignment(javafx.geometry.Pos.CENTER);

            String taskId = UUID.randomUUID().toString();
            taskMap.put(taskId, task);

            label.setOnDragDetected(event -> {
                ClipboardContent content = new ClipboardContent();
                content.putString(taskId);

                Dragboard db = label.startDragAndDrop(TransferMode.MOVE);
                db.setContent(content);
                db.setDragView(label.snapshot(null, null), label.getWidth() / 2, label.getHeight() / 2);

                event.consume();
            });
            return label;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao criar a tarefa no BD");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }
        return null;
    }

    private static class ColoredListCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setTextFill(Color.BLACK);
            if (item == null || empty) {
                setText(null);
                setBackground(Background.EMPTY);
            } else {
                setText(item);
                Color cor = mapColor.get(item);
                if (cor != null) {// purgatorio
                    setBackground(new Background(new BackgroundFill(cor, null, null)));
                }
            }
        }
    }

    private Label criarLabel(TaskWeek task) {
        Label label = new Label(task.getText());
        label.setPadding(new Insets(0, 5, 2, 5)); // Padding de 10 pixels em todos os lados
        label.setWrapText(true);
        label.setPrefWidth(200);
        label.setPrefHeight(TAMANHO_CELULA);
        label.setBackground(
                new Background(new BackgroundFill(mapColor.get(task.getCor()), CornerRadii.EMPTY, null)));
        label.setAlignment(javafx.geometry.Pos.CENTER);

        String taskId = UUID.randomUUID().toString();
        taskMap.put(taskId, task);

        label.setOnDragDetected(event -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(taskId);
// purgatorio
            Dragboard db = label.startDragAndDrop(TransferMode.MOVE);
            db.setContent(content);
            db.setDragView(label.snapshot(null, null), label.getWidth() / 2, label.getHeight() / 2);

            event.consume();
        });
        return label;
    }
}
