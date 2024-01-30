import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Empresa {
  String cnpj;
  double saldo;
  List<String> taxas;

  public Empresa(String cnpj, double saldo) {
    this.cnpj = cnpj;
    this.saldo = saldo;
    this.taxas = new ArrayList<>();
  }
}

class Cliente {
  String cpf;
  double saldo;
  String cnpjEmpresa;

  public Cliente(String cpf, double saldoCliente, String cnpjEmpresa) {
    this.cpf = cpf;
    this.saldo = saldoCliente;
    this.cnpjEmpresa = cnpjEmpresa;
  }
}

public class SistemaBancario {
  private List<Empresa> empresas;
  private List<Cliente> clientes;

  public SistemaBancario() {
    this.empresas = new ArrayList<>();
    this.clientes = new ArrayList<>();
  }

  private boolean isValidCNPJ(String cnpj) {
    return cnpj != null && cnpj.matches("\\d{14}");
  }

  private boolean isValidCPF(String cpf) {
    return cpf != null && cpf.matches("\\d{11}");
  }

  public void criarEmpresa(String cnpj, double saldo) {
    if (!isValidCNPJ(cnpj)) {
      System.out.println("CNPJ inválido, tente novamente");
      return;
    }

    Optional<Empresa> existingEmpresa = empresas.stream().filter(e -> e.cnpj.equals(cnpj)).findAny();
    if (existingEmpresa.isPresent()) {
      System.out.println("CNPJ já está em uso, tente novamente");
    } else {
      empresas.add(new Empresa(cnpj, saldo));
      System.out.println("Empresa criada com sucesso");
    }
  }

  public void criarCliente(String cpf, double saldoCliente, String cnpjEmpresa) {
    if (!isValidCPF(cpf)) {
      System.out.println("CPF inválido, tente novamente");
      return;
    }

    Optional<Empresa> empresaVinculada = empresas.stream().filter(e -> e.cnpj.equals(cnpjEmpresa)).findAny();
    if (empresaVinculada.isPresent()) {
      clientes.add(new Cliente(cpf, saldoCliente, cnpjEmpresa));
      System.out.println("Cliente criado com sucesso");
    } else {
      System.out.println("Empresa não encontrada");
    }
  }

  public void realizarTransacao(String cpf, String tipo, double valor) {
    Optional<Cliente> cliente = clientes.stream().filter(c -> c.cpf.equals(cpf)).findAny();
    if (cliente.isPresent()) {
      if (tipo.equals("saque") && valor > cliente.get().saldo) {
        System.out.println("Saldo insuficiente para saque");
      } else {
        // Taxa fixa de 5 reais
        double taxa = 5.0;

        // Deduz a taxa fixa e o valor da transação do saldo do cliente
        cliente.get().saldo -= (valor + taxa);

        // Encontra a empresa vinculada ao cliente
        Optional<Empresa> empresaVinculada = empresas.stream().filter(
            e -> e.cnpj.equals(cliente.get().cnpjEmpresa)).findAny();

        if (empresaVinculada.isPresent()) {
          // Adiciona a taxa ao saldo da empresa vinculada
          empresaVinculada.get().saldo += taxa;
          System.out.println("Transação realizada com sucesso");
          System.out.println("Novo saldo do cliente: " + cliente.get().saldo);
          System.out.println("Novo saldo da empresa: " + empresaVinculada.get().saldo);
        } else {
          System.out.println("Empresa vinculada não encontrada");
        }
      }
    } else {
      System.out.println("Cliente não encontrado");
    }
  }

  public static void main(String[] args) {
    SistemaBancario sistema = new SistemaBancario();
    sistema.criarEmpresa("12345678901234", 1000.0);
    sistema.criarCliente("12345678901", 500.0, "12345678901234");
    sistema.realizarTransacao("12345678901", "saque", 100.0);
  }

  public List<Empresa> getEmpresas() {
    return empresas;
  }

  public void setEmpresas(List<Empresa> empresas) {
    this.empresas = empresas;
  }

  public List<Cliente> getClientes() {
    return clientes;
  }

  public void setClientes(List<Cliente> clientes) {
    this.clientes = clientes;
  }
}
