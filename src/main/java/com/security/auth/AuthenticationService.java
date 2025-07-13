//package com.security.auth;
//
//import com.security.config.JwtService;
//import com.security.user.ROLE;
//import com.security.user.User;
//import com.security.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//
//    private final UserRepository repository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    private final JwtService jwtService;
//
//    private final AuthenticationManager authenticationManager;
//
//    public AuthenticationResponse register(RegisterRequest request) {
//        var user = User.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(ROLE.USER)
//                .build();
//        repository.save(user);
//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//        var user = repository.findByEmail(request.getEmail())
//                .orElseThrow();
//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//
//
//    }
//}

package com.security.auth;

import com.security.config.EmailService;
import com.security.config.JwtService;
import com.security.user.ROLE;
import com.security.user.User;
import com.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationResponse register(RegisterRequest request) {
        // Build user entity
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(ROLE.USER)
                .build();

        // Save user to DB
        repository.save(user);

        // Email subject and body
        String subject = "You are successfully authenticated !!🎉";
        String body = "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "<head><meta charset='UTF-8'><title>JWT Authentication and Mail Sender</title></head>"
                + "<body>"
                + "<div style='max-width: 600px; margin: 40px auto; padding: 30px; font-family: Segoe UI, Tahoma, Geneva, Verdana, sans-serif; background: linear-gradient(to bottom, #f9f9ff, #ffffff); color: #333333; border-radius: 12px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);'>"

                + "<div style='text-align: center; margin-bottom: 25px;'>"
                + "<svg width='60' height='60' viewBox='0 0 24 24' fill='none' xmlns='http://www.w3.org/2000/svg' style='margin-bottom: 15px;'>"
                + "<path d='M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22Z' fill='#6366F1' stroke='#6366F1' stroke-width='2'/>"
                + "<path d='M8 14C8 14 9.5 16 12 16C14.5 16 16 14 16 14' stroke='white' stroke-width='2' stroke-linecap='round'/>"
                + "<path d='M9 9H9.01' stroke='white' stroke-width='2' stroke-linecap='round'/>"
                + "<path d='M15 9H15.01' stroke='white' stroke-width='2' stroke-linecap='round'/>"
                + "</svg>"
                + "<h1 style='margin: 0; color: #4F46E5; font-size: 28px; font-weight: 600;'>Welcome, " + request.getFirstName() + "!</h1>"
                + "<p style='color: #64748B; margin-top: 8px;'>Your secure account is now ready to use</p>"
                + "</div>"

                + "<img src='cid:jwtImage' alt='JWT Authentication Flow' style='width: 100%; border-radius: 8px; margin: 20px 0; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);' />"

                + "<div style='background-color: #F0F9FF; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #4F46E5;'>"
                + "<p style='font-size: 15px; line-height: 1.6; margin: 0; color: #334155;'>Thank you for registering with our application. Your account has been <strong style='color: #4F46E5;'>successfully created</strong> and is now protected with <strong style='color: #4F46E5;'>JWT Authentication</strong>.</p>"
                + "</div>"

                + "<div style='display: flex; margin: 25px 0;'>"
                + "<div style='flex: 1; padding: 15px; background-color: #EEF2FF; border-radius: 8px; margin-right: 10px;'>"
                + "<h3 style='margin-top: 0; color: #4F46E5;'>🔒 Secure Access</h3>"
                + "<p style='font-size: 14px; line-height: 1.5; color: #475569;'>JWT tokens ensure your session is protected with industry-standard security.</p>"
                + "</div>"
                + "<div style='flex: 1; padding: 15px; background-color: #ECFDF5; border-radius: 8px; margin-left: 10px;'>"
                + "<h3 style='margin-top: 0; color: #059669;'>📬 Reliable Service</h3>"
                + "<p style='font-size: 14px; line-height: 1.5; color: #475569;'>Our Spring Boot mail system guarantees you'll receive important notifications.</p>"
                + "</div>"
                + "</div>"

                + "<div style='background-color: #F8FAFC; padding: 20px; border-radius: 8px; margin-top: 30px; text-align: center;'>"
                + "<p style='font-size: 14px; margin: 0; color: #64748B;'>Need help or have questions?</p>"
                + "<p style='font-size: 16px; margin: 10px 0 0 0;'>"
                + "<a href='mailto:mdmozammilraza06@gmail.com' style='color: #4F46E5; text-decoration: none; font-weight: 500;'>📧 Contact our support team</a>"
                + "</p>"
                + "</div>"

                + "<div style='margin-top: 40px; padding-top: 20px; border-top: 1px solid #E2E8F0; text-align: center;'>"
                + "<p style='font-size: 13px; color: #64748B; margin-bottom: 5px;'>Best regards,</p>"
                + "<p style='font-size: 15px; color: #1E293B; font-weight: 600; margin: 0;'>Mozammil Raza</p>"
                + "<p style='font-size: 13px; color: #64748B; margin-top: 5px;'>📞 +91 62059 14390</p>"
                + "</div>"

                + "</div>"
                + "</body>"
                + "</html>";

        // Send HTML email
        emailService.sendEmailWithImage(request.getEmail(), subject, body);

        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
