package tqs.cloudit.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configurations related to authentication
 *
 * @author aspedrosa
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    /**
     * JDBC data source used to get usernames and passwords
     * Configurations on src/main/resources/application.settings
     */
    
    @Autowired
    DataSource dataSource;

    /**
     * Password encoder to be used on user authentication on the api
     */
    private PasswordEncoder passwordEncoder;

    /**
     * Configure security of api
     *  <ul>
     *      <li>which paths need to be authenticated</li>
     *      <li>type of authentication</li>
     *      <li>logout behaviour</li>
     *  </ul>
     *
     * @param http where the configurations will be stored
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/joboffer").authenticated()
            .antMatchers("/company","/hire/id/**", "/joboffer/self", "/messages", "/messages/id/**", "/login", "/profile", "/favourite/**").authenticated()
            .antMatchers(HttpMethod.GET, "/joboffer").permitAll()
            .anyRequest().permitAll()
            .and().httpBasic()
            .and().logout().logoutUrl("/logout").deleteCookies("JSESSIONID")
            .and().csrf().disable();
    }

    /**
     * Configure from where the authentication credentials will be retrieved
     *
     * @param auth builder of a authentication manager (dependency injection)
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
        .usersByUsernameQuery("select username, password, true from user where username=?")
        .authoritiesByUsernameQuery("select ?, \"USER\"");
    }

    /**
     * Provides a unique class for a password encoder (Singleton)
     *
     * @return the unique instance of a BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        if (passwordEncoder == null) {
            passwordEncoder = new BCryptPasswordEncoder();
        }
        return passwordEncoder;
    }
}
