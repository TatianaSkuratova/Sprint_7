package service;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrdersService {
    private RequestSpecification requestSpecification;
    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
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
}
