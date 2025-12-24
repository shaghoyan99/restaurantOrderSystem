import service.CustomerService;
import service.DishService;
import service.OrderService;

import java.util.Scanner;

public class RestOrderSystem implements Commands {

    private static final CustomerService customerService =  new CustomerService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderService orderService =  new OrderService();
    private static final DishService dishService =  new DishService();


    public static void main(String[] args) {
        boolean isRun =  true;

        while (isRun) {
            Commands.printMainManu();
            String command = scanner.nextLine();
            switch (command) {
                case EXIT ->  isRun = false;
                case ADD_DISH -> addDish();
                case REMOVE_DISH -> removeDish();
                case CHANGE_DISH -> changeDish();
                case ADD_CUSTOMER -> addCustomer();
                case PRINT_CUSTOMERS -> printCustomers();
                case CREAT_NEW_ORDER -> creatNewOrder();
                case PRINT_ALL_ORDERS -> printAllOrders();
                case PRINT_ALL_ORDERS_BY_CUSTOMER -> printAllOldersByCustomer();
                case ORDER_INFORMATION -> printOrderInformation();
                case CHANGE_ORDER_STATUS -> changeOrderStatus();
                case PRINT_MENU_BY_CATEGORY -> printRestaurantMenuByCategory();
                default -> System.out.println("Invalid command" + command);
            }
        }
    }

    private static void printRestaurantMenuByCategory() {
    }

    private static void changeOrderStatus() {
    }

    private static void printOrderInformation() {
    }

    private static void printAllOldersByCustomer() {
    }

    private static void printAllOrders() {
    }

    private static void creatNewOrder() {
    }

    private static void printCustomers() {
    }

    private static void addCustomer() {
    }

    private static void changeDish() {
    }

    private static void removeDish() {
    }

    private static void addDish() {
    }

}
