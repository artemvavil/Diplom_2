package json;

public class CreatingUser {
    private String email;
    private String password;
    private String name;

    public CreatingUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public CreatingUser(){}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

}
