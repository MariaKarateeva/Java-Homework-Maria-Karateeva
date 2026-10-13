package bank.ATM;

public enum BankType {
    NEO("НеоКредит Банк", 0.01),
    AUM("Арум Финтех", 0.02),
    VTA("Вектор Альянс Банк", 0.00);

    public final String russianName;
    public final double commission;

    BankType(String russianName, double commission){
        this.russianName = russianName;
        this.commission = commission;
    }

    public String getRussianName(){
        return russianName;
    }

    public double getCommission(){
        return commission;
    }
}
