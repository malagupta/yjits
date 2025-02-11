package conf;

import java.util.UUID;

public class Speaker extends AllowedPerson {
    private String shirtSize;
    private final String uniqueId = UUID.randomUUID().toString();

    public Speaker(String firstName, String lastName, String shirtSize) {
        super(firstName, lastName);
        this.shirtSize = shirtSize;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public String toString() {
        return "\n\tSpeaker{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", shirtSize='" + shirtSize + '\'' +
                '}';
    }
}