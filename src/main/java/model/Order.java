package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private int id;
    private Customer customer;
    private LocalDateTime orderDate;
    private double totalPrice = 0;
    private Status status;
}
