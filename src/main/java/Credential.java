public class Credential {

    private String login;
    private String password;

    public static Credential fromCourier(Courier courier){
        return new Credential
                .Builder()
                .withLogin(courier.getLogin())
                .withPassword(courier.getPassword())
                .build();
    }

    public static class Builder{
        private Credential newCredential;
        public Builder(){
            newCredential = new Credential();
        }
        public Builder withLogin(String login){
            newCredential.login = login;
            return this;
        }
        public Builder withPassword(String password){
            newCredential.password = password;
            return this;
        }
        public Credential build(){
            return newCredential;
        }
    }
}
