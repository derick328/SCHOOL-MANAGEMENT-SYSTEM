package com.school.sms.auth.service;

import com.school.sms.auth.dto.*;
import com.school.sms.auth.entity.User;
import com.school.sms.auth.repository.UserRepository;
import com.school.sms.common.enums.AccountStatus;
import com.school.sms.common.exception.AuthenticationException;
import com.school.sms.common.exception.BadRequestException;
import com.school.sms.common.exception.ResourceNotFoundException;
import com.school.sms.security.JwtTokenProvider;
import com.school.sms.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Authentication Service
 * Handles login, registration, password reset, and token management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Value("${app.security.max-login-attempts}")
    private int maxLoginAttempts;

    @Value("${app.security.lockout-duration-minutes}")
    private int lockoutDurationMinutes;

    /**
     * Login user and return JWT tokens
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // Find user
        User user = userRepository.findByUsernameOrEmail(
                request.getUsernameOrEmail(),
                request.getUsernameOrEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        // Check if account is locked
        if (user.isAccountLocked()) {
            throw new AuthenticationException(
                    "Account is locked until " + user.getAccountLockedUntil() +
                            ". Please try again later or contact administrator.");
        }

        // Check account status
        if (user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AuthenticationException(
                    "Account is " + user.getAccountStatus().getDisplayName().toLowerCase() +
                            ". Please contact administrator.");
        }

        try {
            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsernameOrEmail(),
                            request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reset failed login attempts
            if (user.getFailedLoginAttempts() > 0) {
                user.setFailedLoginAttempts(0);
                user.setAccountLockedUntil(null);
            }

            // Update last login
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            // Generate tokens
            String accessToken = jwtTokenProvider.generateToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(user);

            log.info("User logged in successfully: {}", user.getUsername());

            return buildLoginResponse(user, accessToken, refreshToken);

        } catch (org.springframework.security.core.AuthenticationException ex) {
            // Increment failed login attempts
            handleFailedLogin(user);
            throw new AuthenticationException("Invalid username or password");
        }
    }

    /**
     * Register new user
     */
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Create user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .gender(request.getGender())
                .accountStatus(AccountStatus.ACTIVE)
                .emailVerified(false)
                .failedLoginAttempts(0)
                .passwordExpired(false)
                .passwordChangedAt(LocalDateTime.now())
                .active(true)
                .build();

        // Generate email verification token
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        user.setEmailVerificationTokenExpiry(LocalDateTime.now().plusDays(1));

        userRepository.save(user);

        log.info("User registered successfully: {}", user.getUsername());

        // Auto-login after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return buildLoginResponse(user, accessToken, refreshToken);
    }

    /**
     * Logout user (client-side token deletion)
     */
    public void logout() {
        SecurityContextHolder.clearContext();
        log.info("User logged out successfully");
    }

    /**
     * Initiate forgot password process
     */
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Generate password reset token
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        // TODO: Send email with reset link
        log.info("Password reset token generated for user: {}", user.getUsername());
        log.info("Reset token: {}", resetToken); // Remove in production
    }

    /**
     * Reset password with token
     */
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // Validate passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Find user by reset token
        User user = userRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        // Check if token is expired
        if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token has expired");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setPasswordExpired(false);

        userRepository.save(user);

        log.info("Password reset successfully for user: {}", user.getUsername());
    }

    /**
     * Change password for authenticated user
     */
    @Transactional
    public void changePassword(ChangePasswordRequest request, Long userId) {
        // Validate passwords match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setPasswordExpired(false);

        userRepository.save(user);

        log.info("Password changed successfully for user: {}", user.getUsername());
    }

    /**
     * Refresh access token
     */
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // Validate refresh token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthenticationException("Invalid refresh token");
        }

        // Get user from token
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Check if user can login
        if (!user.canLogin()) {
            throw new AuthenticationException("Account is not active");
        }

        // Generate new tokens
        String newAccessToken = jwtTokenProvider.generateTokenFromUser(user);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        return buildLoginResponse(user, newAccessToken, newRefreshToken);
    }

    /**
     * Handle failed login attempts
     */
    private void handleFailedLogin(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= maxLoginAttempts) {
            user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(lockoutDurationMinutes));
            log.warn("Account locked due to too many failed login attempts: {}", user.getUsername());
        }

        userRepository.save(user);
    }

    /**
     * Build login response
     */
    private LoginResponse buildLoginResponse(User user, String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationTime() / 1000) // Convert to seconds
                .user(LoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .fullName(user.getFullName())
                        .role(user.getRole())
                        .profilePhotoUrl(user.getProfilePhotoUrl())
                        .build())
                .build();
    }
}
