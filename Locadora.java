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
    try (java.io.PrintWriter pwV = new java.io.PrintWriter(new java.io.FileWriter("veiculos.txt"));
         java.io.PrintWriter pwL = new java.io.PrintWriter(new java.io.FileWriter("locacoes.txt"))) {
        
        for (Veiculo v : veiculos)
            pwV.println(v.serializar());
        
        for (Locacao l : locacoes)
            pwL.println(l.serializar());
        
        dadosAlterados = false;
        System.out.println("Dados salvos com sucesso!");
        
    } catch (IOException e) {
        System.out.println("Erro ao salvar dados: " + e.getMessage());
    }
}

    public void consultaVeiculo(Scanner sc) {
        System.out.println("Atributos disponíveis: modelo, cor, ano, cidade");
        System.out.print("Informe o atributo: ");

        String atributo = sc.nextLine().trim().toLowerCase();
        System.out.print("Informe o valor do atributo: ");
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

    public void realizarLocacao(Scanner sc) {

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

    public void realizaDevolucao(Scanner sc) {

        System.out.print("Informe o nome do cliente: ");
        String nomeCliente = sc.nextLine().trim();

        Locacao locacaoAtiva = null;
        for (Locacao l : locacoes) {
            if (l.cliente.equalsIgnoreCase(nomeCliente) && !l.veiculo.isDisponivel()) {
            locacaoAtiva = l;
            break;
        }
    }

    if (locacaoAtiva == null) {
        System.out.println("Nenhuma locação ativa encontrada para este cliente.");
        return;
    }

    System.out.print("Informe a cidade de devolução: ");
    String cidadeDevolucao = sc.nextLine().trim();

    System.out.print("Informe a quilometragem percorrida: ");
    int kmPercorrido = sc.nextInt();
    sc.nextLine();

    System.out.printf("Quantidade de diárias contratadas: %d%n", locacaoAtiva.qt_dias);
    System.out.print("Confirma a quantidade de dias ou deseja informar um valor diferente? (S para confirmar / N para informar): ");
    String resposta = sc.nextLine().trim();

    int diasReais;
    if (resposta.equalsIgnoreCase("S")) {
        diasReais = locacaoAtiva.qt_dias;
    } else {
        System.out.print("Informe a quantidade real de dias utilizados: ");
        diasReais = sc.nextInt();
        sc.nextLine();
    }

    locacaoAtiva.qt_dias_realizado = diasReais;
    locacaoAtiva.km_rodado = kmPercorrido;
    locacaoAtiva.destino = cidadeDevolucao;

    double valorDiariasContratadas = locacaoAtiva.qt_dias * locacaoAtiva.veiculo.getValor_diaria();
    double ajusteDiarias = 0;

    if (diasReais < locacaoAtiva.qt_dias) {

        int diasNaoUsados = locacaoAtiva.qt_dias - diasReais;
        ajusteDiarias = -(diasNaoUsados * locacaoAtiva.veiculo.getValor_diaria() * 0.20);
        System.out.printf("Devolveu %d dia(s) antes. Desconto de 20%%: R$ %.2f%n", diasNaoUsados, Math.abs(ajusteDiarias));

    } else if (diasReais > locacaoAtiva.qt_dias) {
        int diasExtras = diasReais - locacaoAtiva.qt_dias;
        ajusteDiarias = diasExtras * locacaoAtiva.veiculo.getValor_diaria() * 1.30;
        System.out.printf("Devolveu %d dia(s) depois. Valor das diárias extras (com 30%% de multa): R$ %.2f%n", diasExtras, ajusteDiarias);
    }

    double valorKm = kmPercorrido * locacaoAtiva.veiculo.getValor_km_rodado();
    System.out.printf("Valor referente aos quilômetros rodados: R$ %.2f%n", valorKm);

    double totalFinal = valorDiariasContratadas + ajusteDiarias + valorKm;
    System.out.printf("Valor total da locação: R$ %.2f%n", totalFinal);

    locacaoAtiva.veiculo.setCidade(cidadeDevolucao);
    locacaoAtiva.veiculo.setOdomentro(locacaoAtiva.veiculo.getOdomentro() + kmPercorrido);
    locacaoAtiva.veiculo.setDisponivel(true);

    dadosAlterados = true;

    System.out.println("Devolução realizada com sucesso! Veículo liberado para nova locação.");

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
