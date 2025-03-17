import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(AuthRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthResponse(jwtUtil.generateAccessToken(user.getEmail()), refreshToken);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(email);
        String newRefreshToken = jwtUtil.generateRefreshToken(email);

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}
