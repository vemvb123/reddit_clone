package com.example.Reddit.clone;


import com.example.Reddit.clone.Auth.AuthenticationRequest;
import com.example.Reddit.clone.Auth.AuthenticationResponse;
import com.example.Reddit.clone.Auth.RegisterRequest;
import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.DTO.CommunityDTO;
import com.example.Reddit.clone.DTO.ModeratorRightsDTO;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@AllArgsConstructor
public class RightsTest {


    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private CommunityRepository communityRepository;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private JwtService jwtService;


    private void makeRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder() .name("ADMIN").build() );
        roles.add(Role.builder() .name("USER").build() );

        roleRepository.saveAll(roles);
    }

    private User makeUser(String firstname, String lastname, String email, String password, String username) throws Exception {
            RegisterRequest registerRequestOtherUser = new RegisterRequest(
                    firstname,
                    lastname,
                    email,
                    password,
                    username
            );

            mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequestOtherUser)));

            User user = userRepository.findByUsername(registerRequestOtherUser.username())
                    .orElseThrow(() -> new NotFoundException(NotFound.USER));
            return user;
    }

    private String getTokenOfUser(String username, String password) throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                username,
                password
        );

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(content, AuthenticationResponse.class);

        String token = response.token();

        return token;
    }



    @Test
    public void user_making_community_gets_admin_rights() throws Exception {

        makeRoles();
        User user = makeUser("firstname", "lastname", "email.com", "password", "username");
        String token = getTokenOfUser("username", "password");

        CommunityDTO communityDTO = CommunityDTO.builder()
                        .title("title")
                        .description("description")
                        .communityType(CommunityType.PUBLIC)
                        .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/community/save_community")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(communityDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", notNullValue()))
                .andDo(print());

        assertTrue(communityRepository.existsByTitle(communityDTO.title()));
        assertTrue(communityRepository.findMembersOfCommunityByTitle(communityDTO.title()).contains(user));
        assertTrue(communityRepository.findAdminsOfCommunityByTitle(communityDTO.title()).contains(user));
    }


    @Test
    public void normal_users_dont_have_mod_rights() {

    }



    public Post makePost(String title, String content, String authorization, String communityName) throws Exception {

        var postRequest = PostDTO.builder()
                .title(title)
                .content(content)
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/post/save_post_to_community/" + communityName)
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        PostDTO response = objectMapper.readValue(json, PostDTO.class);

        return postRepository.findById(response.id())
                .orElseThrow(() -> new NotFoundException(NotFound.POST));


    }

    public Comment makeComment(String title, String description, String authorization, Long postId, Long parentId) throws Exception {

        var commentRequest = CommentDTO.builder()
                .title(title)
                .description(description)
                .parentCommentId(parentId)
                .postId(postId)
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/comment/saveComment")
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        CommentDTO response = objectMapper.readValue(json, CommentDTO.class);

        return commentRepository.findById(response.id()) .orElseThrow(() -> new RuntimeException("No comment with that id"));

    }


    public Community makeCommunity(String title, String description, CommunityType communityType, String authorization) throws Exception {

        var communityRequest = CommunityDTO.builder()
                .title(title)
                .description(description)
                .communityType(communityType)
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/community/save_community")
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(communityRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        CommunityDTO response = objectMapper.readValue(json, CommunityDTO.class);

        return communityRepository.findByTitle(response.title())
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));
    }

    public void makeUserBecomeMod(String authorization, String communityName, String usernameToBecomeMod) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/community/makeUserBecomeMod/" + communityName + "/" + usernameToBecomeMod)
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    public void makeUserBecomeMember(String authorization, String communityName) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/community/makeUserBecomeMember/" + communityName)
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }







    @Test
    @Transactional
    public void admins_can_set_rights_and_mods_are_allowed_and_restricted_by_rights() throws Exception {
        // making users
        makeRoles();

        User admin = makeUser("firstname", "lastname", "email.com", "password", "admin");
        String adminToken = getTokenOfUser("admin", "password");

        User mod = makeUser("firstname", "lastname", "email.com", "password", "mod");
        String modToken = getTokenOfUser("mod", "password");

        User normalUser = makeUser("firstname", "lastname", "email.com", "password", "normal");
        String normalUserToken = getTokenOfUser("normal", "password");

        // making community
        Community community = makeCommunity("title", "description", CommunityType.PUBLIC, adminToken);
        makeUserBecomeMember(normalUserToken, community.getTitle());
        makeUserBecomeMember(modToken, community.getTitle());
        makeUserBecomeMod(adminToken, community.getTitle(), mod.getUsername());


        // ===========================================================
        // moderatorCanDeleteOthersPosts;
        // ===========================================================
        // post
        Post post = makePost("title", "content", normalUserToken, community.getTitle());

        // mod deleting post while not allowed
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/delete_post_by_id/" + post.getId())
                        .header("Authorization", modToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());

        assertTrue(postRepository.existsById(post.getId()));

       // admin makes it so mod is allowed to delete others posts
        assertFalse(communityRepository.findByTitle(community.getTitle()).orElseThrow().getModeratorCanDeleteOthersPosts());

        var changeModeratorRightsRequest = ModeratorRightsDTO.builder().deleteOthersPosts(true).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/community/change_moderator_rights/" + community.getTitle())
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeModeratorRightsRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        assertTrue(communityRepository.findByTitle(community.getTitle()) .orElseThrow() .getModeratorCanDeleteOthersPosts());

        // mod deleting post while allowed
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/delete_post_by_id/" + post.getId())
                        .header("Authorization", modToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());



        // moderatorCanDeleteOthersComments;
        Post postForComment = makePost("title", "content", normalUserToken, community.getTitle());


        Comment comment = makeComment("title", "description", normalUserToken, postForComment.getId(), null);


        mockMvc.perform(MockMvcRequestBuilders.post("/comment/delete_comment/" + comment.getId())
                        .header("Authorization", modToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
        assertTrue(commentRepository.existsById(comment.getId()));

        var moderatorCanDeleteOthersCommentsRequest = ModeratorRightsDTO.builder()
                .deleteOthersComments(true)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/community/change_moderator_rights/" + community.getTitle())
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(moderatorCanDeleteOthersCommentsRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/delete_comment/" + comment.getId())
                        .header("Authorization", modToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
        assertFalse(commentRepository.existsById(comment.getId()));




        // moderatorCanBanUser;
        // moderatorCanDeleteCommunity;
        // moderatorCanMakeAnnouncement;
        // moderatorCanChangeCommunityDescription;







    }
    //1
    //2

    // mods and admins can delete other peoples posts, comments

    //mods and admins can ban other users

    //admins can ban other mods

    //admins can not ban eachother

    // mods can not ban eachother, not admins

    // admin can remove mod powers
    // mod can not remove admin powers, nor other mod's powers

    // admin can't leave community on certain conditions

    //when mod leaves, its powers are removed

}
