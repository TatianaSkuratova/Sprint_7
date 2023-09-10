import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName; // импорт DisplayName


import static org.hamcrest.Matchers.equalTo;

public class TestCourierCreate {
    public static final String SCOOTER_SERVICE_URI = "https://qa-scooter.praktikum-services.ru/";
    public static final String LOGIN = RandomStringUtils.randomAlphabetic(10);
    public static final String PASSWORD = "123";
    public static final String FIRST_NAME = RandomStringUtils.randomAlphabetic(15);

    ScooterServiceClient client = new ScooterServiceClient();
    public Courier courier;
    @Before
    public void setUp(){
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(SCOOTER_SERVICE_URI)
                .build();
        client.setRequestSpecification(requestSpec);
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
    @DisplayName("Courier is created")
    public void createCourierIsAvailable() {
        courier = new Courier
                .Builder()
                .withLogin(LOGIN)
                .withPassword(PASSWORD)
                .withFirstName(FIRST_NAME)
                .build();
        ValidatableResponse response = client.createCourier(courier);
        response.assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

    @Test
    @DisplayName("Create courier with duplicate login isn't available")
    public void createCourierWithDuplicateLoginIsNotAvailable() {
        courier = new Courier
                .Builder()
                .withLogin(LOGIN)
                .withFirstName(FIRST_NAME)
                .withPassword(PASSWORD)
                .build();
        client.createCourier(courier);
        Courier newCourier = new Courier
                .Builder()
                .withLogin(LOGIN)
                .withFirstName(FIRST_NAME+(int) (Math.random()*10))
                .withPassword(PASSWORD+(int) (Math.random()*10))
                .build();
        ValidatableResponse response = client.createCourier(newCourier);
        response.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);
    }

    @Test
    @DisplayName("Create duplicate courier isn't available")
    public void createDuplicateCourierIsNotAvailable() {
        courier = new Courier
                .Builder()
                .withLogin(LOGIN)
                .withFirstName(FIRST_NAME)
                .withPassword(PASSWORD)
                .build();
        client.createCourier(courier);
        ValidatableResponse  response= client.createCourier(courier);
        response.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);
    }

    @Test
    @DisplayName("Create courier without firstName is available")
    public void createCourierWithoutFirstNameIsAvailable() {
        courier = new Courier
                .Builder()
                .withLogin(LOGIN)
                .withPassword(PASSWORD)
                .build();
        ValidatableResponse response= client.createCourier(courier);
        response.assertThat().body("ok", equalTo(true)).and().statusCode(201);

    }

    @Test
    @DisplayName("Create courier without pass isn't available")
    public void createCourierWithoutPassIsNotAvailable() {
        courier = new Courier
                .Builder()
                .withLogin(LOGIN)
                .withFirstName(FIRST_NAME)
                .build();
        ValidatableResponse response= client.createCourier(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    @DisplayName("Create courier without login isn't available")
    public void createCourierWithoutLoginIsNotAvailable() {
        courier = new Courier
                .Builder()
                .withPassword(PASSWORD)
                .withFirstName(FIRST_NAME)
                .build();
        ValidatableResponse  response= client.createCourier(courier);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

}
