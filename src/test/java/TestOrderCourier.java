import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import service.CourierService;
import service.Endpoints;
import service.OrderService;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class TestOrderCourier {
    public static final String[] GREY_COLOR = new String[] {"GREY"};
    public static final String[] BLACK_COLOR = new String[] {"BLACK"};
    public static final String[] MULTIPLE_COLOR = new String[] {"BLACK", "GREY"};
    public static final String[] EMPTY_COLOR = new String[] {};


    OrderService orderService = new OrderService();
    public Order order;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;
    @Before
    public void setUp(){
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Endpoints.SCOOTER_SERVICE_URI)
                .build();
        orderService.setRequestSpecification(requestSpec);
    }
    @Step("Create order with {color}")
    @Parameterized.Parameters()
    public static Object[][] getOrderData() {
        return new Object[][]{
                {"Иван", "Петров", "Москва ул.Мира 10 кв.145", "Черкизовская","79275445454", 1,"03.09.2023", "Как можно скорее",BLACK_COLOR},
                {"Василий", "Уткин", "Москва ул.Петрова 50 кв.5", "Сухаревская","79275445454", 2,"03.09.2023", "Как можно скорее",  GREY_COLOR},
                {"Иван", "Петров", "Москва ул.Корева 13 кв.15", "Автозаводская","79275445454", 1,"05.09.2023", "Как можно скорее",  MULTIPLE_COLOR},
                {"Иван", "Петров", "Москва ул.Мира 10 кв.145", "ВДНХ","79275445454", 2,"04.09.2023", "Как можно скорее",  EMPTY_COLOR}
        };
    }

    public TestOrderCourier(String firstName, String lastName, String address, String metroStation, String phone, Integer rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Test
    @DisplayName("Check create order")
    public void createOrder(){
        order = new Order.Builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withAddress(address)
                .withMetroStation(metroStation)
                .withPhone(phone)
                .withRentTime(rentTime)
                .withDeliveryDate(deliveryDate)
                .withColor(color)
                .withComment(comment)
                .build();
        ValidatableResponse response = orderService.createOrder(order);
        response.assertThat().statusCode(201).and().body("track", notNullValue());
    }


}
