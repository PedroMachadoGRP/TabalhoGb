import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        boolean iniciar = true;
        Locadora locadora = new Locadora();
        locadora.carregaDados();
        Scanner sc = new Scanner(System.in);
        
        while (iniciar) {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Consultar veículos");
            System.out.println("2 - Realizar locação");
            System.out.println("3 - Realizar devolução");
            System.out.println("4 - Consultar locações");
            System.out.println("5 - Resumo");
            System.out.println("6 - Salvar");
            System.out.println("7 - Integrantes");
            System.out.println("8 - Sair");

            int escolha = sc.nextInt();
            sc.nextLine();

            switch (escolha) {
                case 1:
                    locadora.consultaVeiculo(sc);
                    break;
                case 2:
                    locadora.realizarLocacao(sc);
                    break;
                case 3:
                    locadora.realizaDevolucao(sc);
                    break;
                case 4:
                    locadora.consultaLocacao(sc);
                    break;
                case 5:
                    locadora.relatorioResumo();
                    break;
                case 6:
                    locadora.salvaDados();
                    break;
                case 7:
                    locadora.mostraIntegrantes();
                    break;
                case 8:
                    locadora.salvaDados();
                    iniciar = false;
                    break;
                default:
                    break;
            }
        }
    }
}