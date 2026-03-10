/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingappbackend.utils;

import chattingappbackend.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author CP
 */
@Component
public class JwtUtil {

    @Autowired
    private JwtService jwtService;

    public String getUserIdFromRequest(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }

        String token = header.substring(7);

        return jwtService.extractUserId(token);
    }
}
