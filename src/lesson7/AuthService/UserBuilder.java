package lesson7.AuthService;

public class UserBuilder {
    private String login = "";
    private String password = "";
    private String nickname = "";
    private boolean isOnline;

    //region Setters
    public UserBuilder setLogin(String login){
        this.login = login;
        return this;
    }

    public UserBuilder setPassword(String password){
        this.password = password;
        return this;
    }

    public UserBuilder setNickname(String nickname){
        this.nickname = nickname;
        return this;
    }

    public UserBuilder isOnline(boolean isOnline){
        this.isOnline = isOnline;
        return this;
    }
    //endregion

    //region Builds

    //endregion

    public User build(){
        return new User(login, password,nickname,isOnline);
    }
}
