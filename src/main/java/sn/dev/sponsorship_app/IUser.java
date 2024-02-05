package sn.dev.sponsorship_app;

public interface IUser {
    public User seConnecter(String email, String password);

    public String generateLogin(String nom, String prenom);
}
