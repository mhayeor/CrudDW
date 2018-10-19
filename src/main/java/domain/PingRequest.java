package domain;

/**
 * Created by User on 10/18/2018.
 */
public class PingRequest {

    private String input;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "PingRequest{" +
                "input='" + input + '\'' +
                '}';
    }
}