import model.Category;
import model.Customer;
import model.Dish;
import model.Order;
import model.OrderItem;
import model.Status;
import service.CustomerService;
import service.DishService;
import service.OrderItemService;
import service.OrderService;
import util.CheckEmailUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RestOrderSystem implements Commands {

    private static final CustomerService customerService = new CustomerService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final OrderService orderService = new OrderService();
    private static final DishService dishService = new DishService();
    private static final OrderItemService orderItemService = new OrderItemService();


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
                case CHANGE_ORDER_STATUS -> updateOrderStatus();
                case PRINT_MENU_BY_CATEGORY -> printRestaurantMenuByCategory();
                default -> System.out.println("Invalid command" + command);
            }
        }
    }

    private static void printRestaurantMenuByCategory() {
        Commands.printMenuCategory();
        String category = scanner.nextLine();
        List<Dish> dishesByCategory = dishService.getDishesByCategory(Category.valueOf(category));
        System.out.println(dishesByCategory);
    }

    private static void updateOrderStatus() {
        List<Order> orders = orderService.getOrders();
        if (!orders.isEmpty()) {
            System.out.println("Please input order id: ");
            System.out.println(orders);
            int orderId = Integer.parseInt(scanner.nextLine());
            Order orderById = orderService.getOrderById(orderId);
            Status status = orderById.getStatus();
            switch (status) {
                case PENDING -> orderById.setStatus(Status.PREPARING);
                case PREPARING -> orderById.setStatus(Status.READY);
                case READY -> orderById.setStatus(Status.DELIVERED);
                case DELIVERED -> System.out.println("Order has been successfully delivered!");
            }
            orderService.changeOrderStatus(orderById);
            System.out.println("Status updated to: " + orderById.getStatus());
        } else {
            System.out.println("Orders is empty!");
        }
    }

    private static void printOrderInformation() {
        List<Order> orders = orderService.getOrders();
        if (!orders.isEmpty()) {
            System.out.println("Please input order id: ");
            System.out.println(orders);
            int orderId = Integer.parseInt(scanner.nextLine());
            Order orderById = orderService.getOrderById(orderId);
            OrderItem orderItem = orderItemService.getOrderItem(orderId);
            System.out.println("Order information: " + orderById);
            System.out.println("Item: " + orderItem);
        }
    }

    private static void printAllOrderByCustomer() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        if (!allCustomers.isEmpty()) {
            System.out.println("Please enter the customer id:");
            System.out.println(allCustomers);
            int customerId = Integer.parseInt(scanner.nextLine());
            List<Order> ordersByCustomer = orderService.getOrdersByCustomer(allCustomers.get(customerId));
            System.out.println(ordersByCustomer);
        } else {
            System.out.println("No customer found");
        }
    }

    private static void printAllOrders() {
        List<Order> orders = orderService.getOrders();
        if (!orders.isEmpty()) {
            System.out.println(orders);
        } else {
            System.out.println("No orders found");
        }
    }

    private static void creatNewOrder() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        List<Dish> allDishes = dishService.getAllDishes();
        if (!allCustomers.isEmpty() || !allDishes.isEmpty()) {
            System.out.println(allCustomers);
            System.out.println("Please enter the customer ID");
            int customerId = Integer.parseInt(scanner.nextLine());
            Order order = new Order();
            order.setCustomer(customerService.getCustomerById(customerId));
            order.setTotalPrice(0);
            boolean addMore = true;
            double totalPrice = 0;
            while (addMore) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(orderService.addOrder(order));
                System.out.println("Please input dish id: ");
                int dishId = Integer.parseInt(scanner.nextLine());
                Dish dishById = dishService.getDishById(dishId);
                orderItem.setDish(dishById);
                System.out.println("Please input quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());
                orderItem.setQuantity(quantity);
                orderItem.setPrice(dishById.getPrice());
                orderItemService.addOrderItem(orderItem);
                totalPrice += (quantity * dishById.getPrice());
                System.out.println("Do you want to add another dish? y/n");
                if (scanner.nextLine().equalsIgnoreCase("n")) {
                    addMore = false;
                }
            }
            order.setTotalPrice(totalPrice);
            orderService.updateOrder(order);
            System.out.println("Order has been added!");
        } else {
            System.out.println("Customer or Dish not found");
        }
    }

    private static void printCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        if (!allCustomers.isEmpty()) {
            System.out.println(allCustomers);
        } else {
            System.out.println("There are no customers in the system");
        }
    }

    private static void addCustomer() {
        System.out.println("Please input customer name:");
        String name = scanner.nextLine();
        System.out.println("Please input customer phone");
        String phone = scanner.nextLine();

        String email = "";
        boolean isEmailValid = false;
        do {
            System.out.println("Please input customer email");
            email = scanner.nextLine();
            try {
                CheckEmailUtil.isValidEmail(email);
                isEmailValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (!isEmailValid);

        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setEmail(email);
        customerService.addCustomer(customer);
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
