import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class ControllerCriarPedido extends Controller_Pai_Pedidos {

    @FXML
    private Button botaoApagar;

    @FXML
    private Button botaoCancelar;

    @FXML
    private Button botaoConfirmar;

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

    public void initialize() {
        // formata todos os campos
        setUpCamposDoubles(campoTotal, campoPago, textFaltaPagar, campoCustos);
        setLimitCaracters(campoNome, 50);
        setLimitCaracters(campoCpf, 20);
        setLimitCaracters(campoTelefone, 20);
        setLimitCaracters(campoEndereco, 90);
        setLimitCaracters(campoDescricao, 1000);
    }// purgatorio

    @FXML
    void cancelarFunc(ActionEvent event) {
        voltarMenu(event);
    }

    @FXML
    void criarPedidoFunc(ActionEvent event) {
        String cpfCliente = getCampoText(campoCpf);
        String descricaoPedido = getCampoText(campoDescricao);
        String enderecoEntrega = getCampoText(campoEndereco);
        String nomeCliente = getCampoText(campoNome);
        String telefoneCliente = getCampoText(campoTelefone);
        Boolean pedidoFinalizado = botaoFinalizado.isSelected();
        LocalDate dataEntrega = campoData.getValue();
        Double precoPedido = getCampoInDouble(campoTotal);
        Double valorPago = getCampoInDouble(campoPago);
        Double custos = getCampoInDouble(campoCustos);

        boolean ok = true;
        if (checagemCampos(descricaoPedido, campoDescricao)) {
            ok = false;
        }
        if (checagemCampos(enderecoEntrega, campoEndereco)) {
            ok = false;
        }
        if (checagemCampos(nomeCliente, campoNome)) {
            ok = false;
        }
        if (checagemCampos(telefoneCliente, campoTelefone)) {
            ok = false;
        }
        if (checagemCampos(dataEntrega, campoData)) {
            ok = false;
        }


  




        if (ok) {
            try {
                if (pedidoFinalizado) {
                    InterfaceBancoDados.criarPedido(cpfCliente, descricaoPedido, enderecoEntrega, pedidoFinalizado,
                            nomeCliente, telefoneCliente, dataEntrega, precoPedido, valorPago, custos);
                } else {
                    Pedido pedido = InterfaceBancoDados.criarPedido(cpfCliente, descricaoPedido, enderecoEntrega,
                            pedidoFinalizado,
                            nomeCliente, telefoneCliente, dataEntrega, precoPedido, valorPago, custos);
                    Data.addPedidoLista(pedido);

                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Confirmado");
                    alerta.setHeaderText(null);
                    alerta.setContentText("Pedido Criado!");
                    alerta.showAndWait();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("\nErro ao criar o pedido, O PEDIDO NAO FOI CRIADO");
                System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
            }
            voltarMenu(event);
        }
    }

}
