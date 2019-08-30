import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.DoubleStream;
import java.util.OptionalDouble;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;



interface EquationContainer {
    public String sayHello();
}

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
    public static double salarioBruto(double valorHora, double horasSemanais) {
        return valorHora * horasSemanais * 4.5;
    }
    public static double salarioLiquido(List <TabelaPadrao> tabelaInss,List <TabelaPadrao> tabelaIrrf, double salarioBruto) {
        return  salarioBruto - descontosInss(tabelaInss,salarioBruto) - descontosIrrf(tabelaIrrf,salarioBruto) ;
    }
    public static double descontosInss (List<TabelaPadrao> tabela, double salarioBruto  ) {
        double preCalculoInss =   salarioBruto * aliquotaInss( tabela, salarioBruto);
        return preCalculoInss > 621.03 ? 621.03 : preCalculoInss;
    }
    public static double descontosIrrf (List<TabelaPadrao> tabela, double salarioBruto) {
        return  (salarioBruto * aliquotaIrrf(tabela,salarioBruto)) - deducaoIrrf(tabela,salarioBruto);
    }

    //  -aliquotas
    public static double aliquotaInss( List<TabelaPadrao> tabela ,double salarioBruto){
        return filtraInss(tabela,salarioBruto).aliquota;
    }
    public static double aliquotaIrrf(  List<TabelaPadrao> tabela, double salarioBruto){
        return filtraIrrf(tabela,salarioBruto).aliquota;
    }
    // -deducoes
    public static double deducaoIrrf( List<TabelaPadrao> tabela, double salarioBruto){
        return   filtraIrrf(tabela,salarioBruto).deducao;
    }


    // FUNCOES AUXILIARES

    public static TabelaPadrao filtraInss(List <TabelaPadrao> tabela, double salarioBruto) {
        return tabela.stream()
            .filter(a->  ( salarioBruto < a.salario) ? true : false )
            .reduce((a,b) -> a.salario < b.salario ? b : a).get();
    }
    public static TabelaPadrao filtraIrrf(List <TabelaPadrao> tabela, double salarioBruto) {
        return tabela.stream()
            .filter(a -> a.salario < salarioBruto ? true : false)
            .reduce((a,b) -> b).get();
    }

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

EquationContainer eq = () -> {
return "asdf";
};

        // variaveis de entrada
        final double valorHora = 102,
              horasSemanais=40;



        double salarioBruto = salarioBruto(valorHora,horasSemanais);

        //  *************************** SAIDA:

        System.out.println("aliquota inss " + aliquotaInss(tabelaInss,salarioBruto) );
        System.out.println("descontosInss " + descontosInss(tabelaInss,salarioBruto) );

        System.out.println("aliquota irff " + aliquotaIrrf(tabelaIrrf,salarioBruto) );
        System.out.println("deducao irff " + deducaoIrrf(tabelaIrrf,salarioBruto) );
        System.out.println("desc. irff " + descontosIrrf(tabelaIrrf,salarioBruto) );
        System.out.println("salarioLiquido " + salarioLiquido(tabelaInss,tabelaIrrf,salarioBruto) );
        System.out.println("salarioBruto " + salarioBruto(valorHora,horasSemanais) );
    }

}


