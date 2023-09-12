package service;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.Courier;
import models.Credential;
import models.Order;

import static io.restassured.RestAssured.given;
public class CourierService {
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

}
