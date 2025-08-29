package com.example.Reddit.clone.Config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
                        "/post/get_all_posts_of_public_communities/{page}",
                        "/user/get_user_by_username/{username}",
                        "/post/get_20_latets_posts_of_user_not_logged_in/{page}/{username}",
                        "/comment/get_20_latets_comments_of_user_not_logged_in/{page}/{username}",
                        "/post/get_post_not_logged_in/{postId}",
                        "/community/get_community_by_name_without_token/{communityName}",
                        "/post/get_20_latets_posts_of_community_without_token/{page}/{communityName}",
                        "/comment/getIntervallOfCommentsWithoutToken/{postId}/{page}/{parentCommentId}",

                        "community/change_moderator_rights/{communityName}",
                        "/api/chat/**",
                        "/auth/**",
                        "/image/**",
                        "/community/get_community_by_name/**",
                        "/user/get_communities_of_user/{username}",
                        "/message/get_10_messages_for_user/{page}",
                        "/community/save_community",
                        "/websocket/**",
                        "/post/save_post_to_community/{communityName}",
                        "/community/delete_community/{communityName}",
                        "/post/delete_post_by_id/{postId}",
                        "post/delete_post_by_id/{postId}",
                        "/comment/delete_comment/{commentId}",
                        "/comment/saveComment",
                        "/community/makeUserBecomeMod/{communityName}/{usernameToBecomeMod}",
                        "/community/makeUserBecomeMember/{communityName}"

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
