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

import static org.hamcrest.Matchers.*;

public class TestCourierLogin {
    public static final String LOGIN = RandomStringUtils.randomAlphabetic(10);
    public static final String PASSWORD = "123";
    CourierService courierService = new CourierService();
    public Courier courier;
    @Before
    public void setUp(){
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Endpoints.SCOOTER_SERVICE_URI)
                .build();
        courierService.setRequestSpecification(requestSpec);
        courier = new Courier
                .Builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();
        courierService.createCourier(courier);
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
    @DisplayName("Courier can login")
    public void courierCanLogin(){
        ValidatableResponse response = courierService.login(Credential.fromCourier(courier));
        response.assertThat().body("id", notNullValue()).and().statusCode(200);
    }
    @Test
    @DisplayName("Courier can't login without login")
    public void courierCanNotLoginWithoutLogin(){
        Credential credential = new Credential.Builder().withPassword(PASSWORD).build();
        ValidatableResponse response = courierService.login(credential);
        response.assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }
    @Test
    @DisplayName("Courier can't login with wrong password")
    public void courierCanNotLoginWithWrongPassword(){
        Credential credential = new Credential.Builder().withLogin(LOGIN).withPassword(PASSWORD+(int)Math.random()).build();
        ValidatableResponse response = courierService.login(credential);
        response.assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    @Test
    @DisplayName("Courier can't login with wrong login")
    public void courierCanNotLoginWithWrongLogin(){
        Credential credential = new Credential.Builder().withLogin(LOGIN+(int)Math.random()).withPassword(PASSWORD).build();
        ValidatableResponse response = courierService.login(credential);
        response.assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }
}

