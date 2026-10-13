package bank.ATM;

public class Account {
    public int cardNumber;
    public int pinCode;
    public double balance;
    public BankType bankType;

    public Account(int cardNumber, int pinCode, double balance, BankType bankType){
        if (cardNumber < 10000 || cardNumber > 99999){
            System.out.println("ОшибкаЖ Номер карты должен быть пятизначным. Установлено значение 0.");
            this.cardNumber = 0;
        }else{
            this.cardNumber = cardNumber;
        }

        if (pinCode < 100 || pinCode > 999){
            System.out.println("Ошибка: Пин-код должен быть трёхзначным. Установлено значение 0.");
            this.pinCode = 0;
        }else{
            this.pinCode = pinCode;
        }

        this.balance = balance;
        if (bankType == null){
            this.bankType = BankType.NEO;
        }else{
            this.bankType = bankType;
        }
    }

    public int getCardNumber() {return cardNumber; }
    public int getPinCide() {return pinCode; }
    public double getBalance() {return balance; }
    public BankType getBankType() {return bankType; }

    @Override
    public String toString(){
        return String.format("%s Карта: %d, Баланс: %.2f руб.",
                bankType.getRussianName(), cardNumber, balance);
    }
}
