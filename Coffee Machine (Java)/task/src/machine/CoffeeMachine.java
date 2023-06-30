package machine;

import java.util.Scanner;

public class CoffeeMachine {


    public static void main(String[] args) {

        // Cria uma instância da máquina de café
        CoffeeMaker coffeeMaker = new CoffeeMaker(400, 540, 120,9, 550);
        Scanner scanner = new Scanner(System.in);

        // Loop principal para processar as entradas do usuário até a máquina de café ser desligada
        while (coffeeMaker.isOffline() != CoffeeMaker.StateMachine.OFFLINE) {

            String action = scanner.nextLine();
            coffeeMaker.processInput(action);
        }
    }
}

/**
 * Classe CoffeeMaker representa uma máquina de café.
 * Ela controla o estado e as funcionalidades da máquina.
 */
class CoffeeMaker {

    // Enumeração que define os tipos de café disponíveis
     enum Coffee {

        EXPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPPUCCINO(200, 100, 12, 6);

        private final int water;
        private final int milk;
        private final int coffeeBeans;
        private final int cost;
        Coffee(int water, int milk, int coffeeBeans, int cost) {
            this.water = water;
            this.milk = milk;
            this.coffeeBeans = coffeeBeans;
            this.cost = cost;
        }
    }

    // Enumeração que define os possíveis estados da máquina de café
    enum StateMachine {

         BEGIN_STATE, CHOOSING_COFFEE, FILLING_WATER, FILLING_MILK, FILLING_COFFEE_BEANS, FILLING_CUPS, OFFLINE
    }

    private int water;
    private int milk;
    private int coffeeBeans;
    private int cups;
    private int money;
    private StateMachine currentState;

    public CoffeeMaker(int water, int milk, int coffeeBeans, int cups, int money) {

        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.cups = cups;
        this.money = money;
        this.currentState = StateMachine.BEGIN_STATE;

        initialMenu();
    }

    private void showStatusMachine() {

        System.out.println("\nThe coffee machine has:");
        System.out.printf("%d ml of water\n", water);
        System.out.printf("%d ml of milk\n", milk);
        System.out.printf("%d g of coffee beans\n", coffeeBeans);
        System.out.printf("%d disposable cups\n", cups);
        System.out.printf("$%d of money\n", money);
    }

    /**
     * Verifica se a quantidade de ingredientes disponíveis na maquina de café é suficiente para preparar
     * o café solicitado
     * @param coffee representa o tipo de café a ser preparado
     * @return true se a quantidade é suficiente
     */
    private boolean checkIngredientAvailability(Coffee coffee) {

        if (coffee.water > water) {

           System.out.println("Sorry, not enough water!");
           return false;
        }

        if (coffee.milk > milk) {

            System.out.println("Sorry, not enough milk!");
            return false;
        }

        if (coffee.coffeeBeans > coffeeBeans) {

            System.out.println("Sorry, not enough coffee beans!");
            return false;
        }

        if (cups == 0) {

            System.out.println("Sorry, not enough cups!");
            return false;
        }

        return true;
    }

    /**
     * Processa a opção de compra de café selecionada pelo usuário.
     * Verifica se há ingredientes suficientes e realiza a venda do café.
     * @param option a opção selecionada pelo usuário
     */
    private void processBuyCommand(String option) {

        switch (option) {
            case "1" -> {

                if (checkIngredientAvailability(Coffee.EXPRESSO)) {
                    sellCoffee(Coffee.EXPRESSO);
                }
            }
            case "2" -> {

                if (checkIngredientAvailability(Coffee.LATTE)) {
                    sellCoffee(Coffee.LATTE);
                }
            }
            case "3" -> {

                if (checkIngredientAvailability(Coffee.CAPPUCCINO)) {
                    sellCoffee(Coffee.CAPPUCCINO);
                }
            }

            case  "back" -> {

            }
        }

        currentState = StateMachine.BEGIN_STATE;
    }

    /**
     * Realiza a venda de um café.
     * Reduz a quantidade de ingredientes, adiciona dinheiro e exibe uma mensagem de sucesso.
     * @param coffee o café a ser vendido
     */
    private void sellCoffee(Coffee coffee) {

        water -= coffee.water;
        milk -= coffee.milk;
        coffeeBeans -= coffee.coffeeBeans;
        money += coffee.cost;
        cups--;
        System.out.println("I have enough resources, making you a coffee!");
    }


    /**
     * Realiza o preenchimento de ingredientes na máquina de café.
     * Dependendo do estado atual, adiciona água, leite, grãos de café ou copos descartáveis.
     * @param quantity a quantidade a ser adicionada
     */
    private void processFillCommand(int quantity) {

        if (currentState == StateMachine.FILLING_WATER) {
            water += quantity;
            currentState = StateMachine.FILLING_MILK;
            System.out.println("Write how many ml of milk you want to add:");

        } else if (currentState == StateMachine.FILLING_MILK) {
            milk += quantity;
            currentState = StateMachine.FILLING_COFFEE_BEANS;
            System.out.println("Write how many grams of coffee beans you want to add:");

        } else if (currentState == StateMachine.FILLING_COFFEE_BEANS) {
            coffeeBeans += quantity;
            currentState = StateMachine.FILLING_CUPS;
            System.out.println("Write how many disposable cups you want to add:");

        } else if (currentState == StateMachine.FILLING_CUPS) {
            cups += quantity;
            toBeginState();
        }
    }

    private void processTakeCommand() {

        System.out.printf("\nI gave you $%d\n", money);
        money = 0;
    }

    private void initialMenu() {
        System.out.println("Write action (buy, fill, take, remaining, exit):");
    }

    private void toBeginState() {

        currentState = StateMachine.BEGIN_STATE;
        System.out.println();
        initialMenu();
    }

    /**
     * Processa as solicitações feitas para maquinha de café, funcionando como uma "interface"
     * @param command representa a solicitação desejada, podendo ser tanto as de menu inicial
     *                (buy, fill, take, remaining, exit), como o tipo de café ou quantidades de ingredientes
     */
    public void processInput(String command) {

        if (currentState == StateMachine.BEGIN_STATE) {
            switch (command) {

                case "buy" -> {
                    currentState = StateMachine.CHOOSING_COFFEE;
                    System.out.println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
                }
                case "fill" -> {
                    currentState = StateMachine.FILLING_WATER;
                    System.out.println("Write how many ml of water you want to add:");
                }
                case "take" -> {
                    processTakeCommand();
                    toBeginState();
                }
                case "remaining" -> {
                    showStatusMachine();
                    toBeginState();
                }

                case "exit" -> currentState = StateMachine.OFFLINE;
            }
        } else if (currentState == StateMachine.CHOOSING_COFFEE) {

            processBuyCommand(command);
            toBeginState();

        } else if (currentState == StateMachine.FILLING_WATER ||
                    currentState == StateMachine.FILLING_MILK ||
                    currentState == StateMachine.FILLING_COFFEE_BEANS ||
                    currentState == StateMachine.FILLING_CUPS) {

            processFillCommand(Integer.parseInt(command));
        }
    }

    public StateMachine isOffline() {
        return currentState;
    }
}
