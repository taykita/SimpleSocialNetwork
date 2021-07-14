package mainLogic;

public class WelcomeLogic {
    public String getWelcomeString(String name, String surname) {
        String welcomeString = "Привет";
        if (name != null) {
            welcomeString += ", " + name;
            if (surname != null) {
                welcomeString += " " + surname;
            }
        }
        return welcomeString;
    }
}
