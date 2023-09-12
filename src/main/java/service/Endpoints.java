package service;

public final class Endpoints {
    public static final String SCOOTER_SERVICE_URI = "https://qa-scooter.praktikum-services.ru/";
    public static final String CREATE_COURIER_ENDPOINT = "api/v1/courier";
    public static final String LOGIN_COURIER_ENDPOINT = "api/v1/courier/login";
    public static final String DELETE_COURIER_ENDPOINT = "/api/v1/courier/{id}";
    public static final String CREATE_ORDER_ENDPOINT = "/api/v1/orders";
    public static final String GET_ID_ORDER = "/api/v1/orders/track";
    public static final String PUT_ACCEPT_ORDER = "/api/v1/orders/accept/{id}";
}
