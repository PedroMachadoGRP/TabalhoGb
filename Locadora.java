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
