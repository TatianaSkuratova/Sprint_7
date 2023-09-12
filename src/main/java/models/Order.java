package models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public static class Builder{
        private Order newOrder;
        public Builder(){
            newOrder = new Order();
        }
        public Builder withFirstName(String firstName){
            newOrder.firstName = firstName;
            return this;
        }
        public Builder withLastName(String lastName){
            newOrder.lastName = lastName;
            return this;
        }
        public Builder withAddress(String address){
            newOrder.address = address;
            return this;
        }
        public Builder withMetroStation(String metroStation){
            newOrder.metroStation = metroStation;
            return this;
        }
        public Builder withPhone(String phone){
            newOrder.phone = phone;
            return this;
        }
        public Builder withRentTime(Integer rentTime){
            newOrder.rentTime = rentTime;
            return this;
        }
        public Builder withDeliveryDate(String deliveryDate){
            newOrder.deliveryDate = deliveryDate;
            return this;
        }
        public Builder withComment(String comment){
            newOrder.comment = comment;
            return this;
        }
        public Builder withColor(String [] color){
            newOrder.color = color;
            return this;
        }
        public Order build(){
            return newOrder;
        }

    }
}
