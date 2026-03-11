package chattingappbackend.models;

public class OTPDetails {
    private final String code;
    private final String target;
    private final long expiryTime;


    public OTPDetails(String code, String target, int durationInMinutes){
        this.code = code;
        this.target = target;
        this.expiryTime = System.currentTimeMillis()+(durationInMinutes*60*1000L);
    }
    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
    public String getCode() { return code; }

    public String getTarget() {
        return target;
    }

    public long getExpiryTime() {
        return expiryTime;
    }
}
