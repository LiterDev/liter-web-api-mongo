package io.liter.web.api.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Auth implements UserDetails {

    @Id
    private String userId;

    private String username;

    @JsonIgnore
    private String password;

    private List<String> roles = new ArrayList<>();

    private List<AuthScope> scopes = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return this.roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
        //log.debug("]-----] Auth::getAuthorities [-----[ {}", this.scopes.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(toList()));
        return this.scopes.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String getUsername() {
        return this.username;
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
