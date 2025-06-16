package ads.dacha.services.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

@Controller
public class AuthController {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    //private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        //this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    public String showAuthForm(@RequestParam(required = false) Boolean error, 
                             Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверные учетные данные");
        }
        model.addAttribute("authDto", new AuthDto());
        return "auth";
    }
    /*public String auth(Model model) {
        if (!model.containsAttribute("authDto")) {
            model.addAttribute("authDto", new AuthDto());
        }
        return "auth";
    }*/

    @GetMapping("/reg")
    public String reg(Model model) {
        model.addAttribute("user", new User());
        return "reg";
    }

    /*@PostMapping("/auth")
    public String processAuth(
            @ModelAttribute("authDto") AuthDto authDto,
            HttpServletRequest request,
            Model model) {

                try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authDto.getEmail(),
                    authDto.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
            
        } catch (Exception e) {
            model.addAttribute("error", "Неверный email или пароль");
            return "auth";
        }*/
        
        /*User user = userRepo.findByEmail(authDto.getEmail())
                .orElse(null);

        if (user == null) {
            model.addAttribute("error", "Пользователь с таким email не найден");
            model.addAttribute("authDto", authDto);
            return "auth";
        }

        System.out.println("=== PASSWORD VERIFICATION ===");
        System.out.println("Input: " + authDto.getPassword().trim());
        System.out.println("Stored: " + user.getPassword());
        System.out.println("New hash: " + passwordEncoder.encode(authDto.getPassword().trim()));
    
        if (!passwordEncoder.matches(authDto.getPassword().trim(), user.getPassword())) {
        model.addAttribute("error", "Неверный пароль");
        model.addAttribute("authDto", authDto);
        return "auth";
        }
        if (user == null || !passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
            model.addAttribute("error", "Неверный email или пароль");
            model.addAttribute("authDto", authDto);
            return "auth";
        }

        return "redirect:/";
    }*/

    @PostMapping("/reg")
    public String processReg(
            @ModelAttribute("user") User user,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        System.out.println("=== DEBUG ===");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password (raw): " + user.getPassword());
        System.out.println("Confirm Password: " + confirmPassword);
        
        if (user.getPassword().startsWith("$2a$")) {
        model.addAttribute("error", "Ошибка: пароль уже захеширован");
        return "reg";
        }
        
        if (userRepo.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email уже используется");
            model.addAttribute("user", user);
            return "reg";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("user", user); // Сохраняем введенные данные
            return "reg";
        }

        if (user.getPassword().length() < 8) {
            model.addAttribute("error", "Пароль должен содержать минимум 8 символов");
            model.addAttribute("user", user);
            return "reg";
        }

        String encodedPassword = passwordEncoder.encode(confirmPassword.trim());
        System.out.println("Encoded during registration: " + encodedPassword);

        /*if (!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("user", user); // Возвращаем введенные данные
            return "reg";
        }*/

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return "redirect:/auth?success";
    }

}
