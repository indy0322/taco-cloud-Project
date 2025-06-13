package tacos.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tacos.data.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/register")
public class RegistrationController {
    private UserRepository userRepo;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public RegistrationController(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        userRepo.save(form.toUser(passwordEncoder));
        
        return "redirect:/login";
    }
    
}
