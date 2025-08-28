package ads.dacha.services.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ads.dacha.services.models.AuthDto;
import ads.dacha.services.models.User;
import ads.dacha.services.models.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/auth")
    public String showAuthForm(@RequestParam(required = false) String error, 
                             @RequestParam(required = false) String logout,
                             Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверные учетные данные");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы");
        }
        if (!model.containsAttribute("authDto")) {
            model.addAttribute("authDto", new AuthDto());
        }
        return "auth";
    }

    @GetMapping("/reg")
    public String reg(Model model) {
        model.addAttribute("user", new User());
        return "reg";
    }

    @PostMapping("/auth")
    public String processAuth(
            @ModelAttribute("authDto") AuthDto authDto,
            HttpSession session,
            Model model) {

        // Проверяем, что поля не пустые
        if (authDto.getEmail() == null || authDto.getEmail().trim().isEmpty()) {
            model.addAttribute("error", "Email не может быть пустым");
            model.addAttribute("authDto", authDto);
            return "auth";
        }
        
        if (authDto.getPassword() == null || authDto.getPassword().trim().isEmpty()) {
            model.addAttribute("error", "Пароль не может быть пустым");
            model.addAttribute("authDto", authDto);
            return "auth";
        }

        User user = userRepo.findByEmail(authDto.getEmail().trim()).orElse(null);

        if (user == null) {
            model.addAttribute("error", "Пользователь с таким email не найден");
            model.addAttribute("authDto", authDto);
            return "auth";
        }

        System.out.println("=== PASSWORD VERIFICATION ===");
        System.out.println("Input: '" + authDto.getPassword().trim() + "'");
        System.out.println("Stored: '" + user.getPassword() + "'");
        System.out.println("Matches: " + passwordEncoder.matches(authDto.getPassword().trim(), user.getPassword()));
    
        if (!passwordEncoder.matches(authDto.getPassword().trim(), user.getPassword())) {
            model.addAttribute("error", "Неверный пароль");
            model.addAttribute("authDto", authDto);
            return "auth";
        }

        // Создаем объект аутентификации с правами пользователя
        UsernamePasswordAuthenticationToken authToken = 
            new UsernamePasswordAuthenticationToken(
                user.getEmail(), 
                authDto.getPassword().trim(),
                AuthorityUtils.createAuthorityList("ROLE_USER")
            );
        
        // Создаем SecurityContext и устанавливаем в него аутентификацию
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authToken);
        SecurityContextHolder.setContext(securityContext);
        
        // Сохраняем SecurityContext в сессии
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        
        // Устанавливаем время жизни сессии (1 час)
        session.setMaxInactiveInterval(3600); // 3600 секунд = 1 час
        session.setAttribute("currentUser", user);
        
        // Перенаправляем на страницу профиля после успешной авторизации
        return "redirect:/profile";
    }

    @PostMapping("/reg")
    public String processReg(
            @ModelAttribute("user") User user,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        System.out.println("=== REGISTRATION DEBUG ===");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password (raw): " + user.getPassword());
        System.out.println("Confirm Password: " + confirmPassword);
        
        // Проверка на пустые значения
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            model.addAttribute("error", "Email не может быть пустым");
            model.addAttribute("user", user);
            return "reg";
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            model.addAttribute("error", "Пароль не может быть пустым");
            model.addAttribute("user", user);
            return "reg";
        }
        
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            model.addAttribute("error", "Подтверждение пароля не может быть пустым");
            model.addAttribute("user", user);
            return "reg";
        }

        if (userRepo.existsByEmail(user.getEmail().trim())) {
            model.addAttribute("error", "Email уже используется");
            model.addAttribute("user", user);
            return "reg";
        }
        
        if (!user.getPassword().trim().equals(confirmPassword.trim())) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("user", user);
            return "reg";
        }

        if (user.getPassword().trim().length() < 8) {
            model.addAttribute("error", "Пароль должен содержать минимум 8 символов");
            model.addAttribute("user", user);
            return "reg";
        }

        // Хешируем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        user.setEmail(user.getEmail().trim()); // Убираем лишние пробелы
        
        try {
            userRepo.save(user);
            System.out.println("User saved successfully with encoded password: " + user.getPassword());
            return "redirect:/auth?success=registered";
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            model.addAttribute("error", "Ошибка при регистрации. Попробуйте позже.");
            model.addAttribute("user", user);
            return "reg";
        }
    }
}
