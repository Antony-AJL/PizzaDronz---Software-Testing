package uk.ac.ed.inf;

import org.junit.Test;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void TestGetRestaurants() throws Exception {
        String BASEURL = "https://ilp-rest.azurewebsites.net";
        Restaurant[] restaurants = UtilityHelper.getRestaurants(BASEURL);
        assertNotNull(restaurants);
        assertTrue(restaurants.length > 0);
    }

    @Test
    public void TestGetOrders() {
        String BASEURL = "https://ilp-rest.azurewebsites.net";
        String date = "2023-01-01";
        Order[] orders = UtilityHelper.getOrders(BASEURL, date);
        assertNotNull(orders);
        assertTrue(orders.length >= 0);
    }

    @Test
    public void TestValidateOrder() {
        LocalDate orderDate = LocalDate.parse("2023-01-01");
        OrderStatus orderStatus = OrderStatus.PENDING;
        int priceTotalInPence = 1000;
        Pizza[] pizzasInOrder = new Pizza[]{new Pizza("Sample Pizza", 800)};
        CreditCardInformation creditCardInformation = new CreditCardInformation("John Doe", "1234-5678-9012-3456");

        Order sampleOrder = new Order("SampleOrderID", orderDate, orderStatus, OrderValidationCode.VALID, priceTotalInPence, pizzasInOrder, creditCardInformation);

        Restaurant[] restaurants = new Restaurant[0];
        Order validatedOrder = UtilityHelper.validateOrder(sampleOrder, restaurants);
        assertNotNull(validatedOrder);
    }

    @Test
    public void TestUpdateOrderStatus() {
        LocalDate orderDate1 = LocalDate.parse("2023-01-01");
        LocalDate orderDate2 = LocalDate.parse("2023-01-02");
        Order order1 = new Order("Order1", orderDate1, OrderStatus.PENDING, OrderValidationCode.VALID, 1000, new Pizza[]{new Pizza("Sample Pizza", 800)}, new CreditCardInformation("John Doe", "1234-5678-9012-3456"));
        Order order2 = new Order("Order2", orderDate2, OrderStatus.PENDING, OrderValidationCode.VALID, 1000, new Pizza[]{new Pizza("Sample Pizza", 800)}, new CreditCardInformation("John Doe", "1234-5678-9012-3456"));

        List<Order> orders = Arrays.asList(order1, order2);
        UtilityHelper.updateOrderStatus(orders);

        for (Order order : orders) {
            assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, order.getOrderStatus());
        }
    }

    @Test
    public void TestIsWebsiteAlive() {
        String BASEURL = "https://ilp-rest.azurewebsites.net";
        boolean isAlive = UtilityHelper.isWebsiteAlive(BASEURL);
        assertTrue(isAlive);
    }

    @Test
    public void TestCreateResultFolder() {
        UtilityHelper.createResultFolder();
        File resultFolder = new File("resultfiles/");
        assertTrue(resultFolder.exists() && resultFolder.isDirectory());
    }

    @Test
    public void TestGetRestaurant() {
        Restaurant restaurant1 = new Restaurant("Restaurant1", new LngLat(55.7558, 37.6176),
                new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY},
                new Pizza[]{
                        new Pizza("Margherita", 800),
                        new Pizza("Pepperoni", 900)
                }
        );
        Restaurant restaurant2 = new Restaurant("Restaurant2", new LngLat(55.7558, 37.6176),
                new DayOfWeek[]{DayOfWeek.TUESDAY, DayOfWeek.THURSDAY},
                new Pizza[]{
                        new Pizza("Hawaiian", 850),
                        new Pizza("Vegetarian", 850)
                }
        );

        Restaurant[] restaurants = new Restaurant[]{restaurant1, restaurant2};
        LocalDate orderDate = LocalDate.parse("2023-01-01");
        Order sampleOrder = new Order("SampleOrderID", orderDate, OrderStatus.PENDING, OrderValidationCode.VALID, 1000, new Pizza[]{new Pizza("Sample Pizza", 800)}, new CreditCardInformation("John Doe", "1234-5678-9012-3456"));
        Restaurant foundRestaurant = UtilityHelper.getRestaurant(restaurants, sampleOrder);
        assertNotNull(foundRestaurant);
    }
}
