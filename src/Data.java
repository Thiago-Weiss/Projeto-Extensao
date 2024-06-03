import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.SQLException;
import java.time.LocalDate;

public class Data {
    /////////////////////// LISTA PEDIDO ///////////////////////
    /////////////////////// LISTA PEDIDO ///////////////////////
    /////////////////////// LISTA PEDIDO ///////////////////////
    /////////////////////// LISTA PEDIDO ///////////////////////
    /////////////////////// LISTA PEDIDO ///////////////////////
    /////////////////////// LISTA PEDIDO ///////////////////////

    private static List<Pedido> listaPedidos = new ArrayList<>();

    public static void setListaPedidos(List<Pedido> list) {
        Data.listaPedidos = list;
    }

    public static List<Pedido> getListaPedidos() {
        return Data.listaPedidos;
    }

    public static void orderListByDate() {
        Collections.sort(listaPedidos, Comparator.comparing(Pedido::getDataEntrega));
    }

    public static List<Pedido> getFiltroData(LocalDate data) {
        return listaPedidos.stream().filter(pedido -> pedido.getDataEntrega().equals(data))
                .collect(Collectors.toList());
    }

    public static List<Pedido> getFiltroNome(String nome) {
        String[] filtro = nome.toLowerCase().split("\\s+");

        return listaPedidos.stream().filter(pedido -> {
            String[] nomes = pedido.getNomeCliente().trim().toLowerCase().split("\\s+");
            for (String n : nomes) {
                for (String f : filtro) {
                    if (n.contains(f)) {
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

    public static List<Pedido> getFiltroCpf(String cpf) {
        return listaPedidos.stream().filter(pedido -> {
            String clienteCpf = pedido.getCpfCliente();
            if (clienteCpf == null) {
                return false;
            }
            if (clienteCpf.toLowerCase().contains(cpf)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    /////////////////////// SET PEDIDO ///////////////////////
    /////////////////////// SET PEDIDO ///////////////////////
    /////////////////////// SET PEDIDO ///////////////////////
    /////////////////////// SET PEDIDO ///////////////////////
    /////////////////////// SET PEDIDO ///////////////////////
    /////////////////////// SET PEDIDO ///////////////////////

    public static void addPedidoLista(Pedido pedido) {
        int index = findInsertionIndex(pedido.getDataEntrega());
        listaPedidos.add(index, pedido);
    }

    private static int findInsertionIndex(LocalDate dataEntrega) {
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (dataEntrega.compareTo(listaPedidos.get(i).getDataEntrega()) <= 0) {
                return i;
            }
        }
        return listaPedidos.size();
    }

    public static void excluirPedido(Pedido pedido) throws SQLException {
        InterfaceBancoDados.excluirPedido(pedido.getIdPedido());
        removerPedidoLista(pedido);
    }

    public static void attPedidoFinalizado(Pedido pedido) throws SQLException {
        pedido.setPedidoFinalizado();
        removerPedidoLista(pedido);
    }

    private static void removerPedidoLista(Pedido pedido) {
        listaPedidos.remove(pedido);
    }

    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////

    private static List<TaskWeek> listaTask = new ArrayList<>();

    public static List<TaskWeek> getListaTask() {
        return listaTask;
    }

    public static void addTask(TaskWeek task) {
        listaTask.add(task);
    }

    public static void criarTask(TaskWeek task) throws SQLException {
        int id = InterfaceBancoDados.criarTarefa(task.getText(), task.getCor(), task.getDia());
        listaTask.add(task);
        task.setIdTabela(id);
    }

    public static void removeTask(TaskWeek task) throws SQLException {
        listaTask.remove(task);
        InterfaceBancoDados.excluirTarefa(task.getIdTabela());
    }
}
