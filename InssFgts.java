import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.DoubleStream;
import java.util.OptionalDouble;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;

public class InssFgts{

    // estrutura de dados
    public static class TabelaPadrao {
        Double salario = null;
        Double aliquota = null;
        Double deducao = null;

        TabelaPadrao(Double salario, Double aliquota, Double deducao) {
            this.salario = salario;
            this.aliquota = aliquota;
            this.deducao = deducao;
        }
    }

    // EQUACOES
    public static BiFunction < Double, Double, Double>
        calcSalarioBruto = (valorHora, horasSemanais) ->  valorHora * horasSemanais * 4.5;

    public static BiFunction < List<TabelaPadrao>, Double , TabelaPadrao> filtraInss = (tabela,salarioBruto) ->
        tabela.stream().filter(a->  ( salarioBruto < a.salario) ? true : false )
        .reduce((a,b) -> a.salario < b.salario ? b : a).get();

    public static BiFunction < List<TabelaPadrao>, Double , TabelaPadrao> filtraIrrf = (tabela,salarioBruto) ->
        tabela.stream().filter(a -> a.salario < salarioBruto ? true : false)
        .reduce((a,b) -> b).get();

    //  -aliquotas
    public static BiFunction < List<TabelaPadrao>, Double , Double>
        aliquotaInss = (tabela,salarioBruto) ->filtraInss.apply(tabela,salarioBruto).aliquota;

    public static BiFunction < List<TabelaPadrao>, Double , Double>
        aliquotaIrrf = (tabela,salarioBruto) ->filtraIrrf.apply(tabela,salarioBruto).aliquota;

    //  -deducoes
    public static BiFunction < List<TabelaPadrao>, Double , Double>
        deducaoIrrf = (tabela,salarioBruto) -> filtraIrrf.apply(tabela,salarioBruto).deducao;

    //  -descontos
    public static BiFunction < List<TabelaPadrao>, Double , Double>
        calcDescontosIrrf = (tabela,salarioBruto) ->
        (salarioBruto * aliquotaIrrf.apply(tabela,salarioBruto)) - deducaoIrrf.apply(tabela,salarioBruto);

    public static BiFunction < List<TabelaPadrao>, Double , Double>
        calcDescontosInss = (tabela,salarioBruto) -> {
            double preCalculoInss =   salarioBruto * aliquotaInss.apply( tabela, salarioBruto);
            return preCalculoInss > 621.03 ? 621.03 : preCalculoInss;
        };

    public static BiFunction < List<TabelaPadrao>, List<TabelaPadrao> , Function<Double,Double>>
        salarioLiquido = (tabelaInss,tabelaIrrf) -> salarioBruto -> {
            double descontosInss = calcDescontosInss.apply(tabelaInss,salarioBruto);
            double descontosIrrf = calcDescontosIrrf.apply(tabelaIrrf,salarioBruto);
            return salarioBruto - descontosInss - descontosIrrf;
        };



    public static void main(String []args) {

        //  ************************ INPUTS
        //tabelas
        List<TabelaPadrao> tabelaInss = new ArrayList<>();
        tabelaInss.add(new TabelaPadrao(0.0,      0.0,      0.0));
        tabelaInss.add(new TabelaPadrao(1693.72,  0.08,     0.0));
        tabelaInss.add(new TabelaPadrao(2822.90,  0.09,     0.0));
        tabelaInss.add(new TabelaPadrao(5645.80,  0.11,     0.0));
        tabelaInss.add(new TabelaPadrao(1e20,     0.11,     0.0));

        List<TabelaPadrao> tabelaIrrf = new ArrayList<>();
        tabelaIrrf.add(new TabelaPadrao(350.0,    0.0,      0.0));
        tabelaIrrf.add(new TabelaPadrao(1903.98,  0.075,  142.0));
        tabelaIrrf.add(new TabelaPadrao(2826.65,  0.15,   354.0));
        tabelaIrrf.add(new TabelaPadrao(3751.87,  0.225,   636.0));
        tabelaIrrf.add(new TabelaPadrao(4664.68,  0.275,  869.36));
        tabelaIrrf.add(new TabelaPadrao(1e20,     0.275,  869.36));


        // variaveis de entrada
        final double valorHora = 102,
              horasSemanais=40;



        double salarioBruto = calcSalarioBruto.apply(valorHora,horasSemanais);

        //  *************************** SAIDA:

        System.out.println("aliquota inss " + aliquotaInss.apply(tabelaInss,salarioBruto) );
        System.out.println("descontosInss " + calcDescontosInss.apply(tabelaInss,salarioBruto) );
        System.out.println("aliquota irff " + aliquotaIrrf.apply(tabelaIrrf,salarioBruto) );
        System.out.println("deducao irff " + deducaoIrrf.apply(tabelaIrrf,salarioBruto) );
        System.out.println("desc. irff " + calcDescontosIrrf.apply(tabelaIrrf,salarioBruto) );
        System.out.println("salarioLiquido " + salarioLiquido.apply(tabelaInss,tabelaIrrf).apply(salarioBruto) );
        System.out.println("salarioBruto " + calcSalarioBruto.apply(valorHora,horasSemanais) );
    }

}


