package com.security.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Application user implementing Spring Security's {@link UserDetails}.
 * Can be either a customer or an admin based on the associated role.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
public class User implements UserDetails {

    // Manual getters and setters (Lombok provides them, but the original had them)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String username;

    private String password;

    private String address;

    private Long number;                // phone number

    private String identityProof;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "role_id")
    private Role roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accountList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Investment> investmentList = new ArrayList<>();

    public User() {
    }

    /**
     * Returns authorities based on the assigned role name.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (roles != null) {
            authorities.add(new SimpleGrantedAuthority(roles.getRoleName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setIdentityProof(String identityProof) {
        this.identityProof = identityProof;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public void setInvestmentList(List<Investment> investmentList) {
        this.investmentList = investmentList;
    }
}