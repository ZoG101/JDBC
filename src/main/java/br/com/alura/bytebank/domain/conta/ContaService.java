package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;

import java.math.BigDecimal;
import java.util.Set;

public class ContaService {

    private ConnectionFactory connection;

    public ContaService () {

        this.connection = new ConnectionFactory();

    }

    public Set<Conta> listarContasAbertas() {
        return ContaDAO.listar(this.connection);
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) {
        
        ContaDAO.salvar(dadosDaConta, this.connection);

    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }
        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }
        if (!conta.getEstaAtiva()) {
            throw new RegraDeNegocioException("A conta não está ativa.");
        }

        ContaDAO.sacar(this.connection, numeroDaConta, valor);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }
        if (!conta.getEstaAtiva()) {
            throw new RegraDeNegocioException("A conta não está ativa.");
        }

        ContaDAO.depositar(this.connection, conta.getNumero(), valor);
    }

    public void desativar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (!conta.getEstaAtiva()) {
            throw new RegraDeNegocioException("A conta já está desativada!");
        }

        ContaDAO.desativar(this.connection, conta.getNumero());
    }

    public void reativar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.getEstaAtiva()) {
            throw new RegraDeNegocioException("A conta já está ativa!");
        }

        ContaDAO.reativar(this.connection, conta.getNumero());
    }

    public void realizaTransferencia (Integer numeroDaContaDebito, Integer numeroDaContaCredito, BigDecimal valor) {

        var contaDebito =  buscarContaPorNumero(numeroDaContaDebito);
        var contaCredito =  buscarContaPorNumero(numeroDaContaCredito);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor da transferência deve ser superior a zero!");
        }
        if (valor.compareTo(contaDebito.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }
        if (contaCredito.getNumero().compareTo(contaDebito.getNumero()) == 0) {
            throw new RegraDeNegocioException("Não é possível transferir um valor para a mesma conta!");
        }
        if ((!contaCredito.getEstaAtiva()) || (!contaDebito.getEstaAtiva())) {
            throw new RegraDeNegocioException("Uma das contas não está ativa.");
        }

        ContaDAO.transferir(this.connection, numeroDaContaDebito, numeroDaContaCredito, valor);

    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        ContaDAO.deletar(this.connection, conta.getNumero());
    }

    private Conta buscarContaPorNumero(Integer numero) {

        Conta conta = ContaDAO.listarPorNumero(this.connection, numero);

        if (conta == null) throw new RegraDeNegocioException("Não existe conta cadastrada com esse número!");
        return conta;

    }
    
}
