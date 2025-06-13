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

import jakarta.servlet.http.HttpSession;
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
        return username -> { //이 코드에서 username은 login.html에서 <form> 안에 <input type="text" name="username" id="username" />에서 가져온 값이다. 
            User user = userRepo.findByUsername(username);

            if(user != null) {
                List<SimpleGrantedAuthority> authorities = Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_USER"));

                return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities); //비밀번호는 BCryptPasswordEncoder로 암호화된 상태로 비교됨. 사용자가 입력한 비밀번호는 login.html의 <form>의 <input type="password" name="password" id="password" />에서 가져온다.
                //Spring Security가 name="password"을 보고 자동으로 비교해준다.
                //Spring Security가 로그인 시 인식하는 필드명은 name="username", name="password"이다.
            }
            throw new UsernameNotFoundException("User '" + username + "'not found");//user 값이 없을 경우 UsernameNotFoundException예외를 발생시킨다.
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {//Spring Security가 자동적으로 /login(기본 로그인 폼 경로) URL을 만듦. 따로 GET,POST 매핑을 만들필요가 없음. 
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/design", "/orders").hasRole("USER")
                .requestMatchers("/", "/**").permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login") //직접 만든 login.html을 사용하겠다는 의미
                .defaultSuccessUrl("/design",true)  
                .successHandler((request, response, authentication) -> {//successHandler()는 Spring Security에서 로그인 성공 후 실행할 동작을 지정하는 코드이다.
                    //request는 클라이언트 요청 객체, response는 서버 응답 객체, authentication은 로그인에 성공한 사용자 정보를 담고 있는 객체
                    org.springframework.security.core.userdetails.User userDetails = 
                        (org.springframework.security.core.userdetails.User) authentication.getPrincipal(); //로그인한 사용자의 정보를 가져오는 코드

                    // 사용자 이름을 세션에 저장
                    HttpSession session = request.getSession();
                    session.setAttribute("username", userDetails.getUsername());
                    
                    response.sendRedirect("/design");
                })
            )
            .logout(logout -> logout // /logout 경로도 Spring Security가 자동으로 만든다.
                .logoutSuccessUrl("/")
            )

            .build();
    }

    
}
