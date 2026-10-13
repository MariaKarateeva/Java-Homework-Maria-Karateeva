package bank.ATM;

public class CashMachine implements WithdrawalOperations, DepositOperations{
    @Override
    public double deposit(double currentBalance, Double amountToDeposit){
        if (amountToDeposit == null || amountToDeposit <=0){
            return currentBalance;
        }
        double newBalance = currentBalance + amountToDeposit;
        return Math.round(newBalance*100.0)/100.0;
    }
    @Override
    public double withdraw(double currentBalance, Double amountToWithdraw, BankType bankType){
        if (amountToWithdraw == null || amountToWithdraw <= 0){
            return currentBalance;
        }
        double commission = applyCommission(amountToWithdraw, bankType);
        double totalDeduction = amountToWithdraw + commission;
        totalDeduction = Math.round(totalDeduction*100.0)/100.0;
        if (totalDeduction > currentBalance){
            System.out.println("Ошибка: Недостаточно средств!");
            System.out.println("Требуется:"+totalDeduction + " руб. (включая комиссию" + commission + ")");
            System.out.println("Доступно: "+currentBalance + " руб.");

            return currentBalance;
        }
        double newBalance = currentBalance - totalDeduction;

        return Math.round(newBalance * 100.0)/100.0;
    }
}
