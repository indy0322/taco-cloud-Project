package tacos.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import tacos.data.UserRepository;
import tacos.User;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //PsswordEncoder는 비밀번호를 암호화하고, 비교하고, 암호화를 강화하는 메소드가 있는 인터페이스이다. Spring Security에서 제공한다.
        return new BCryptPasswordEncoder();
        //BCryptPasswordEncoder는 Spring Security에서 제공하는 클래스 중 하나이다. 비밀번호를 암호화하는데 사용한다.
    }

    /*@Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        //UserDetails는 Spring Security에서 사용자의 정보를 담는 인터페이스이다.
        //UserDetailsService는 Spring Security에서 우저의 정보를 가져오는 인터페이스이다. 리턴 타입은 UserDetails이다.
        
        List<UserDetails> userList = new ArrayList<>();

        userList.add(new User("buzz", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
        //SimpleGrantedAuthority는 spring security에서 사용자의 권한을 표현할 때 사용하는 클래스이다. 보통 사용자의 역할을 나타낼 때 쓰인다.
        
        userList.add(new User("woody", encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));

        return new InMemoryUserDetailsManager(userList);

        
        
    }*/

    @Bean
    public  UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);

            if(user != null) {
                List<SimpleGrantedAuthority> authorities = Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_USER"));

                return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
            }
            throw new UsernameNotFoundException("User '" + username + "'not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/design", "/orders").hasRole("USER")
                .requestMatchers("/", "/**").permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/design",true)  
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
            )

            .build();
    }

    
}
