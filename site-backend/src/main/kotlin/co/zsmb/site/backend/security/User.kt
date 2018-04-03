package co.zsmb.site.backend.security

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails

@Document(collection = "users")
data class User(var name: String? = null,
                var pass: String? = null,
                var active: Boolean = true,
                var roles: Array<String> = emptyArray(),
                @Id var id: String? = null)
    : UserDetails {

    @JsonIgnore
    override fun getUsername() = name

    @JsonIgnore
    override fun getPassword() = pass

    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return AuthorityUtils.createAuthorityList(*roles)
    }

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean {
        return active
    }

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean {
        return active
    }

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean {
        return active
    }

    @JsonIgnore
    override fun isEnabled(): Boolean {
        return active
    }

}