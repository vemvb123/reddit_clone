package com.example.Reddit.clone.Config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalAuthentication
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http
                .csrf().disable()
                .authorizeHttpRequests()

                .requestMatchers(
                        "/post/{page}",
                        "/post/not_logged_in/{postId}",
                        "/post/not_logged_in/{page}/{username}",
                        "/post/not_logged_in/{page}/{communityName}",
                        "/post/{communityName}",
                        "post/{postId}",

                        "/comment/latest_not_logged_in/{page}/{username}",
                        "/comment/{commentId}",
                        "/comment",
                        "/comment/not_logged_in/{postId}/{page}/{parentCommentId}",

                        "community/mod_rights/{communityName}",
                        "/community/**",
                        "/community",
                        "/community/not_logged_in/{communityName}",
                        "/community/mod/{communityName}/{usernameToBecomeMod}",
                        "/community/member//{communityName}",
                        "/community/{communityName}",

                        "/user/{username}",
                        "/user/communities_of_user/{username}",

                        "/api/chat/**",

                        "/auth/**",

                        "/image/**",

                        "/message/get_10_messages_for_user/{page}",

                        "/websocket/**"

                        )
                .permitAll(

                )

                .requestMatchers(
                        "/post/get_posts_from_communities_member_of/**"
                )
                .hasAnyAuthority("ADMIN", "USER")

                .requestMatchers(
                        // == Post entity ==
                        "/test/admin",
                        "/product/deleteDiscount/**")
                .hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}
