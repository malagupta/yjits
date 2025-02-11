package conf;

import java.util.UUID;

public class Attendee extends AllowedPerson {
    private PaymentType paymentType;
    private final String uniqueId = UUID.randomUUID().toString();

    public Attendee(String firstName, String lastName, PaymentType paymentType) {
        super(firstName, lastName);
        if(paymentType == null) {
            throw new IllegalArgumentException("Invalid Payment type");
        }
        this.paymentType = paymentType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public String toString() {
        return "\n\tAttendee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", paymentType='" + paymentType + '\'' +
                '}';
    }
}