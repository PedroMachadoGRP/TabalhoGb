import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Locadora {

    ArrayList<Veiculo> veiculos;
    ArrayList<Locacao> locacoes;
    private boolean dadosAlterados;

    public Locadora() {
        veiculos = new ArrayList<>();
        locacoes = new ArrayList<>();
        dadosAlterados = false;
    }

    public void carregaDados() {
        carregaVeiculos();
    }

    private void carregaVeiculos() {
        File arquivo = new File("veiculos.txt");

        if (!arquivo.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty())
                    continue;
                try {
                    veiculos.add(Veiculo.deserializar(linha));
                } catch (Exception e) {
                    System.out.println("Erro ao carregar veículo: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler veiculos.txt: " + e.getMessage());
        }
    }

    public void salvaDados() {

    }

    public void consultaVeiculo(Scanner sc) {
        System.out.print("Informe o atributo: ");
        System.out.println("Atributos: modelo, cor, ano, cidade");

        String atributo = sc.nextLine().trim().toLowerCase();
        System.out.print("Informe o valor: ");
        String valor = sc.nextLine().trim().toLowerCase();

        ArrayList<Veiculo> encontrados = new ArrayList<>();

        for (Veiculo v : veiculos) {
            switch (atributo) {
                case "modelo":
                    if (v.getModelo().toLowerCase().contains(valor))
                        encontrados.add(v);
                    break;
                case "cor":
                    if (v.getCor().toLowerCase().contains(valor))
                        encontrados.add(v);
                    break;
                case "ano":
                    try {
                        if (v.getAno() == Integer.parseInt(valor))
                            encontrados.add(v);
                    } catch (NumberFormatException e) {
                    }
                    break;
                case "cidade":
                    if (v.getCidade().toLowerCase().contains(valor))
                        encontrados.add(v);
                    break;
                default:
                    System.out.println("Atributo inválido.");
                    return;
            }
        }

        if (encontrados.isEmpty()) {
            System.out.println("Nenhum veículo encontrado");

        } else {
            imprimirTabelaVeiculos(encontrados);
        }
    }

    public void realizarLocacao() {

    Scanner sc = new Scanner(System.in);

    System.out.print("Informe a cidade de origem: ");
    String cidade = sc.nextLine();

    ArrayList<Veiculo> disponiveis = new ArrayList<>();

    for (Veiculo v : veiculos) {
        if (v.isDisponivel() &&
                v.getCidade().equalsIgnoreCase(cidade)) {

            disponiveis.add(v);
        }
    }

    if (disponiveis.isEmpty()) {
        System.out.println("Nenhum veículo disponível nesta cidade.");
        return;
    }

    imprimirTabelaVeiculos(disponiveis);

    System.out.print("Informe o código do veículo: ");
    int codigo = sc.nextInt();
    sc.nextLine();

    Veiculo escolhido = null;

    for (Veiculo v : disponiveis) {
        if (v.getCodigo() == codigo) {
            escolhido = v;
            break;
        }
    }

    if (escolhido == null) {
        System.out.println("Veículo não encontrado.");
        return;
    }

    System.out.print("Informe o nome do cliente: ");
    String cliente = sc.nextLine();

    for (Locacao l : locacoes) {
        if (l.cliente != null &&
                l.cliente.equalsIgnoreCase(cliente) &&
                !l.veiculo.isDisponivel()) {

            System.out.println("Este cliente já possui uma locação ativa.");
            return;
        }
    }

    System.out.print("Informe a quantidade de diárias: ");
    int dias = sc.nextInt();
    sc.nextLine();

    double valorTotal = dias * escolhido.getValor_diaria();

    System.out.printf("Valor total das diárias: R$ %.2f%n", valorTotal);

    System.out.print("Deseja confirmar a locação? (S/N): ");
    String resposta = sc.nextLine();

    if (!resposta.equalsIgnoreCase("S")) {
        System.out.println("Locação cancelada.");
        return;
    }

    Locacao locacao = new Locacao();
    locacao.veiculo = escolhido;
    locacao.cliente = cliente;
    locacao.origem = cidade;
    locacao.qt_dias = dias;

    locacoes.add(locacao);

    escolhido.setDisponivel(false);

    dadosAlterados = true;

    System.out.println("Locação realizada com sucesso!");
    }

    public void consultaLocacao() {

    }

    public void realizaDevolucao() {

    }

    public void relatorioResumo() {

    }

    private void imprimirTabelaVeiculos(ArrayList<Veiculo> lista) {
        System.out.println("\n" + "=".repeat(110));
        System.out.println(Veiculo.cabecalho());
        System.out.println("=".repeat(110));
        for (Veiculo v : lista)
            System.out.println(v);
        System.out.println("=".repeat(110));
    }

}
