import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.DoubleStream;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ControllerFinancas extends ControllerUtils {
    private static final Map<String, Integer> mapDay = new HashMap<>();

    ObservableList<String> periodoOpcoes = FXCollections.observableArrayList("Ultimo 1 Dia", "Ultimos 7 Dias",
            "Ultimos 30 Dias", "Ultimos 60 Dias", "Ultimos 90 Dias", "Todos os dias");

    private ObservableList<Pedido> listaPedidos = FXCollections.observableArrayList();

    @FXML
    private TableView<Pedido> tabelaPedidos;

    @FXML
    private TableColumn<Pedido, String> colunaDescricao;

    @FXML
    private TableColumn<Pedido, String> colunaPago;

    @FXML
    private TableColumn<Pedido, String> colunaCustos;

    @FXML
    private TableColumn<Pedido, String> colunaNomes;

    @FXML
    private TableColumn<Pedido, String> colunaTelefones;

    @FXML
    private TableColumn<Pedido, String> colunaTempo;

    @FXML
    private TableColumn<Pedido, String> colunaCpf;

    @FXML
    private Label labelCustosTotal;

    @FXML
    private Label labelLucroTotal;

    @FXML
    private Label labelQuantidade;

    @FXML
    private Label labelValorTotal;

    @FXML
    private TextField filtroCpf;

    @FXML
    private DatePicker filtroDataFim;

    @FXML
    private DatePicker filtroDataInicio;

    @FXML
    private TextField filtroNome;

    @FXML
    private ComboBox<String> periodoTempo;

    @FXML
    private Label tituloTabela;
    /////////////////////// initialize ///////////////////////
    /////////////////////// initialize ///////////////////////
    /////////////////////// initialize ///////////////////////

    @FXML
    void initialize() {

        periodoTempo.setItems(periodoOpcoes);
        periodoTempo.setValue(periodoOpcoes.getLast());
        mapDay.put("Ultimo 1 Dia", 1);
        mapDay.put("Ultimos 7 Dias", 7);
        mapDay.put("Ultimos 30 Dias", 30);
        mapDay.put("Ultimos 60 Dias", 60);
        mapDay.put("Ultimos 90 Dias", 90);

        setLimitCaracters(filtroNome, 20);
        setLimitCaracters(filtroCpf, 20);

        setUpTabelaPedidos();
    }

    private void setUpTabelaPedidos() {
        // lista de pedidos da tabela
        listaPedidos.setAll(InterfaceBancoDados.getUltimos_X_pedidos(50));
        tabelaPedidos.setItems(listaPedidos);

        // seta os campos da tabela
        colunaNomes.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colunaCpf.setCellValueFactory(cellData -> {
            String cpf = cellData.getValue().getCpfCliente();
            return new SimpleStringProperty(cpf != null ? cpf : "-");
        });

        colunaTelefones.setCellValueFactory(new PropertyValueFactory<>("telefoneCliente"));

        colunaDescricao.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDescricaoPedidoLinha());
        });
        colunaTempo.setCellValueFactory(cellData -> {
            LocalDate data = cellData.getValue().getDataEntrega();
            String dataText = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return new SimpleStringProperty(dataText);
        });

        colunaPago.setCellValueFactory(cellData -> {
            Double custos = cellData.getValue().getValorPago();
            String text = "R$: "+ (custos != null ? custos : 0.0);
            return new SimpleObjectProperty<>(text);
        });

        colunaCustos.setCellValueFactory(cellData -> {
            Double custos = cellData.getValue().getCustos();
            String text = "R$: "+ (custos != null ? custos : 0.0);
            return new SimpleObjectProperty<>(text);
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
        showEstatisticas();
    }

    private void abrirPedido(Pedido pedido, MouseEvent event) {
        // janela atual
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // att a tabela quando volta pra essa janela
        currentStage.setOnShowing(e -> {
            filtroFunc(null);
        });

        try {
            // Carrega o arquivo FXML para a nova janela
            FXMLLoader loader = new FXMLLoader(getClass().getResource("interfaces/Pedido.fxml"));
            Parent root = loader.load();
            // Cria uma nova instância de Stage para a nova janela
            Stage newStage = new Stage();
            // Define o título e a cena para a nova janela
            newStage.setTitle("Pedido " + pedido.getNomeCliente());
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            // Obtém o controlador da nova janela e inicializa com o pedido e o palco atual
            ControllerPedido controlador = loader.getController();
            controlador.initialize(pedido, currentStage, listaPedidos);
            // Esconde o palco atual e mostra a nova janela
            currentStage.hide();
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void filtroFunc(ActionEvent event) {
        LocalDate dataInicio = filtroDataInicio.getValue();
        LocalDate dataFim = filtroDataFim.getValue();
        String tempo = periodoTempo.getValue();
        String nome = filtroNome.getText().trim();
        String cpf = filtroCpf.getText().trim();

        if (dataInicio != null) {
            if (dataFim != null) {
                if (dataFim.compareTo(dataInicio) >= 0) {
                    listaPedidos.setAll(InterfaceBancoDados.getFiltroDatas(dataInicio, dataFim));
                    tituloTabela.setText("Histórico dos pedidos de " + dataInicio + " a " + dataFim);
                } else {
                    listaPedidos.setAll(InterfaceBancoDados.getFiltroDatas(dataFim, dataInicio));
                    tituloTabela.setText("Histórico dos pedidos de " + dataFim + " a " + dataInicio);
                }
            } else {
                listaPedidos.setAll(InterfaceBancoDados.getFiltroDatas(dataInicio, dataInicio.plusDays(90)));
                tituloTabela.setText("Histórico dos pedidos de " + dataInicio + " a " + dataInicio.plusDays(90));
            }
            filtroDataFim.setValue(null);
            filtroDataInicio.setValue(null);
        } else if (!tempo.equals("Todos os dias")) {
            LocalDate now = LocalDate.now();
            listaPedidos.setAll(InterfaceBancoDados.getFiltroDatas(now.minusDays(mapDay.get(tempo)), now));
            tituloTabela.setText("Histórico dos pedidos dos " + tempo);
            periodoTempo.setValue(periodoOpcoes.getLast());
        } else if (!nome.isEmpty()) {
            listaPedidos.setAll(InterfaceBancoDados.getFiltroNome(nome));
            tituloTabela.setText("Histórico dos pedidos filtrado por nome: " + nome);
            filtroNome.setText("");
        } else if (!cpf.isEmpty()) {
            listaPedidos.setAll(InterfaceBancoDados.getFiltroCpf(cpf));
            tituloTabela.setText("Histórico dos pedidos filtrado por CPF/Cnpj: " + cpf);
            filtroCpf.setText("");
        } else {
            listaPedidos.setAll(InterfaceBancoDados.getUltimos_X_pedidos(50));
            tituloTabela.setText("Histórico dos ultimos pedidos");
        }
        showEstatisticas();
    }

    public void showEstatisticas() {
        double[] listTotalCustos = listaPedidos.stream()
                .filter(pedido -> pedido.getCustos() != null && pedido.getCustos() != 0)
                .mapToDouble(Pedido::getCustos).toArray();
        double[] listTotalPago = listaPedidos.stream()
                .filter(pedido -> pedido.getValorPago() != null && pedido.getValorPago() != 0)
                .mapToDouble(Pedido::getValorPago).toArray();

        double totalCustos = DoubleStream.of(listTotalCustos).sum();
        double totalPago = DoubleStream.of(listTotalPago).sum();

        double totalLucro = totalPago - totalCustos;
        int quantPedido = listaPedidos.size();

        String textoValorTotal = "Total R$: " + String.format("%.2f", totalPago) + " / Médio R$: "
                + String.format("%.2f", (totalPago / listTotalPago.length));
        String textoCustosTotal = "Total R$: " + String.format("%.2f", totalCustos) + " / Médio R$: "
                + String.format("%.2f", (totalCustos / listTotalCustos.length));
        String textoLucroTotal = "Total R$: " + String.format("%.2f", totalLucro) + " / Médio R$: "
                + String.format("%.2f", (totalLucro / quantPedido));

        System.out.println(textoValorTotal);
        System.out.println(textoCustosTotal);
        System.out.println(textoLucroTotal);
        System.out.println(quantPedido);

        labelValorTotal.setText(textoValorTotal);
        labelCustosTotal.setText(textoCustosTotal);
        labelLucroTotal.setText(textoLucroTotal);
        labelQuantidade.setText("" + quantPedido);

    }

    @FXML
    void limparFiltroFunc(ActionEvent event) {
        filtroDataFim.setValue(null);
        filtroDataInicio.setValue(null);
        filtroNome.setText("");
        filtroCpf.setText("");
        periodoTempo.setValue(periodoOpcoes.getLast());
    }

    @FXML
    void voltarMenuFunc(ActionEvent event) {
        // atualiza a tabela do menu
        InterfaceBancoDados.startDataPedido();
        URL janela = getClass().getResource("interfaces/Menu.fxml");
        String janelaName = "Menu";
        switchWindow(event, janela, janelaName);
    }
}
