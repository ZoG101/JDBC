package br.com.alura.bytebank;

import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;
import br.com.alura.bytebank.domain.conta.ContaService;
import br.com.alura.bytebank.domain.conta.DadosAberturaConta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

public class BytebankApplication {

    private static ContaService service = new ContaService();
    private static Scanner teclado = new Scanner(System.in, Charset.defaultCharset());

    public static void main(String[] args) {
        var opcao = exibirMenu();
        while (opcao != 10) {
            try {
                switch (opcao) {
                    case 1:
                        listarContas();
                        break;
                    case 2:
                        abrirConta();
                        break;
                    case 3:
                        encerrarConta();
                        break;
                    case 4:
                        consultarSaldo();
                        break;
                    case 5:
                        realizarSaque();
                        break;
                    case 6:
                        realizarDeposito();
                        break;
                    case 7:
                        realizarTransferencia();
                        break;
                    case 8:
                        desativarConta();
                        break;
                    case 9:
                        reativarConta();
                        break;
                }
            } catch (RegraDeNegocioException e) {

                System.out.println("Erro: " +e.getMessage());
                System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu");
                InputStream fis = System.in;
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String sair = "";

                try {
                    
                    sair = br.readLine();

                } catch (Exception exception) {
                
                    System.err.println(exception.getMessage());
                    
                }

                if (sair.isBlank()) opcao = 0;
            }

            opcao = exibirMenu();

        }

        System.out.println("Finalizando a aplicação.");
    }

    private static int exibirMenu() {
        System.out.println("""
                BYTEBANK - ESCOLHA UMA OPÇÃO:
                1 - Listar contas abertas
                2 - Abertura de conta
                3 - Encerramento de conta
                4 - Consultar saldo de uma conta
                5 - Realizar saque em uma conta
                6 - Realizar depósito em uma conta
                7 - Realizar transferência
                8 - desativar conta
                9 - reativar conta
                10 - Sair
                """);
        return teclado.nextInt();
    }

    private static void listarContas() {
        System.out.println("Contas cadastradas:");
        var contas = service.listarContasAbertas();
        contas.stream().forEach(System.out::println);

        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        InputStream fis = System.in;
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String sair = "";

        try {
            
            sair = br.readLine();

        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        if (sair.isBlank()) return;
    }

    private static void abrirConta() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o nome do cliente:");
        var nome = teclado.next() + teclado.nextLine();

        System.out.println("Digite o cpf do cliente:");
        var cpf = teclado.next();

        System.out.println("Digite o email do cliente:");
        var email = teclado.next();

        try {
         
            service.abrir(new DadosAberturaConta(numeroDaConta, new DadosCadastroCliente(nome, cpf, email)));
            
        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        System.out.println("Conta aberta com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        InputStream fis = System.in;
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String sair = "";

        try {
            
            sair = br.readLine();

        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        if (sair.isBlank()) return;

    }

    private static void encerrarConta() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        service.encerrar(numeroDaConta);

        System.out.println("Conta encerrada com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        InputStream fis = System.in;
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String sair = "";

        try {
            
            sair = br.readLine();

        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        if (sair.isBlank()) return;
    }

    private static void desativarConta() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        service.desativar(numeroDaConta);

        System.out.println("Conta desativada com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        InputStream fis = System.in;
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String sair = "";

        try {
            
            sair = br.readLine();

        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        if (sair.isBlank()) return;
    }

    private static void reativarConta() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        service.reativar(numeroDaConta);

        System.out.println("Conta reativada com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        InputStream fis = System.in;
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String sair = "";

        try {
            
            sair = br.readLine();

        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        if (sair.isBlank()) return;
    }

    private static void consultarSaldo() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();
        var saldo = service.consultarSaldo(numeroDaConta);
        System.out.println("Saldo da conta: " +saldo);

        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        InputStream fis = System.in;
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String sair = "";

        try {
            
            sair = br.readLine();

        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        if (sair.isBlank()) return;
    }

    private static void realizarSaque() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o valor do saque:");
        var valor = teclado.nextBigDecimal();

        service.realizarSaque(numeroDaConta, valor);
        System.out.println("Saque realizado com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        InputStream fis = System.in;
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String sair = "";

        try {
            
            sair = br.readLine();

        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        if (sair.isBlank()) return;
    }

    private static void realizarDeposito() {
        System.out.println("Digite o número da conta:");
        var numeroDaConta = teclado.nextInt();

        System.out.println("Digite o valor do depósito:");
        var valor = teclado.nextBigDecimal();

        service.realizarDeposito(numeroDaConta, valor);

        System.out.println("Depósito realizado com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        InputStream fis = System.in;
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String sair = "";

        try {
            
            sair = br.readLine();

        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        if (sair.isBlank()) return;
    }

    private static void realizarTransferencia() {

        System.out.println("Digite o número da conta de debito:");
        var numeroDaContaDebito = teclado.nextInt();

        System.out.println("Digite o número da conta de crédito:");
        var numeroDaContaCredito = teclado.nextInt();

        System.out.println("Digite o valor do depósito:");
        var valor = teclado.nextBigDecimal();

        service.realizaTransferencia(numeroDaContaDebito, numeroDaContaCredito, valor);

        System.out.println("Transferência realizada com sucesso!");
        System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
        InputStream fis = System.in;
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String sair = "";

        try {
            
            sair = br.readLine();

        } catch (Exception e) {
           
            System.err.println(e.getMessage());
            
        }

        if (sair.isBlank()) return;

    }

}
