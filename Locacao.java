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
}
