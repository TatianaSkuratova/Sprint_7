package models;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Courier {

    private String login;
    private String password;
    private String firstName;

    public static class Builder{
        private Courier newCourier;
        public Builder(){
            newCourier = new Courier();
        }
        public Builder login(String login){
            newCourier.login = login;
            return this;
        }
        public Builder password(String password){
            newCourier.password = password;
            return this;
        }
        public Builder firstName(String firstName){
            newCourier.firstName = firstName;
            return this;
        }
        public Courier build(){
            return newCourier;
        }
    }
}
