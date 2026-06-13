public class Veiculo {
    private int codigo;
    private String modelo;
    private String cor;
    private int ano;
    private int odomentro;
    private String cidade;
    private boolean disponivel;
    private double valor_diaria;
    private double valor_km_rodado;

    public Veiculo(int codigo, String modelo, String cor, int ano, int odomentro, String cidade, boolean disponivel,
            double valor_diaria, double valor_km_rodado) {

        this.codigo = codigo;
        this.modelo = modelo;
        this.cor = cor;
        this.ano = ano;
        this.odomentro = odomentro;
        this.cidade = cidade;
        this.disponivel = disponivel;
        this.valor_diaria = valor_diaria;
        this.valor_km_rodado = valor_km_rodado;
    }

    public Veiculo() {

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {

        if (codigo <= 0) {
            System.out.println("O codigo tem que ser positivo");
            return;
        }

        this.codigo = codigo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {

        if (modelo == null || modelo.trim().isEmpty()) {
            System.out.println("Você deve inserir um modelo");
            return;
        }
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        if (cor == null || cor.trim().isEmpty()) {
            System.out.println("Você deve inserir uma cor");
            return;
        }

        this.cor = cor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {

        if (ano < 0) {
            System.out.println("O ano tem que ser maior que 0");
            return;
        }
        this.ano = ano;
    }

    public int getOdomentro() {
        return odomentro;
    }

    public void setOdomentro(int odomentro) {
        if (odomentro < 0) {
            System.out.println("O odometro tem que ser positivo");
            return;
        }
        this.odomentro = odomentro;
    }

    public String getCidade() {

        return cidade;
    }

    public void setCidade(String cidade) {

        if (cidade == null || cidade.trim().isEmpty()) {
            System.out.println("Você deve inserir uma cidade");
            return;
        }
        this.cidade = cidade;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public double getValor_diaria() {
        return valor_diaria;
    }

    public void setValor_diaria(double valor_diaria) {
        if (valor_diaria < 0) {
            System.out.println("o valor da diaria tem que ser maior que zero e não pode ser negativo");
            return;
        }
        this.valor_diaria = valor_diaria;
    }

    public double getValor_km_rodado() {
        return valor_km_rodado;
    }

    public void setValor_km_rodado(double valor_km_rodado) {

        if (valor_km_rodado < 0) {
            System.out.println("o valor pro km  tem que ser maior que zero e não pode ser negativo");
            return;
        }
        this.valor_km_rodado = valor_km_rodado;
    }

    public String serializar() {
            return getCodigo() + "\t" + getModelo() + "\t" + getCor() + "\t"
            + getAno() + "\t" + getOdomentro() + "\t" + getCidade() + "\t"
            + isDisponivel() + "\t" + getValor_diaria() + "\t" + getValor_km_rodado();
    }

    public static Veiculo deserializar(String linha) {

        String[] campos = linha.split("\t", -1);
        Veiculo veiculo = new Veiculo();
        veiculo.setCodigo(Integer.parseInt(campos[0].trim()));
        veiculo.setModelo(campos[1].isEmpty() ? "N/A" : campos[1]);
        veiculo.setCor(campos[2].isEmpty() ? "N/A" : campos[2]);
        veiculo.setAno(Integer.parseInt(campos[3].trim()));
        veiculo.setOdomentro(Integer.parseInt(campos[4].trim()));
        veiculo.setCidade(campos[5].isEmpty() ? "N/A" : campos[5]);
        veiculo.setDisponivel(campos[6].trim().equalsIgnoreCase("S") || campos[6].trim().equalsIgnoreCase("true"));
        veiculo.setValor_diaria(Double.parseDouble(campos[7].trim()));
        veiculo.setValor_km_rodado(Double.parseDouble(campos[8].trim()));
        return veiculo;
    }

    @Override
    public String toString() {
        return String.format(
        "%-6d %-20s %-12s %-6d %-10d %-20s %-12s %-12.2f %-10.2f",
        getCodigo(), getModelo(), getCor(), getAno(), getOdomentro(),
        getCidade(), isDisponivel() ? "Disponível" : "Alugado",
        getValor_diaria(), getValor_km_rodado()
    );
    }

    public static String cabecalho() {
        return String.format(
                "%-6s %-20s %-12s %-6s %-10s %-20s %-12s %-12s %-10s",
                "Cód", "Modelo", "Cor", "Ano", "Odômetro",
                "Cidade", "Situação", "Vlr/Diária", "Vlr/Km");
    }
}
