package service;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.Order;
import static io.restassured.RestAssured.given;

public class OrderService {

    private RequestSpecification requestSpecification;
    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
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
