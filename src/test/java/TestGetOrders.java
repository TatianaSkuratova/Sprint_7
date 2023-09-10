import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestGetOrders {
    ScooterServiceClient client = new ScooterServiceClient();
    public static final String SCOOTER_SERVICE_URI = "https://qa-scooter.praktikum-services.ru/";
    public static final String LOGIN = RandomStringUtils.randomAlphabetic(10);
    public static final String PASSWORD = "123";

    // десериализуем строку
    Courier courier;
    Order order;
    @Before
    public void setUp(){
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(SCOOTER_SERVICE_URI)
                .build();
        client.setRequestSpecification(requestSpec);
    }
    @Test
    @DisplayName("Orders return by id courier")
    public void getOrdersByIdCourierIsNotNull(){
       //создаем курьера
        courier = new Courier
                .Builder()
                .withLogin(LOGIN)
                .withPassword(PASSWORD)
                .build();
        client.createCourier(courier);
        //получаем логин
        int courierId = client.login(Credential.fromCourier(courier)).extract().body().jsonPath().get("id");
        //создаем заказ
        String[] blackColor = new String[] {"BLACK"};
        order = new Order.Builder()
                .withFirstName("Аня")
                .withLastName("Иванова")
                .withAddress("Москва ул.Мира 14,151")
                .withMetroStation("Сухаревская")
                .withPhone("89276566655")
                .withRentTime(2)
                .withDeliveryDate("10.09.2023")
                .withColor(blackColor)
                .withComment("скорей")
                .build();
        int orderTrack = client.createOrder(order).extract().body().jsonPath().get("track");
        int orderId = client.getIdOrderByTrack(orderTrack).extract().body().jsonPath().get("order.id");
        //принимаем заказ для курьера
        client.acceptOrderToCourier(orderId, courierId);
        //возвращаем список заказов для курьера
        ValidatableResponse response = client.getOrdersByCourierId(courierId);
        Orders orders = response.extract().as(Orders.class);
        Assert.assertNotNull(orders.getOrders().length);

    }

    @After
    public void cleanUp(){
        //удаляем курьера
        if ((courier.getLogin()!=null) && (courier.getPassword()!=null)){
            int id = client.login(Credential.fromCourier(courier)).extract().body().jsonPath().getInt("id");
            if (id!= 0) {
                client.deleteCourierById(id);
            }
        }

    }
    }

