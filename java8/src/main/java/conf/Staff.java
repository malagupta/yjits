package conf;

public class Staff extends AllowedPerson {
    private String hatSize;

    public Staff(String firstName, String lastName, String hatSize) {
        super(firstName, lastName);
        this.hatSize = hatSize;
    }

    public String getHatSize() {
        return hatSize;
    }

    public void setHatSize(String hatSize) {
        this.hatSize = hatSize;
    }

    @Override
    public String toString() {
        return "\n\tStaff{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hatSize='" + hatSize + '\'' +
                '}';
    }
}