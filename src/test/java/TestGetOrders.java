import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.Courier;
import models.Credential;
import models.Order;
import models.Orders;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import service.CourierService;
import service.Endpoints;
import service.OrderService;
import service.OrdersService;

@RunWith(Parameterized.class)
public class TestGetOrders {
    private OrdersService ordersService = new OrdersService();
    private CourierService courierService = new CourierService();
    private OrderService orderService = new OrderService();
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;
    private static final String LOGIN = RandomStringUtils.randomAlphabetic(10);
    private static final String PASSWORD = RandomStringUtils.randomAlphabetic(10);
    public static final String[] BLACK_COLOR = new String[] {"BLACK"};

    // десериализуем строку
    Courier courier;
    Order order;
    @Before
    public void setUp(){
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Endpoints.SCOOTER_SERVICE_URI)
                .build();
        orderService.setRequestSpecification(requestSpec);
        courierService.setRequestSpecification(requestSpec);
        ordersService.setRequestSpecification(requestSpec);

    }
    @Parameterized.Parameters()
    public static Object[][] getOrderData() {
        return new Object[][]{
                {"Иван", "Петров", "Москва ул.Мира 10 кв.145", "Черкизовская","79275445454", 1,"03.09.2023", "Как можно скорее", BLACK_COLOR}
        };
    }
    public TestGetOrders(String firstName, String lastName, String address, String metroStation, String phone, Integer rentTime, String deliveryDate, String comment, String[] color) {
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
    @DisplayName("Orders return by id courier")
    public void getOrdersByIdCourierIsNotNull(){
       //создаем курьера
        courier = new Courier
                .Builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();
        courierService.createCourier(courier);
        //получаем логин
        int courierId = courierService.login(Credential.fromCourier(courier)).extract().body().jsonPath().get("id");
        //создаем заказ

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
        int orderTrack = orderService.createOrder(order).extract().body().jsonPath().get("track");
        int orderId = orderService.getIdOrderByTrack(orderTrack).extract().body().jsonPath().get("order.id");
        //принимаем заказ для курьера
        orderService.acceptOrderToCourier(orderId, courierId);
        //возвращаем список заказов для курьера
        ValidatableResponse response = ordersService.getOrdersByCourierId(courierId);
        Orders orders = response.extract().as(Orders.class);
        Assert.assertNotNull(orders.getOrders().length);

    }

    @After
    public void cleanUp(){
        //удаляем курьера
        if ((courier.getLogin()!=null) && (courier.getPassword()!=null)){
            int id = courierService.login(Credential.fromCourier(courier)).extract().body().jsonPath().getInt("id");
            if (id!= 0) {
                courierService.deleteCourierById(id);
            }
        }

    }
    }

