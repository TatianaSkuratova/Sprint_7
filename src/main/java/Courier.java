import java.time.Period;

public class Courier {

    String login;
    String password;
    String firstName;

    public static class Builder{
        private Courier newCourier;
        public Builder(){
            newCourier = new Courier();
        }
        public Builder withLogin(String login){
            newCourier.login = login;
            return this;
        }
        public Builder withPassword(String password){
            newCourier.password = password;
            return this;
        }
        public Builder withFirstName(String firstName){
            newCourier.firstName = firstName;
            return this;
        }
        public Courier build(){
            return newCourier;
        }
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

}
