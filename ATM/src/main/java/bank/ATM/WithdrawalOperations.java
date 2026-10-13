package bank.ATM;

public interface WithdrawalOperations {

    double withdraw(double currentBalance, Double amountToTithdraw, BankType bankype);

    default double applyCommission(Double amount, BankType bankType){
        if (amount == null || bankType == null){
            return 0.0;
        }
        double commission = amount * bankType.getCommission();

        return Math.round(commission*100.0)/100.0;
    }
}
