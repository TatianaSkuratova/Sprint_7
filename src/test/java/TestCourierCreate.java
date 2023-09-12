import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.Courier;
import models.Credential;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.CourierService;
import service.Endpoints;

import static org.hamcrest.Matchers.equalTo;

public class TestCourierCreate {

    public static final String LOGIN = RandomStringUtils.randomAlphabetic(10);
    public static final String PASSWORD = RandomStringUtils.randomAlphabetic(6);
    public static final String FIRST_NAME = RandomStringUtils.randomAlphabetic(15);

    CourierService courierService = new CourierService();
    public Courier courier;
    @Before
    public void setUp(){
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Endpoints.SCOOTER_SERVICE_URI)
                .build();
        courierService.setRequestSpecification(requestSpec);
    }
    @After
    public void deleteCreatedCourier() {
        if ((courier.getLogin()!=null) && (courier.getPassword()!=null)){
            int id = courierService.login(Credential.fromCourier(courier)).extract().body().jsonPath().getInt("id");
            if (id!= 0) {
                courierService.deleteCourierById(id);
            }
        }
    }
    @Test
    @DisplayName("Courier is created")
    public void createCourierIsAvailable() {
        courier = new Courier
                .Builder()
                .login(LOGIN)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .build();
        ValidatableResponse response = courierService.createCourier(courier);
        response.assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

    @Test
    @DisplayName("Create courier with duplicate login isn't available")
    public void createCourierWithDuplicateLoginIsNotAvailable() {
        courier = new Courier
                .Builder()
                .login(LOGIN)
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .build();
        courierService.createCourier(courier);
        Courier newCourier = new Courier
                .Builder()
                .login(LOGIN)
                .password(PASSWORD+(int) (Math.random()*10))
                .firstName(FIRST_NAME+(int) (Math.random()*10))
                .build();
        ValidatableResponse response = courierService.createCourier(newCourier);
        response.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);
    }

    @Test
    @DisplayName("Create duplicate courier isn't available")
    public void createDuplicateCourierIsNotAvailable() {
        courier = new Courier
                .Builder()
                .login(LOGIN)
                .firstName(FIRST_NAME)
                .password(PASSWORD)
                .build();
        courierService.createCourier(courier);
        ValidatableResponse  response= courierService.createCourier(courier);
        response.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);
    }

    @Test
    @DisplayName("Create courier without firstName is available")
    public void createCourierWithoutFirstNameIsAvailable() {
        courier = new Courier
                .Builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();
        ValidatableResponse response= courierService.createCourier(courier);
        response.assertThat().body("ok", equalTo(true)).and().statusCode(201);

    }

    @Test
    @DisplayName("Create courier without pass isn't available")
    public void createCourierWithoutPassIsNotAvailable() {
        courier = new Courier
                .Builder()
                .login(LOGIN)
                .firstName(FIRST_NAME)
                .build();
        ValidatableResponse response= courierService.createCourier(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    @DisplayName("Create courier without login isn't available")
    public void createCourierWithoutLoginIsNotAvailable() {
        courier = new Courier
                .Builder()
                .password(PASSWORD)
                .firstName(FIRST_NAME)
                .build();
        ValidatableResponse  response= courierService.createCourier(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

}
