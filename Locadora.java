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
        carregaLocacoes();
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

    private void carregaLocacoes() {
        File arquivo = new File("locacoes.txt");

        if (!arquivo.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty())
                    continue;

                try {
                    String[] campos = linha.split("\t", -1);
                    int codigoVeiculo = Integer.parseInt(campos[0].trim());
                    Veiculo veiculo = buscarVeiculoPorCodigo(codigoVeiculo);

                    if (veiculo == null) {
                        System.out.println("Veículo da locação não encontrado: " + codigoVeiculo);
                        continue;
                    }

                    Locacao locacao = new Locacao();
                    locacao.veiculo = veiculo;
                    locacao.cliente = campos[1];
                    locacao.origem = campos[2];
                    locacao.destino = campos[3].isEmpty() ? null : campos[3];
                    locacao.km_rodado = Integer.parseInt(campos[4].trim());
                    locacao.qt_dias = Integer.parseInt(campos[5].trim());
                    locacao.qt_dias_realizado = Integer.parseInt(campos[6].trim());

                    locacoes.add(locacao);

                    if (!locacaoFinalizada(locacao))
                        veiculo.setDisponivel(false);

                } catch (Exception e) {
                    System.out.println("Erro ao carregar locação: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler locacoes.txt: " + e.getMessage());
        }
    }

    private Veiculo buscarVeiculoPorCodigo(int codigo) {
        for (Veiculo v : veiculos) {
            if (v.getCodigo() == codigo)
                return v;
        }
        return null;
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

    public void consultaLocacao(Scanner sc) {
    System.out.print("Informe o nome do cliente ou modelo do veículo: ");
    String valor = sc.nextLine().trim().toLowerCase();

    boolean encontrou = false;

    for (Locacao l : locacoes) {
        if ((l.cliente != null && l.cliente.toLowerCase().contains(valor)) ||
            (l.veiculo != null && l.veiculo.getModelo().toLowerCase().contains(valor))) {

            encontrou = true;

            System.out.println("\nCliente: " + l.cliente);
            System.out.println("Veículo: " + l.veiculo.getModelo() + " - " + l.veiculo.getCor());
            System.out.println("Ano: " + l.veiculo.getAno());
            System.out.println("Origem: " + l.origem);
            System.out.println("Destino: " + (l.destino == null ? "" : l.destino));
            System.out.println("Km rodado: " + l.km_rodado);
            System.out.println("Dias reservados: " + l.qt_dias);
            System.out.println("Dias realizados: " + l.qt_dias_realizado);
            System.out.println("Situação: " + (locacaoFinalizada(l) ? "Finalizada" : "Ativa"));
        }
    }

    if (!encontrou) {
        System.out.println("Nenhuma locação encontrada.");
    }
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
        int totalKm = 0;
        int totalDiasContratados = 0;
        int totalDiasRealizados = 0;
        double totalDiariasContratadas = 0;
        double totalDiariasExtras = 0;
        double totalKmRodado = 0;
        double totalGeral = 0;
        int quantidadeFinalizadas = 0;

        for (Locacao l : locacoes) {
            if (!locacaoFinalizada(l))
                continue;
            quantidadeFinalizadas++;
            double valorDiariasContratadas = l.qt_dias * l.veiculo.getValor_diaria();
            double valorExtras = 0;
            double valorDesconto = 0;
            double valorKm = l.km_rodado * l.veiculo.getValor_km_rodado();

            if (l.qt_dias_realizado > l.qt_dias) {
                int diasExtras = l.qt_dias_realizado - l.qt_dias;
                valorExtras = diasExtras * l.veiculo.getValor_diaria() * 1.30;
            } else if (l.qt_dias_realizado < l.qt_dias) {
                int diasNaoUsados = l.qt_dias - l.qt_dias_realizado;
                valorDesconto = diasNaoUsados * l.veiculo.getValor_diaria() * 0.20;
            }

            totalKm += l.km_rodado;
            totalDiasContratados += l.qt_dias;
            totalDiasRealizados += l.qt_dias_realizado;
            totalDiariasContratadas += valorDiariasContratadas;
            totalDiariasExtras += valorExtras;
            totalKmRodado += valorKm;
            totalGeral += valorDiariasContratadas + valorExtras - valorDesconto + valorKm;
        }

        if (quantidadeFinalizadas == 0) {
            System.out.println("Nenhuma locação finalizada encontrada.");
            return;
        }

        System.out.println("\nResumo das locações finalizadas");
        System.out.println("Quantidade de locações finalizadas: " + quantidadeFinalizadas);
        System.out.println("Total de km rodados: " + totalKm);
        System.out.println("Total de dias contratados: " + totalDiasContratados);
        System.out.println("Total de dias realizados: " + totalDiasRealizados);
        System.out.printf("Valor das diárias contratadas: R$ %.2f%n", totalDiariasContratadas);
        System.out.printf("Valor das diárias extras: R$ %.2f%n", totalDiariasExtras);
        System.out.printf("Valor dos km rodados: R$ %.2f%n", totalKmRodado);
        System.out.printf("Valor total das locações: R$ %.2f%n", totalGeral);
    }

    public void mostraIntegrantes() {
        System.out.println("Integrantes do grupo:");
        System.out.println("João Pedro Boniatti");
        System.out.println("Otto Antonio Machado");
        System.out.println("Pedro Machado");
        System.out.println("Peterson Machado");
    }

    private boolean locacaoFinalizada(Locacao l) {
        return l.destino != null && !l.destino.trim().isEmpty();
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
