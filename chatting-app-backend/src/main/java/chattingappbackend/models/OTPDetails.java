package chattingappbackend.models;

public class OTPDetails {
    private final String code;
    private final long expiryTime;

    public OTPDetails(String code, int durationInMinutes){
        this.code = code;
        this.expiryTime = System.currentTimeMillis()+(durationInMinutes*60*1000L);
    }
    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
    public String getCode() { return code; }
}
