import model.Category;
import model.Dish;
import service.CustomerService;
import service.DishService;
import service.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RestOrderSystem implements Commands {

    private static final CustomerService customerService = new CustomerService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderService orderService = new OrderService();
    private static final DishService dishService = new DishService();


    public static void main(String[] args) {
        boolean isRun = true;

        while (isRun) {
            Commands.printMainManu();
            String command = scanner.nextLine();
            switch (command) {
                case EXIT -> isRun = false;
                case ADD_DISH -> addDish();
                case REMOVE_DISH -> removeDish();
                case CHANGE_DISH -> changeDish();
                case ADD_CUSTOMER -> addCustomer();
                case PRINT_CUSTOMERS -> printCustomers();
                case CREAT_NEW_ORDER -> creatNewOrder();
                case PRINT_ALL_ORDERS -> printAllOrders();
                case PRINT_ALL_ORDERS_BY_CUSTOMER -> printAllOrderByCustomer();
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

    private static void printAllOrderByCustomer() {
    }

    private static void printAllOrders() {
    }

    private static void creatNewOrder() {

    }

    private static void printCustomers() {
    }

    private static void addCustomer() {

    }

    private static Optional<Dish> getDishById() {
        List<Dish> allDishes = dishService.getAllDishes();
        if (allDishes.isEmpty()) {
            return Optional.empty();
        }
        System.out.println(allDishes);
        System.out.println("Please enter the id of the dish you would like to remove");
        try {
            int dishId = Integer.parseInt(scanner.nextLine());
            return Optional.ofNullable(dishService.getDishById(dishId));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static void changeDish() {
        Optional<Dish> dishById = getDishById();
        dishById.ifPresentOrElse(
                dish -> {
                    dishService.changeDish(dish);
                    System.out.println("Dish has been changed");
                },
                () -> System.out.println("Invalid dish id"));

    }

    private static void removeDish() {
        Optional<Dish> dishById = getDishById();
        dishById.ifPresentOrElse(
                dish -> {
                    dishService.deleteDish(dish);
                    System.out.println("Dish has been deleted");
                },
                () -> System.out.println("Invalid dish id"));

    }

    private static void addDish() {
        Commands.printMenuCategory();
        String category = scanner.nextLine();
        System.out.println("Please input name.");
        String name = scanner.nextLine();
        System.out.println("Please input price.");
        double price = Double.parseDouble(scanner.nextLine());
        Dish dish = new Dish();
        dish.setName(name);
        dish.setCategory(Category.getCategoryByCode(category));
        dish.setPrice(price);
        dish.setAvailable(true);
        dishService.addDish(dish);
    }

}
