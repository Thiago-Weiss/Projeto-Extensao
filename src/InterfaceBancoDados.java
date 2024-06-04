import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InterfaceBancoDados {
    // instacia somente um objeto da class
    private static InterfaceBancoDados Singleton;
    private static String url;
    private static String user;
    private static String password;

    private InterfaceBancoDados(String url, String user, String password) {
        InterfaceBancoDados.url = url;
        InterfaceBancoDados.password = password;
        InterfaceBancoDados.user = user;
    }

    public static InterfaceBancoDados getInstance(String url, String user, String password) {
        if (Singleton == null) {
            Singleton = new InterfaceBancoDados(url, user, password);
        }
        return Singleton;
    }

    private static Connection conecao_BD() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("\nDriver JDBC não encontrado.");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\nErro ao conectar ao banco de dados.");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }
        return connection;
    }

    private static boolean fecharConexao_BD(Connection connection) {
        try {
            if (connection != null)
                connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\nerro ao fechar a conecao com o banco de dados");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }
        return false;
    }

    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////
    /////////////////////// PEDIDO ///////////////////////

    public static Pedido criarPedido(String cpfCliente, String descricaoPedido, String enderecoEntrega,
            boolean pedidoFinalizado, String nomeCliente, String telefoneCliente, LocalDate dataEntrega,
            Double precoPedido, Double valorPago, Double custos) throws SQLException {

        Connection conexao = conecao_BD();

        String tabela_cliente = "INSERT INTO cliente (cpf_cnpj, nome_cliente, telefone_cliente) VALUES (?, ?, ?) RETURNING id_cliente";
        PreparedStatement statement_cliente = conexao.prepareStatement(tabela_cliente);
        statement_cliente.setString(1, cpfCliente);
        statement_cliente.setString(2, nomeCliente);
        statement_cliente.setString(3, telefoneCliente);

        ResultSet resultSet = statement_cliente.executeQuery();

        resultSet.next();
        int id_tabela_cliente = resultSet.getInt("id_cliente");

        String tabela_pedido = "INSERT INTO pedido (pedido_finalizado, id_cliente_chave, data_criacao_pedido, custos, data_entrega, valor_pago, endereco_entrega, descricao_pedido, preco_pedido) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_pedido";
        PreparedStatement statement_pedido = conexao.prepareStatement(tabela_pedido);

        statement_pedido.setBoolean(1, pedidoFinalizado);
        statement_pedido.setInt(2, id_tabela_cliente);
        statement_pedido.setDate(3, Date.valueOf(LocalDate.now()));
        statement_pedido.setObject(4, custos); // Usa setObject para permitir null
        statement_pedido.setDate(5, Date.valueOf(dataEntrega));
        statement_pedido.setObject(6, valorPago); // Usa setObject para permitir null
        statement_pedido.setString(7, enderecoEntrega);
        statement_pedido.setString(8, descricaoPedido);
        statement_pedido.setObject(9, precoPedido);

        ResultSet resultSeta = statement_pedido.executeQuery();
        resultSeta.next();
        int id_pedido = resultSeta.getInt("id_pedido");

        fecharConexao_BD(conexao);

        Pedido pedido = new Pedido(id_pedido, cpfCliente, descricaoPedido, enderecoEntrega, pedidoFinalizado,
                nomeCliente, telefoneCliente, dataEntrega, dataEntrega, precoPedido, valorPago, custos);

        return pedido;
    }

    public static void excluirPedido(int idPedido) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setInt(1, idPedido);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void startDataPedido() {
        Connection conexao = conecao_BD();
        List<Pedido> listaPedidos = new ArrayList<>();

        String sql = "SELECT p.*, c.* FROM pedido p JOIN cliente c ON p.id_cliente_chave = c.id_cliente WHERE p.pedido_finalizado = ? ORDER BY p.data_entrega LIMIT 30";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setBoolean(1, false);

            ResultSet resultSet = statement.executeQuery();
            listaPedidos = createPedido(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao pegar a lista de pedidos abertos");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }

        fecharConexao_BD(conexao);

        Data.setListaPedidos(listaPedidos);
    }

    private static List<Pedido> createPedido(ResultSet resultSet) throws SQLException {
        List<Pedido> listaPedidos = new ArrayList<>();
        while (resultSet.next()) {
            // Obtém os valores das colunas
            int idPedido = resultSet.getInt("id_pedido");
            boolean pedidoFinalizado = resultSet.getBoolean("pedido_finalizado");
            LocalDate dataCriacao = resultSet.getDate("data_criacao_pedido").toLocalDate();
            Double custos = resultSet.getDouble("custos");
            LocalDate dataEntrega = resultSet.getDate("data_entrega").toLocalDate();
            Double valorPago = resultSet.getDouble("valor_pago");
            Double precoPedido = resultSet.getDouble("preco_pedido");
            String enderecoEntrega = resultSet.getString("endereco_entrega").trim();
            String descricaoPedido = resultSet.getString("descricao_pedido").trim();
            String nomeCliente = resultSet.getString("nome_cliente").trim();
            String telefoneCliente = resultSet.getString("telefone_cliente").trim();
            String cpfCnpjCliente = resultSet.getString("cpf_cnpj");
            cpfCnpjCliente = cpfCnpjCliente != null ? cpfCnpjCliente.trim() : null;

            Pedido pedido = new Pedido(idPedido, cpfCnpjCliente, descricaoPedido, enderecoEntrega, pedidoFinalizado,
                    nomeCliente, telefoneCliente, dataCriacao, dataEntrega, precoPedido, valorPago, custos);

            listaPedidos.add(pedido);
        }
        return listaPedidos;
    }

    ////////////////////////// SETTERS //////////////////////////
    ////////////////////////// SETTERS //////////////////////////
    ////////////////////////// SETTERS //////////////////////////
    ////////////////////////// SETTERS //////////////////////////
    ////////////////////////// SETTERS //////////////////////////
    ////////////////////////// SETTERS //////////////////////////

    public static void attValorPago(int id, Double valor) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "UPDATE pedido SET valor_pago = ? WHERE id_pedido = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);

        if (valor == null) {
            statement.setNull(1, Types.DOUBLE);
        } else {
            statement.setDouble(1, valor);
        }
        statement.setInt(2, id);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void attEstadoPedido(int id, boolean valor) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "UPDATE pedido SET pedido_finalizado = ? WHERE id_pedido = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setBoolean(1, valor);
        statement.setInt(2, id);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void attDataEntrega(int id, LocalDate valor) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "UPDATE pedido SET data_entrega = ? WHERE id_pedido = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setDate(1, Date.valueOf(valor));
        statement.setInt(2, id);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void attCustos(int id, Double valor) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "UPDATE pedido SET custos = ? WHERE id_pedido = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);

        if (valor == null) {
            statement.setNull(1, Types.DOUBLE);
        } else {
            statement.setDouble(1, valor);
        }
        statement.setInt(2, id);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void attPrecoPedido(int id, Double valor) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "UPDATE pedido SET preco_pedido = ? WHERE id_pedido = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);

        if (valor == null) {
            statement.setNull(1, Types.DOUBLE);
        } else {
            statement.setDouble(1, valor);
        }
        statement.setInt(2, id);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void attDescricao(int id, String valor) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "UPDATE pedido SET descricao_pedido = ? WHERE id_pedido = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setString(1, valor);
        statement.setInt(2, id);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void attEnderecoEntrega(int id, String valor) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "UPDATE pedido SET endereco_entrega = ? WHERE id_pedido = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setString(1, valor);
        statement.setInt(2, id);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    private static int getIdTabelaCliente(Connection conexao, int idPedido) throws SQLException {

        String sql = "SELECT id_cliente_chave FROM pedido WHERE id_pedido = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setInt(1, idPedido);

        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        int idCliente = resultSet.getInt("id_cliente_chave");

        return idCliente;
    }

    public static void attCpf(int idPedido, String valor) throws SQLException {
        Connection conexao = conecao_BD();

        int id_tabela_cliente = getIdTabelaCliente(conexao, idPedido);

        String sql = "UPDATE cliente SET cpf_cnpj = ? WHERE id_cliente = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setString(1, valor);
        statement.setInt(2, id_tabela_cliente);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void attNomeCliente(int idPedido, String valor) throws SQLException {
        Connection conexao = conecao_BD();

        int id_tabela_cliente = getIdTabelaCliente(conexao, idPedido);

        String sql = "UPDATE cliente SET nome_cliente = ? WHERE id_cliente = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setString(1, valor);
        statement.setInt(2, id_tabela_cliente);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void attTelefoneCliente(int idPedido, String valor) throws SQLException {
        Connection conexao = conecao_BD();

        int id_tabela_cliente = getIdTabelaCliente(conexao, idPedido);

        String sql = "UPDATE cliente SET telefone_cliente = ? WHERE id_cliente = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setString(1, valor);
        statement.setInt(2, id_tabela_cliente);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    /////////////////////// TASK ///////////////////////
    public static int criarTarefa(String text, String cor, String dia) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "INSERT INTO tarefa (texto, cor, diasemana) VALUES (?, ?, ?) RETURNING id_tarefa";

        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setString(1, text);
        statement.setString(2, cor);
        statement.setString(3, dia);

        ResultSet resultSet = statement.executeQuery();

        resultSet.next();
        int id_tarefa = resultSet.getInt("id_tarefa");

        fecharConexao_BD(conexao);
        return id_tarefa;
    }

    public static void startDataTask() {
        Connection conexao = conecao_BD();

        String sql = "SELECT id_tarefa, texto, cor, diasemana FROM tarefa;";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idTabela = resultSet.getInt("id_tarefa");
                String texto = resultSet.getString("texto");
                String cor = resultSet.getString("cor");
                String diaSemana = resultSet.getString("diasemana");
                TaskWeek task = new TaskWeek(idTabela, texto, cor, diaSemana);
                Data.addTask(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao pegar as tarefas do banco de dados");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }
        fecharConexao_BD(conexao);
    }

    public static void excluirTarefa(int idTarefa) throws SQLException {
        Connection conexao = conecao_BD();

        String sql = "DELETE FROM tarefa WHERE id_tarefa = ?";
        PreparedStatement statement = conexao.prepareStatement(sql);
        statement.setInt(1, idTarefa);

        statement.executeUpdate();

        fecharConexao_BD(conexao);
    }

    public static void attTarefaDiasemana(int idTarefa, String valor) {
        Connection conexao = conecao_BD();

        String sql = "UPDATE tarefa SET diasemana = ? WHERE id_tarefa = ?";
        try {
            PreparedStatement statement;
            statement = conexao.prepareStatement(sql);
            statement.setString(1, valor);
            statement.setInt(2, idTarefa);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar o dia da tarefa no banco de dados");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }
        fecharConexao_BD(conexao);
    }

    /////////////////////// FINANCA ///////////////////////
    /////////////////////// FINANCA ///////////////////////
    /////////////////////// FINANCA ///////////////////////
    /////////////////////// FINANCA ///////////////////////
    /////////////////////// FINANCA ///////////////////////
    /////////////////////// FINANCA ///////////////////////

    public static List<Pedido> getUltimos_X_pedidos(int quant_x) {
        Connection conexao = conecao_BD();
        List<Pedido> listaPedidos = new ArrayList<>();

        String sql = "SELECT p.*, c.* FROM pedido p JOIN cliente c ON p.id_cliente_chave = c.id_cliente ORDER BY p.data_entrega DESC LIMIT ?";

        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setInt(1, quant_x);

            ResultSet resultSet = statement.executeQuery();
            listaPedidos = createPedido(resultSet);
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao pegar a lista dos ultimos pedidos");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }

        fecharConexao_BD(conexao);
        return listaPedidos;
    }

    public static List<Pedido> getFiltroNome(String nome, int limite) {
        Connection conexao = conecao_BD();
        List<Pedido> listaPedidos = new ArrayList<>();

        String sql = "SELECT p.*, c.* FROM pedido p JOIN cliente c ON p.id_cliente_chave = c.id_cliente WHERE LOWER(c.nome_cliente) ILIKE ? LIMIT ?";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setString(1, "%" + nome.toLowerCase().trim() + "%");
            statement.setInt(2, limite);

            ResultSet resultSet = statement.executeQuery();
            listaPedidos = createPedido(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao filtrar os pedidos pelo nome");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }

        fecharConexao_BD(conexao);
        return listaPedidos;
    }

    public static List<Pedido> getFiltroCpf(String cpf, int limite) {
        Connection conexao = conecao_BD();
        List<Pedido> listaPedidos = new ArrayList<>();

        String sql = "SELECT p.*, c.* FROM pedido p JOIN cliente c ON p.id_cliente_chave = c.id_cliente WHERE LOWER(REPLACE(c.cpf_cnpj, '.', '')) ILIKE ? LIMIT ?";

        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setString(1, "%" + cpf.toLowerCase().trim() + "%");
            statement.setInt(2, limite);

            ResultSet resultSet = statement.executeQuery();
            listaPedidos = createPedido(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao filtrar os pedidos pelo cpf");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }

        fecharConexao_BD(conexao);
        return listaPedidos;
    }

    public static List<Pedido> getFiltroDatas(LocalDate inicio, LocalDate fim, int limite) {
        Connection conexao = conecao_BD();
        List<Pedido> listaPedidos = new ArrayList<>();

        String sql = "SELECT p.*, c.* FROM pedido p JOIN cliente c ON p.id_cliente_chave = c.id_cliente WHERE p.data_entrega BETWEEN ? AND ? LIMIT ?";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(inicio));
            statement.setDate(2, java.sql.Date.valueOf(fim));
            statement.setInt(3, limite);

            ResultSet resultSet = statement.executeQuery();
            listaPedidos = createPedido(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao filtrar os pedidos pelo cpf");
            System.out.println("Contate o suporte e detalhe o que foi feito na hora do erro");
        }

        fecharConexao_BD(conexao);
        return listaPedidos;
    }

}
