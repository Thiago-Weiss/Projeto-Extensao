import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
// purgatorio
public class ControllerPedido extends Controller_Pai_Pedidos {

    private Pedido pedido;
    private Stage janelaAnterior;
    private ObservableList<Pedido> listaPedidos;

    @FXML
    private CheckBox botaoFinalizado;

    @FXML
    private TextField campoCpf;

    @FXML
    private TextField campoCustos;

    @FXML
    private DatePicker campoData;

    @FXML
    private TextArea campoDescricao;

    @FXML
    private TextField campoEndereco;

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoPago;

    @FXML
    private TextField campoTelefone;

    @FXML
    private TextField campoTotal;

    @FXML
    private Label textFaltaPagar;

    @FXML
    private Button botaoApagar;

    @FXML
    private Button botaoCancelar;

    @FXML
    private Button botaoConfirmar;

    public void initialize(Pedido pedido, Stage urlJanela, ObservableList<Pedido> listaPedidos) {
        // recebe e salva o objeto pedido e preenche os campos do pedido
        this.pedido = pedido;
        this.janelaAnterior = urlJanela;
        this.listaPedidos = listaPedidos;
        setCampos();

        // formata todos os campos
        setUpCamposDoubles(campoTotal, campoPago, textFaltaPagar, campoCustos);
        setLimitCaracters(campoNome, 50);
        setLimitCaracters(campoCpf, 20);
        setLimitCaracters(campoTelefone, 20);
        setLimitCaracters(campoEndereco, 90);
        setLimitCaracters(campoDescricao, 1000);
    }

    private void setCampos() {
        campoNome.setText(pedido.getNomeCliente());
        campoCpf.setText(pedido.getCpfCliente());
        campoTelefone.setText(pedido.getTelefoneCliente());
        campoEndereco.setText(pedido.getEnderecoEntrega());
        campoData.setValue(pedido.getDataEntrega());
        campoTotal.setText("R$: " + (pedido.getPrecoPedido() != null ? pedido.getPrecoPedido() : ""));
        campoPago.setText("R$: " + (pedido.getValorPago() != null ? pedido.getValorPago() : ""));
        campoCustos.setText("R$: " + (pedido.getCustos() != null ? pedido.getCustos() : ""));
        campoDescricao.setText(pedido.getDescricaoPedido());
        botaoFinalizado.setSelected(pedido.getEstadoPedido());
    }

    @Override
    protected void voltarMenu(ActionEvent event) {
        if (janelaAnterior == null) {
            URL janela = getClass().getResource("interfaces/Menu.fxml");
            String janelaName = "Menu";
            switchWindow(event, janela, janelaName);
        } else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            janelaAnterior.show();
        }
    }

    @FXML
    void cancelarFunc(ActionEvent event) {
        voltarMenu(event);
    }

    @FXML
    void salvarPedidoFunc(ActionEvent event) {
        // pega todos os dados dos campos
        String cpfCliente = getCampoText(campoCpf);
        String descricaoPedido = getCampoText(campoDescricao);
        String enderecoEntrega = getCampoText(campoEndereco);
        String nomeCliente = getCampoText(campoNome);
        String telefoneCliente = getCampoText(campoTelefone);
        boolean pedidoFinalizado = botaoFinalizado.isSelected();
        LocalDate dataEntrega = campoData.getValue();
        Double precoPedido = getCampoInDouble(campoTotal);
        Double valorPago = getCampoInDouble(campoPago);
        Double custos = getCampoInDouble(campoCustos);

        // verifica se os campos obrigatórios estão
        boolean tudoCerto = true;
        if (checagemCampos(descricaoPedido, campoDescricao)) {
            tudoCerto = false;
        }
        if (checagemCampos(enderecoEntrega, campoEndereco)) {
            tudoCerto = false;
        }
        if (checagemCampos(nomeCliente, campoNome)) {
            tudoCerto = false;
        }
        if (checagemCampos(telefoneCliente, campoTelefone)) {
            tudoCerto = false;
        }
        if (checagemCampos(dataEntrega, campoData)) {
            tudoCerto = false;
        }


        if (tudoCerto) {
            try {
                if (!descricaoPedido.equals(pedido.getDescricaoPedido())) {
                    pedido.setDescricaoPedido(descricaoPedido);
                }
                if (!enderecoEntrega.equals(pedido.getEnderecoEntrega())) {
                    pedido.setEnderecoEntrega(enderecoEntrega);
                }
                if (!nomeCliente.equals(pedido.getNomeCliente())) {
                    pedido.setNomeCliente(nomeCliente);
                }
                if (!telefoneCliente.equals(pedido.getTelefoneCliente())) {
                    pedido.setTelefoneCliente(telefoneCliente);
                }
                if (!dataEntrega.equals(pedido.getDataEntrega())) {
                    pedido.setDataEntrega(dataEntrega);
                }

                if (cpfCliente != null) {
                    if (!cpfCliente.equals(pedido.getCpfCliente())) {
                        pedido.setCpfCliente(cpfCliente);
                    }// purgatorio
                } else if (pedido.getCpfCliente() != null) {
                    pedido.setCpfCliente(cpfCliente);
                }

                if (precoPedido != null) {
                    if (!precoPedido.equals(pedido.getPrecoPedido())) {
                        pedido.setPrecoPedido(precoPedido);
                    }
                } else if (pedido.getPrecoPedido() != null) {
                    pedido.setPrecoPedido(precoPedido);
                }

                if (valorPago != null) {
                    if (!valorPago.equals(pedido.getValorPago())) {
                        pedido.setValorPago(valorPago);
                    }
                } else if (pedido.getValorPago() != null) {
                    pedido.setValorPago(valorPago);
                }

                if (custos != null) {
                    if (!custos.equals(pedido.getCustos())) {
                        pedido.setCustos(custos);
                    }
                } else if (pedido.getCustos() != null) {
                    pedido.setCustos(custos);
                }
                if (pedidoFinalizado != pedido.getEstadoPedido()) {
                    if (pedidoFinalizado) {
                        Data.attPedidoFinalizado(pedido);
                    } else {
                        pedido.setPedidoAberto();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("\nErro ao atualizar os dados do pedido, O DADOS NAO FORAM ATUALIZADOS");
                System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
            }
            voltarMenu(event);

        }
    }

    @FXML
    void excluirPedidoFunc(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText(null);
        alert.setContentText("Você quer excluir o pedido ?\n" + "Ele será excluído permanentemente !!!");

        // Personalizar os botões da caixa de diálogo
        ButtonType buttonSim = new ButtonType("Sim");
        ButtonType buttonNao = new ButtonType("Não");
        alert.getButtonTypes().setAll(buttonSim, buttonNao);

        // Mostrar a caixa de diálogo e pegar a resposta do usuário
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonSim) {
            try {
                if (listaPedidos != null) {
                    listaPedidos.remove(pedido);
                }
                Data.excluirPedido(pedido);
            } catch (SQLException e) {// purgatorio
                e.printStackTrace();
                System.out.println("\nErro ao excluir o pedido, O PEDIDO NAO FOI EXCLUIDO");
                System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
            }
            voltarMenu(event);
        }
    }
}
