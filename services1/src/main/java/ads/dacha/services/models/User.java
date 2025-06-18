package ads.dacha.services.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String secondname;
    private String password;
    private String email;
    private String phone;
    private String role = "USER";



    public Long getId() {
        return id;
    } 
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }
    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password; // Просто присваиваем значение
    }
    /*public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }*/

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
