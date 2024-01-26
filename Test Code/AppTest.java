package uk.ac.ed.inf;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;

import java.time.LocalDate;

public class OrderValidatorTest {
    private Restaurant[] restaurants;
    private OrderValidator validator;
    private Pizza pizzaMargarita;
    private Pizza pizzaPepperoni;
    private Pizza pizzaVegan;

    @Before
    public void setup() {
        // Initialize pizzas and restaurants for testing
        // ...
        validator = new OrderValidator();
    }

    // Tests for General Order Fields
    @Test
    public void validateInvalidOrderNumber() {
        Order invalidOrderNumber = new Order(null, LocalDate.now(), "Customer", new CreditCardInformation("1234567890123456", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertFalse(validator.validateOrder(invalidOrderNumber, restaurants).isValid());
    }

    @Test
    public void validateInvalidOrderDate() {
        Order invalidOrderDate = new Order("123", null, "Customer", new CreditCardInformation("1234567890123456", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertFalse(validator.validateOrder(invalidOrderDate, restaurants).isValid());
    }

    @Test
    public void validateInvalidCustomerName() {
        Order invalidCustomerName = new Order("123", LocalDate.now(), "", new CreditCardInformation("1234567890123456", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertFalse(validator.validateOrder(invalidCustomerName, restaurants).isValid());
    }

    @Test
    public void validateValidCompleteOrder() {
        Order validateValidCompleteOrder = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("1234567890123456", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertTrue(validator.validateOrder(validateValidCompleteOrder, restaurants).isValid());
    }





    // Tests for Credit Card Information
    // Credit Card Expiry Date Tests
    @Test
    public void validateInvalidExpiryDateFormat() {
        Order invalidExpiryFormat = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/2023", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertFalse(validator.validateOrder(invalidExpiryFormat, restaurants).isValid());
    }

    @Test
    public void validateExpiryDateBeforeOrderDate() {
        Order expiryBeforeOrder = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "01/20", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertFalse(validator.validateOrder(expiryBeforeOrder, restaurants).isValid());
    }

    @Test
    public void validateValidExpiryDate() {
        Order validExpiryDate = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "12/25", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertTrue(validator.validateOrder(validExpiryDate, restaurants).isValid());
    }

    // CVV Tests
    @Test
    public void validateInvalidCvvFormat() {
        Order invalidCvvFormat = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "12A"), 1000, new Pizza[]{pizzaMargarita});
        assertFalse(validator.validateOrder(invalidCvvFormat, restaurants).isValid());
    }

    @Test
    public void validateValidCvv() {
        Order validCvv = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertTrue(validator.validateOrder(validCvv, restaurants).isValid());
    }

    @Test
    public void validateCardWithInvalidFormat() {
        Order invalidFormatCard = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("1234", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertFalse(validator.validateOrder(invalidFormatCard, restaurants).isValid());
    }

    @Test
    public void validateNonVisaMastercard() {
        Order nonVisaMastercard = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("6011111111111117", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertFalse(validator.validateOrder(nonVisaMastercard, restaurants).isValid());
    }

    @Test
    public void validateCardWithInvalidChecksum() {
        Order invalidChecksumCard = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("1234567890123456", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertFalse(validator.validateOrder(invalidChecksumCard, restaurants).isValid());
    }

    @Test
    public void validateValidCardNumber() {
        Order validCardNumber = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita});
        assertTrue(validator.validateOrder(validCardNumber, restaurants).isValid());
    }

    // Pizza Count Tests
    @Test
    public void validateExcessivePizzaCount() {
        Pizza[] tooManyPizzas = new Pizza[]{pizzaMargarita, pizzaPepperoni, pizzaVegan, pizzaMargarita, pizzaPepperoni};
        Order tooManyPizzasOrder = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 5000, tooManyPizzas);
        assertFalse(validator.validateOrder(tooManyPizzasOrder, restaurants).isValid());
    }

    @Test
    public void validateValidPizzaCount() {
        Pizza[] validPizzaCount = new Pizza[]{pizzaMargarita, pizzaPepperoni, pizzaVegan};
        Order validPizzaCountOrder = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 3000, validPizzaCount);
        assertTrue(validator.validateOrder(validPizzaCountOrder, restaurants).isValid());
    }

    // Undefined Pizza Test
    @Test
    public void validateOrderWithUndefinedPizza() {
        Pizza undefinedPizza = new Pizza("Undefined", 1000);
        Order orderWithUndefinedPizza = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 1500, new Pizza[]{undefinedPizza});
        assertFalse(validator.validateOrder(orderWithUndefinedPizza, restaurants).isValid());
    }

    // Pizzas From Same Restaurant Test
    @Test
    public void validatePizzasFromDifferentRestaurants() {
        Pizza[] pizzasFromDifferentRestaurants = new Pizza[]{pizzaMargarita, pizzaVegan}; // Assuming they are from different restaurants
        Order orderFromDifferentRestaurants = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 2100, pizzasFromDifferentRestaurants);
        assertFalse(validator.validateOrder(orderFromDifferentRestaurants, restaurants).isValid());
    }

    @Test
    public void validatePizzasFromSameRestaurant() {
        Pizza[] pizzasFromSameRestaurant = new Pizza[]{pizzaMargarita, pizzaPepperoni}; // Assuming they are from the same restaurant
        Order orderFromSameRestaurant = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 2200, pizzasFromSameRestaurant);
        assertTrue(validator.validateOrder(orderFromSameRestaurant, restaurants).isValid());
    }

    // Order Total Tests
    @Test
    public void validateInvalidOrderTotal() {
        Order invalidTotalOrder = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 999, new Pizza[]{pizzaMargarita}); // Incorrect total amount
        assertFalse(validator.validateOrder(invalidTotalOrder, restaurants).isValid());
    }

    @Test
    public void validateValidOrderTotal() {
        Order validTotalOrder = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 1000, new Pizza[]{pizzaMargarita}); // Correct total amount
        assertTrue(validator.validateOrder(validTotalOrder, restaurants).isValid());
    }

    // Valid Order Test
    @Test
    public void validateCorrectlyFormattedOrder() {
        Order correctlyFormattedOrder = new Order("123", LocalDate.now(), "Customer", new CreditCardInformation("4242424242424242", "10/23", "123"), 2500, new Pizza[]{pizzaMargarita, pizzaPepperoni});
        assertTrue(validator.validateOrder(correctlyFormattedOrder, restaurants).isValid());
    }

}

