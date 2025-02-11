package conf;

import java.util.UUID;

/**
 * Java has a problem with extremes when it comes to extension.
 * In the past, either a class was final (so no extension) or
 * "open" which implied infinite extensions, such as InvalidAttendee
 * here. With the more modern "sealed" classes it is possible to
 * create a more controlled and finite hierarchy for extensions.
 * In this example, Alumnus, Attendee, Speaker, Staff and
 * VendorSponsor are all allowed extensions, while
 * InvalidAttendee needs to be commented out.
 * <p>
 * NOTE: This class hierarchy shows the usage of sealed classes
 */
//FIXME_8_: Convert to Sealed type
public class AllowedPerson {
    protected String firstName;
    protected String lastName;

    public AllowedPerson() {
    }

    public AllowedPerson(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "AllowedPerson{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

//NOTE: Possible to have an "Alumnus" subclass of Attendee

//NOTE: No extensions allowed for Speaker

//NOTE: Staff can possibly be further extended into Admin, FrontDesk, Security and many more

//NOTE: No extensions allowed for VendorSponsor

//FIXME_8_: Show as not allowed in sealed hierarchy
class InvalidAttendee extends AllowedPerson {
    public InvalidAttendee(String firstName, String lastName) {
        super(firstName, lastName);
    }
}
