package test;

public class TestRunner {

    private static int total = 0;
    private static int passaram = 0;
    private static int falharam = 0;

    public static void assertIsTrue(boolean condicao, String msgSucesso, String msgFalha) {
        total++;
        if(condicao) {
            passaram++;
            System.out.println("✅ " + msgSucesso);
        } else {
            falharam++;
            System.out.println("❌ " + msgFalha);
        }
    }

    public static void reportarFalha(String msg) {
        total++;
        falharam++;
        System.out.println("❌ " + msg);
    }

    public static void exibirResumo() {
        double aproveitamento = total == 0 ? 0 : ((double) passaram / total) * 100;

        System.out.println("\n==========================================");
        System.out.println("           RESUMO DOS TESTES              ");
        System.out.println("==========================================");
        System.out.println("Total de verificações : " + total);
        System.out.println("Testes que PASSARAM   : " + passaram);
        System.out.println("Testes que FALHARAM   : " + falharam);
        System.out.printf("Aproveitamento        : %.2f%%\n", aproveitamento);
        System.out.println("==========================================");

        if (falharam == 0 && total > 0) {
            System.out.println("🎉 TODOS OS TESTES PASSARAM COM SUCESSO!");
        } else {
            System.out.println("⚠️ ATENÇÃO: EXISTEM FALHAS A SEREM CORRIGIDAS!");
        }
    }

}
