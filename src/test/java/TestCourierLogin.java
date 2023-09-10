import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestCourierLogin {
    public static final String SCOOTER_SERVICE_URI = "https://qa-scooter.praktikum-services.ru/";
    public static final String LOGIN = RandomStringUtils.randomAlphabetic(10);
    public static final String PASSWORD = "123";
    ScooterServiceClient client = new ScooterServiceClient();
    public Courier courier;
    @Before
    public void setUp(){
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(SCOOTER_SERVICE_URI)
                .build();
        client.setRequestSpecification(requestSpec);
        courier = new Courier
                .Builder()
                .withLogin(LOGIN)
                .withPassword(PASSWORD)
                .build();
        client.createCourier(courier);
    }
    @After
    public void deleteCreatedCourier() {
        if ((courier.getLogin()!=null) && (courier.getPassword()!=null)){
            int id = client.login(Credential.fromCourier(courier)).extract().body().jsonPath().getInt("id");
            if (id!= 0) {
                client.deleteCourierById(id);
            }
        }
    }

    @Test
    @DisplayName("Courier can login")
    public void courierCanLogin(){
        ValidatableResponse response = client.login(Credential.fromCourier(courier));
        response.assertThat().body("id", notNullValue()).and().statusCode(200);
    }
    @Test
    @DisplayName("Courier can't login without login")
    public void courierCanNotLoginWithoutLogin(){
        Credential credential = new Credential.Builder().withPassword(PASSWORD).build();
        ValidatableResponse response = client.login(credential);
        response.assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }
//    @Test
//    @DisplayName("Курьер не может авторизоваться без пароля")
//    public void courierCanNotLoginWithoutPassword(){
//        Credential credential = new Credential.Builder().withLogin(LOGIN).build();
//        ValidatableResponse response = client.login(credential);
//        response.assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
//    }
    @Test
    @DisplayName("Courier can't login with wrong password")
    public void courierCanNotLoginWithWrongPassword(){
        Credential credential = new Credential.Builder().withLogin(LOGIN).withPassword(PASSWORD+(int)Math.random()).build();
        ValidatableResponse response = client.login(credential);
        response.assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    @Test
    @DisplayName("Courier can't login with wrong login")
    public void courierCanNotLoginWithWrongLogin(){
        Credential credential = new Credential.Builder().withLogin(LOGIN+(int)Math.random()).withPassword(PASSWORD).build();
        ValidatableResponse response = client.login(credential);
        response.assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }
}

