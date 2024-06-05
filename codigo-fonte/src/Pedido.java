import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Pedido {
    // Atributos do pedido
    private int idPedido;
    private String cpfCliente;
    private String descricaoPedido;
    private String enderecoEntrega;
    private String nomeCliente;
    private String telefoneCliente;
    private boolean pedidoFinalizado;
    private LocalDate dataCriacao;
    private LocalDate dataEntrega;
    private Double precoPedido;
    private Double valorPago;
    private Double custos;

    // Construtor
    public Pedido(int idPedido, String cpfCliente, String descricaoPedido, String enderecoEntrega,
            boolean pedidoFinalizado, String nomeCliente, String telefoneCliente,
            LocalDate dataCriacao, LocalDate dataEntrega, Double precoPedido,
            Double valorPago, Double custos) {
        this.idPedido = idPedido;
        this.cpfCliente = cpfCliente;
        this.descricaoPedido = descricaoPedido;
        this.enderecoEntrega = enderecoEntrega;
        this.pedidoFinalizado = pedidoFinalizado;
        this.nomeCliente = nomeCliente;
        this.telefoneCliente = telefoneCliente;
        this.dataCriacao = dataCriacao;
        this.dataEntrega = dataEntrega;
        this.precoPedido = precoPedido;
        this.valorPago = valorPago;
        this.custos = custos;
    }

    //////////////////////// TABLE TEXT ////////////////////////
    //////////////////////// TABLE TEXT ////////////////////////
    //////////////////////// TABLE TEXT ////////////////////////

    public String getDescricaoPedidoLinha() {
        String[] linhas = descricaoPedido.split("\\r?\\n");
        String primeiraLinha = linhas[0];

        if (primeiraLinha.length() <= 100) {
            return primeiraLinha;
        } else {
            return primeiraLinha.substring(0, 100);
        }
    }

    public String getDescricaoFaltaPagar() {
        if (precoPedido == null) {
            return "Sem Preço";
        } else if (valorPago == null) {
            return "R$: " + precoPedido;
        } else {
            double falta = precoPedido - valorPago;
            if (falta <= 0) {
                return "Pago";
            } else {
                return "R$: " + falta;
            }
        }
    }

    public String getTempoRestante() {
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), dataEntrega);
        if (dias < 0) {
            return "Finalizado ?";
        } else if (dias == 0) {
            return "Hoje";
        } else {
            return "Falta " + Long.toString(dias) + " dias";
        }
    }

    //////////////////////// GETTERS ////////////////////////
    //////////////////////// GETTERS ////////////////////////
    //////////////////////// GETTERS ////////////////////////

    public int getIdPedido() {
        return idPedido;
    }

    public boolean getEstadoPedido() {
        return pedidoFinalizado;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public String getDescricaoPedido() {
        return descricaoPedido;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public boolean isPedidoFinalizado() {
        return pedidoFinalizado;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public Double getPrecoPedido() {
        return precoPedido;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public Double getCustos() {
        return custos;
    }

    //////////////////////// SETTERS ////////////////////////
    //////////////////////// SETTERS ////////////////////////
    //////////////////////// SETTERS ////////////////////////

    public void setCpfCliente(String cpfCliente) throws SQLException {
        this.cpfCliente = cpfCliente;
        InterfaceBancoDados.attCpf(idPedido, cpfCliente);
    }

    public void setDescricaoPedido(String descricaoPedido) throws SQLException {
        this.descricaoPedido = descricaoPedido;
        InterfaceBancoDados.attDescricao(idPedido, descricaoPedido);
    }

    public void setEnderecoEntrega(String enderecoEntrega) throws SQLException {
        this.enderecoEntrega = enderecoEntrega;
        InterfaceBancoDados.attEnderecoEntrega(idPedido, enderecoEntrega);
    }

    public void setPedidoFinalizado() throws SQLException {
        this.pedidoFinalizado = true;
        InterfaceBancoDados.attEstadoPedido(idPedido, true);// purgatorio
    }

    public void setPedidoAberto() throws SQLException {
        this.pedidoFinalizado = false;
        InterfaceBancoDados.attEstadoPedido(idPedido, false);
    }

    public void setNomeCliente(String nomeCliente) throws SQLException {
        this.nomeCliente = nomeCliente;
        InterfaceBancoDados.attNomeCliente(idPedido, nomeCliente);
    }

    public void setTelefoneCliente(String telefoneCliente) throws SQLException {
        this.telefoneCliente = telefoneCliente;
        InterfaceBancoDados.attTelefoneCliente(idPedido, telefoneCliente);
    }

    public void setDataEntrega(LocalDate dataEntrega) throws SQLException {
        this.dataEntrega = dataEntrega;
        InterfaceBancoDados.attDataEntrega(idPedido, dataEntrega);
    }

    public void setPrecoPedido(Double precoPedido) throws SQLException {
        this.precoPedido = precoPedido;
        InterfaceBancoDados.attPrecoPedido(idPedido, precoPedido);
    }

    public void setValorPago(Double valorPago) throws SQLException {
        this.valorPago = valorPago;
        InterfaceBancoDados.attValorPago(idPedido, valorPago);
    }

    public void setCustos(Double custos) throws SQLException {
        this.custos = custos;// purgatorio
        InterfaceBancoDados.attCustos(idPedido, custos);
    }
}