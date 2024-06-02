package com.utp.creacionesjoaquin.security.model;

import com.utp.creacionesjoaquin.security.enums.RolName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class MainUser implements UserDetails {
    /*private String firstName;
    private String lastName;*/
    private String username;
    private String email;
    private String password;
    private Set<RolName> roles;

    public static MainUser build( User user ){
        return new MainUser(
                /*user.getPersonalInformation().getFirstName(),
                user.getPersonalInformation().getLastName(),*/
                user.getEmail(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map( Role::getRolName ).collect(Collectors.toSet())
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map( RolName::name ).map( SimpleGrantedAuthority::new ).collect(Collectors.toList());
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
}
