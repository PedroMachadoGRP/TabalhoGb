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
    private static String separador = ";";


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

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getOdomentro() {
        return odomentro;
    }

    public void setOdomentro(int odomentro) {
        this.odomentro = odomentro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
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
        this.valor_diaria = valor_diaria;
    }

    public double getValor_km_rodado() {
        return valor_km_rodado;
    }

    public void setValor_km_rodado(double valor_km_rodado) {
        this.valor_km_rodado = valor_km_rodado;
    }

    public String serializar() {
        return System.lineSeparator() + getCodigo() + separador + getModelo() + separador + getCor() + separador + getAno() + separador + getOdomentro() + separador + getCidade() + separador  + isDisponivel() + separador + getValor_diaria() + separador + getValor_km_rodado();
    }

}
