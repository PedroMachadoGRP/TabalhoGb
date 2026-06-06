public class Locacao {
    Veiculo veiculo;
    String cliente;
    String origem;
    String destino;
    int km_rodado;
    int qt_dias;
    int qt_dias_realizado;

    public double valorDiarias(){
        return qt_dias * veiculo.getValor_diaria();
    }

    public double valorKmRodado(){
        return km_rodado * veiculo.getValor_km_rodado();
    }

    public String serializar() {
        return veiculo.getCodigo() + "\t" + cliente + "\t" + origem + "\t" + 
               (destino != null ? destino : "") + "\t" + km_rodado + "\t" + 
               qt_dias + "\t" + qt_dias_realizado;
    }
}