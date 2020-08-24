package by.it.academy;

public class Recipient {

    private String emailAddress;
    private String mobilePhone;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "emailAddress='" + emailAddress + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                '}';
    }
}
