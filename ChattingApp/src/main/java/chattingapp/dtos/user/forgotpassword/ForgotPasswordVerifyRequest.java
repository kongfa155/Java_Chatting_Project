package chattingapp.dtos.user.forgotpassword;


import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordVerifyRequest(
        @SerializedName("username")
        @NotBlank(message = "Username cannot be blank")
        String username,
        @SerializedName("otp")
        @NotBlank(message = "OTP cannot be blank")
        @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters")
        String otp,
        @SerializedName("new_password")
        @NotBlank(message = "New password cannot be blank")
        @Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
        String newPassword
) {}