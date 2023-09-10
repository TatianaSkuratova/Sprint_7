
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
public class ScooterServiceClient {
    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;

    }

    private RequestSpecification requestSpecification;

    public ValidatableResponse createCourier (Courier courier){
        return (ValidatableResponse) given()
                .spec(requestSpecification)
                .log()
                .all()
                .body(courier)
                .post(Endpoints.CREATE_COURIER_ENDPOINT)
                .then()
                .log()
                .all();

    }

    public ValidatableResponse login(Credential credentials){
        return given()
                .spec(requestSpecification)
                .log()
                .all()
                .body(credentials)
                .post(Endpoints.LOGIN_COURIER_ENDPOINT)
                .then()
                .log()
                .all();
    }

    public ValidatableResponse deleteCourierById(Integer id){
        return given()
                .spec(requestSpecification)
                .log()
                .all()
                .delete(Endpoints.DELETE_COURIER_ENDPOINT, id)
                .then()
                .log()
                .all();
    }
    public ValidatableResponse createOrder(Order order){
        return given()
                .spec(requestSpecification)
                .log()
                .all()
                .body(order)
                .post(Endpoints.CREATE_ORDER_ENDPOINT)
                .then()
                .log()
                .all();
    }

    public ValidatableResponse getOrdersByCourierId(int courierId){
        return given().spec(requestSpecification)
                .log()
                .all()
                .queryParam("courierId", courierId)
                .get("/api/v1/orders")
                .then()
                .log()
                .all();
    }

    public ValidatableResponse getIdOrderByTrack(int track){
        return given().spec(requestSpecification)
                .log()
                .all()
                .queryParam("t",track)
                .get(Endpoints.GET_ID_ORDER)
                .then()
                .log()
                .all();
    }
    public ValidatableResponse acceptOrderToCourier(int orderId, int courierId){
        return given().spec(requestSpecification)
                .log()
                .all()
                .queryParam("courierId",courierId )
                .put(Endpoints.PUT_ACCEPT_ORDER, orderId)
                .then()
                .log()
                .all();


    }

}
