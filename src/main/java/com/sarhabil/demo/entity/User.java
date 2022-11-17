package com.sarhabil.demo.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sarhabil.demo.config.Constants;

import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "username"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        })
})
//@Table(name = "app_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}),@UniqueConstraint(columnNames = {"email"}))
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    
    @Column(length = 254, unique=true)
    @NotEmpty
    @Email(message = "Please enter a valid e-mail address")
    @Size(min = 5, max = 254)
    private String email;
    
//    @NotNull
//    @NotEmpty
////    @Size(min = 2, max = 30)
//    @Length(min = 5 , message = "Password should be least 5 characters")
//    @ValidPassword
    @NonNull
    @NotBlank(message = "New Password is mandatory")
    private String password;
    
 //   @NotNull
//    @NotEmpty
//    @Size(min = 2, max = 30)
//    @Length(min = 5 , message = "Password should be least 5 characters")
//    @ValidPassword
    @NonNull
    @NotBlank(message = "Confirm Password is mandatory")
    @Transient
    private String confirmPassword; 

    @NotNull
    @Column(nullable = false)
    @Value("false")
    private boolean activated;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;
//  @JsonIgnore
//    @ManyToMany(fetch = FetchType.LAZY)
    @ManyToMany
    @JoinTable(name = "user_roles", 
      joinColumns = @JoinColumn(name = "user_id" , referencedColumnName = "id"), 
      inverseJoinColumns = @JoinColumn(name = "role_id" , referencedColumnName = "name"))
    @BatchSize(size = 20)
    private Set<Role> roles = new HashSet<>();
    
 // relationship with active mail

 	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
     private VerificationEmailByToken verificationToken;
 	
 // relationship with resetPassword

 	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
     private ResetPasswords resetPassword;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public VerificationEmailByToken getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationEmailByToken verificationToken) {
		this.verificationToken = verificationToken;
	}

	public ResetPasswords getResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(ResetPasswords resetPassword) {
		this.resetPassword = resetPassword;
	}

	public User() {
		super();
	}

	public User(long id, @NotNull @Pattern(regexp = "^[_.@A-Za-z0-9-]*$") @Size(min = 1, max = 50) String username,
			@Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotEmpty @Email(message = "Please enter a valid e-mail address") @Size(min = 5, max = 254) String email,
			@NotBlank(message = "Confirm Password is mandatory") String password,
			@NotBlank(message = "Confirm Password is mandatory") String confirmPassword, @NotNull boolean activated,
			@Size(min = 2, max = 10) String langKey, Set<Role> roles, VerificationEmailByToken verificationToken,
			ResetPasswords resetPassword) {
		super();
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.activated = activated;
		this.langKey = langKey;
		this.roles = roles;
		this.verificationToken = verificationToken;
		this.resetPassword = resetPassword;
	}
	

	public User(@NotNull @Pattern(regexp = "^[_.@A-Za-z0-9-]*$") @Size(min = 1, max = 50) String username,
			@Size(max = 50) String firstName,
			@NotEmpty @Email(message = "Please enter a valid e-mail address") @Size(min = 5, max = 254) String email,
			@NotBlank(message = "Confirm Password is mandatory") String password) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.email = email;
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", password=" + password + ", confirmPassword=" + confirmPassword
				+ ", activated=" + activated + ", langKey=" + langKey + ", roles=" + roles + ", verificationToken="
				+ verificationToken + ", resetPassword=" + resetPassword + "]";
	}

     
}
