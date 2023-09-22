package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

/**
 * Pequeno CRUD.
 */
public class ContaDAO {

    public static void salvar (DadosAberturaConta dadosDaConta, ConnectionFactory con) {

        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente, Boolean.TRUE);

        final String sqlInsert = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email, esta_ativa) " +
        "VALUES (?,?,?,?,?,?)";

        try (Connection conexao = con.recuperarConexao()) {

            PreparedStatement pstt = conexao.prepareStatement(sqlInsert);
            pstt.setInt(1, conta.getNumero());
            pstt.setBigDecimal(2, BigDecimal.ZERO);
            pstt.setString(3, dadosDaConta.dadosCliente().nome());
            pstt.setString(4, dadosDaConta.dadosCliente().cpf());
            pstt.setString(5, dadosDaConta.dadosCliente().email());
            pstt.setBoolean(6, Boolean.TRUE);
            pstt.execute();
            pstt.close();
            conexao.close();
            
        } catch (Exception e) {
           
            throw new RuntimeException(e);
            
        }

    }

    public static Set<Conta> listar (ConnectionFactory con) {

        Set<Conta> contas = new HashSet<Conta>();

        /*select * from contas c inner join usuario u
        on c.usuario_id = u.id
        where u.status = ‘Ativa’ */

        final String sqlSelect = "SELECT * FROM conta";

        try (Connection conexao = con.recuperarConexao()) {

            PreparedStatement pstt = conexao.prepareStatement(sqlSelect);
            ResultSet resultado = pstt.executeQuery();
            
            while (resultado.next()) {

                Integer numero = resultado.getInt(1);
                BigDecimal saldo = resultado.getBigDecimal(2);
                String nome = resultado.getString(3);
                String cpf = resultado.getString(4);
                String email = resultado.getString(5);
                Boolean estaAtiva = resultado.getBoolean(6);
                contas.add(new Conta(numero, new Cliente(new DadosCadastroCliente(nome, cpf, email)), saldo, estaAtiva));

            }

            resultado.close();
            pstt.close();
            conexao.close();

        } catch (Exception e) {

            System.err.println(e.getMessage());

        }

        return contas;

    }

    public static Conta listarPorNumero (ConnectionFactory con, Integer num) {

        Conta conta = null;
        Set<Conta> contas = new HashSet<Conta>();

        final String sqlSelect = "SELECT * FROM conta WHERE numero = ?";

        try (Connection conexao = con.recuperarConexao()) {

            PreparedStatement pstt = conexao.prepareStatement(sqlSelect);
            pstt.setInt(1, num);

            ResultSet resultado = pstt.executeQuery();
            
            while (resultado.next()) {

                BigDecimal saldo = resultado.getBigDecimal(2);
                String nome = resultado.getString(3);
                String cpf = resultado.getString(4);
                String email = resultado.getString(5);
                Boolean estaAtiva = resultado.getBoolean(6);
                contas.add(new Conta(num, new Cliente(new DadosCadastroCliente(nome, cpf, email)), saldo, estaAtiva));

            }

            resultado.close();
            pstt.close();
            conexao.close();

        } catch (Exception e) {

            System.err.println(e.getMessage());

        }

        if (contas.size() == 0) return null;

        Optional<Conta> contaOptional = contas.stream().filter(c -> c.getNumero() == num)
        .findFirst();

        if (contaOptional.isPresent()) conta = contaOptional.get();

        return conta;

    }

    public static void depositar (ConnectionFactory con, Integer num, BigDecimal valor) {

        final String sqlUpdate = "UPDATE conta SET saldo = saldo + ? WHERE numero = ?";

        try (Connection conexao = con.recuperarConexao()) {

            PreparedStatement pstt = conexao.prepareStatement(sqlUpdate);
            pstt.setBigDecimal(1, valor);
            pstt.setInt(2, num);
            pstt.execute();
            pstt.close();
            conexao.close();

        } catch (Exception e) {

            System.err.println(e.getMessage());

        }


    }

    public static void sacar (ConnectionFactory con, Integer num, BigDecimal valor) {

        final String sqlUpdate = "UPDATE conta SET saldo = saldo - ? WHERE numero = ?";

        try (Connection conexao = con.recuperarConexao()) {

            PreparedStatement pstt = conexao.prepareStatement(sqlUpdate);
            pstt.setBigDecimal(1, valor);
            pstt.setInt(2, num);
            pstt.execute();
            pstt.close();
            conexao.close();

        } catch (Exception e) {

            System.err.println(e.getMessage());

        }

    }

    public static void transferir (ConnectionFactory con, Integer numContaDebito, Integer numContaCredito, BigDecimal valor) {

        final String sqlBegin = "START TRANSACTION";
        final String sqlAbort = "ROLLBACK";
        final String sqlCommit = "COMMIT";
        final String sqlUpdateDebito = "UPDATE conta SET saldo = saldo - ? WHERE numero = ?";
        final String sqlUpdateCredito = "UPDATE conta SET saldo = saldo + ? WHERE numero = ?";
        Connection conexao;

        try {

            conexao = con.recuperarConexao();

        } catch (Exception e) {

            System.err.println(e.getMessage());
            return;

        }

        PreparedStatement pstt;

        try {

            pstt = conexao.prepareStatement(sqlBegin);
            pstt.execute();

            pstt = conexao.prepareStatement(sqlUpdateDebito);
            pstt.setBigDecimal(1, valor);
            pstt.setInt(2, numContaDebito);
            pstt.execute();

            pstt = conexao.prepareStatement(sqlUpdateCredito);
            pstt.setBigDecimal(1, valor);
            pstt.setInt(2, numContaCredito);
            pstt.execute();

            pstt = conexao.prepareStatement(sqlCommit);
            pstt.execute();

            conexao.close();
            pstt.close();

            
        } catch (Exception e) {
            
            System.err.println(e.getMessage());

            try {

                pstt = conexao.prepareStatement(sqlAbort);
                pstt.execute();
                
            } catch (Exception exception) {
               
                System.err.println(exception.getMessage());
                
            }

        }

    }

    public static void deletar (ConnectionFactory con, Integer numConta) {

        final String sqlDelete = "DELETE FROM conta WHERE numero = ?";

        try (Connection conexao = con.recuperarConexao()) {

            PreparedStatement pstt = conexao.prepareStatement(sqlDelete);
            pstt.setInt(1, numConta);
            pstt.execute();
            pstt.close();
            conexao.close();

        } catch (Exception e) {
        
            System.err.println(e.getMessage());

        }

    }

    public static void desativar (ConnectionFactory con, Integer numConta) {

        final String sqlDelete = "UPDATE conta SET esta_ativa = FALSE WHERE numero = ?";

        try (Connection conexao = con.recuperarConexao()) {

            PreparedStatement pstt = conexao.prepareStatement(sqlDelete);
            pstt.setInt(1, numConta);
            pstt.execute();
            pstt.close();
            conexao.close();

        } catch (Exception e) {
        
            System.err.println(e.getMessage());

        }

    }

    public static void reativar (ConnectionFactory con, Integer numConta) {

        final String sqlDelete = "UPDATE conta SET esta_ativa = TRUE WHERE numero = ?";

        try (Connection conexao = con.recuperarConexao()) {

            PreparedStatement pstt = conexao.prepareStatement(sqlDelete);
            pstt.setInt(1, numConta);
            pstt.execute();
            pstt.close();
            conexao.close();

        } catch (Exception e) {
        
            System.err.println(e.getMessage());

        }

    }
    
}
