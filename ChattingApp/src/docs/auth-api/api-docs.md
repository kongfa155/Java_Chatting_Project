### Đăng ký tài khoản
#### Step 1: Đăng ký tài khoản
+ Endpoint: /api/users/register
+ Method: POST
+ Request body: 
```json
{
  "username": "string",
  "password": "string",
  "phone_number": "string",
  "display_name": "string",
  "avatar_url": "string",
  "gender": "boolean"
}
```
+ Response body:

Success (201 Created): Đăng ký thành công
```json
{
  "status": "success",
  "message": "User registered successfully",
  "data": {
    "username": "string",
    "status": "UNVERIFIED"
  }
}
```
  Error (400 Bad Request): Tài khoản đã tồn tại
```json
{
  "status": "error",
  "error_code": "USERNAME_EXISTS",
  "message": "Input username or phone number have been used, please use another username/phone number."
}
```
#### Step 2: Nhận OTP xác thực.
+ Endpoint: /api/users/get-register-otp
+ Method: POST
+ Request body:
```json
{
  "username": "string"
}
```
+ Response body:

Success (200 OK): Gửi mã OTP thành công
```json
{
  "status": "success",
  "message": "Sent OTP sucessfully",
  "data": null
}
```
Error (400 Bad Request): Tên đăng nhập không tồn tại
```json
{
  "status": "error",
  "error_code": "INVALID_USERNAME",
  "message": "Input username is invalid."
}
```
Error (400 Bad Request): Tài khoản đã xác nhận rồi
```json
{
  "status": "error",
  "error_code": "ACCOUNT_ALREADY_VERIFIED",
  "message": "Account have already been verified"
}
```

### Xác thực tài khoản
+ Endpoint: /api/users/verify
+ Method: POST
+ Request body:
```json
{
  "username": "string",
  "otp": "string"
}
```
+ Response body:

Success (200 OK): Xác thực thành công
```json
{
  "status": "success",
  "message": "Account verified successfully",
  "data": {
    "access_token": "string",
    "expires_in": "int",
    "user_id": "string",
    "username": "string",
    "display_name": "string",
    "gender": "boolean",
    "status": "ACTIVATED",
    "created_at": "timestamp"
  }
}
```
Error (400 Bad Request): Sai OTP
```json
{
  "status": "error",
  "error_code": "INVALID_OTP",
  "message": "Input otp is invalid."
}
```
### Đăng nhập tài khoản
+ Endpoint: /api/users/login
+ Method: POST
+ Request body:
```json
{
  "username": "string",
  "password": "string"
}
```
+ Response body:

Success (200 OK): Đăng nhập thành công
```json
{
  "status": "success",
  "message": "Login successfully",
  "data": {
    "user_id": "string",
    "username": "string",
    "display_name": "string",
    "avatar_url": "string",
    "access_token": "string",
    "expires_in": "int"
  }
}
```
Error (401 Unauthorized): Sai thông tin đăng nhập
```json
{
  "status": "error",
  "error_code": "INVALID_CREDENTIALS",
  "message": "Wrong username or password."
}
```
Error (403 Forbidden): Tài khoản chưa xác thực
```json
{
  "status": "error",
  "error_code": "ACCOUNT_NOT_VERIFIED",
  "message": "Account have not been verified."
}
```
### Đổi mật khẩu
+ Endpoint: /api/users/change-password
+ Method: PATCH
+ Request body:
```json
{
  "user_id": "string",
  "old_password": "string",
  "new_password": "string"
}
```
+ Response body:

Success (200 OK): Đổi mật khẩu thành công
```json
{
  "status": "success",
  "message": "Changed password successfully",
  "data": null
}
```
Error (400 Bad Request): Sai mật khẩu cũ
```json
{
  "status": "error",
  "error_code": "WRONG_OLD_PASSWORD",
  "message": "Wrong old password."
}
```
### Đổi số điện thoại
#### Step 1: Nhận OTP đổi số điện thoại
+ Endpoint: /api/users/get-change-phone-number-otp
+ Method: POST
+ Request body:
```json
{
  "user_id": "string",
  "password": "string",
  "new_phone_number": "string"
}
```
+ Response body:

Success (200 OK): Gửi mã OTP thành công
```json
{
  "status": "success",
  "message": "Sent OTP sucessfully",
  "data": null
}
```
Error (400 Bad Request): Sai mật khẩu
```json
{
  "status": "error",
  "error_code": "INVALID_CREDENTIALS",
  "message": "Wrong password."
}
```
Error (400 Bad Request): Số điện thoại đã có người sử dụng
```json
{
  "status": "error",
  "error_code": "PHONE_NUMBER_EXISTS",
  "message": "Phone number exists."
}
```
+ Endpoint: /api/users/change-phone-number
+ Method: PATCH
+ Request body:
```json
{
  "user_id": "string",
  "new_phone_number": "string",
  "otp": "string"
}
```
+ Response body:

Success (200 OK): Đổi số điện thoai thành công
```json
{
  "status": "success",
  "message": "Changed phone number successfully",
  "data": null
}
```
Error (400 Bad Request): Sai OTP
```json
{
  "status": "error",
  "error_code": "INVALID_OTP",
  "message": "Input otp is invalid."
}
```
### Đổi thông tin hiển thị
+ Endpoint: /api/users/profile
+ Method: PATCH
+ Request body:
```json
{
  "user_id": "string",
  "password": "string",
  "display_name": "string",
  "gender": "boolean",
  "avatar_url": "string"
}
```
+ Response body:

Success (200 OK): Đổi thông tin hiển thị thành công
```json
{
  "status": "success",
  "message": "Changed display information successfully",
  "data": {
    "user_id": "string",
    "username": "string",
    "display_name": "string",
    "gender": "boolean",
    "avatar_url": "string",
    "status": "ACTIVATED"
  }
}
```
Error (400 Bad Request): Sai mật khẩu 
```json
{
  "status": "error",
  "error_code": "INVALID_CREDENTIALS",
  "message": "Wrong password."
}
```
### Đổi mật khẩu qua quên mật khẩu
#### Step 1: Nhận OTP reset mật khẩu
+ Endpoint: /api/users/get-forget-password-otp
+ Method: POST
+ Request body:
```json
{
  "username": "string"
}
```
+ Response body:

Success (200 OK): Gửi mã OTP thành công
```json
{
  "status": "success",
  "message": "Sent OTP sucessfully",
  "data": null
}
```
Error (400 Bad Request): Tên đăng nhập không tồn tại
```json
{
  "status": "error",
  "error_code": "INVALID_USERNAME",
  "message": "Input username is invalid."
}
```


#### Step 2: Xác thực OTP
+ Endpoint: /api/users/forget-password
+ Method: PATCH
+ Request body:
```json
{
  "username": "string",
  "otp": "string",
  "new_password": "string"
}
```
+ Response body:

Success (200 OK): Đổi mật khẩu thành công
```json
{
  "status": "success",
  "message": "Password reset successfully",
  "data": {
    "user_id": "string",
    "username": "string",
    "display_name": "string",
    "avatar_url": "string",
    "access_token": "string",
    "expires_in": "int"
  }
}
```
Error (400 Bad Request): Tên đăng nhập không tồn tại
```json
{
  "status": "error",
  "error_code": "INVALID_USERNAME",
  "message": "Input username is invalid."
}
```
Error (400 Bad Request): Sai OTP
```json
{
  "status": "error",
  "error_code": "INVALID_OTP",
  "message": "Input otp is invalid."
}
```


