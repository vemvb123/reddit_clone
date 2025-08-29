package com.example.Reddit.clone;

import com.example.Reddit.clone.Auth.AuthenticationResponse;
import com.example.Reddit.clone.DTO.CommentDTO;
import com.example.Reddit.clone.DTO.CommunityDTO;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.*;
import com.example.Reddit.clone.Repository.*;
import com.example.Reddit.clone.Services.ExceptionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.Reddit.clone.Auth.AuthenticationRequest;
import com.example.Reddit.clone.Auth.RegisterRequest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class EntityTest {


    @Autowired
            private UserRepository userRepository;

    @Autowired
            private PostRepository postRepository;

    @Autowired
            private CommentRepository commentRepository;

    @Autowired
            private CommunityRepository communityRepository;

    @Autowired
            private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
            private MessageRepository messageRepository;


    RegisterRequest registerRequest = RegisterRequest.builder()
            .firstname("hans")
            .lastname("larsen")
            .email("hans@email.com")
            .password("password1")
            .username("username1")
            .build();


    AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
            .username(registerRequest.getUsername())
            .password(registerRequest.getPassword())
            .build();


    private void createRoles() {
        Role roleUser = new Role();
        roleUser.setName("USER");

        Role roleAdmin = new Role();
        roleAdmin.setName("ADMIN");

        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
    }

    private User register_user() throws Exception {
        createRoles();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)));

        User user = userRepository.findByUsername(registerRequest.getUsername()) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(registerRequest.getUsername()));
        return user;
    }

    private String getUserToken() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(content, AuthenticationResponse.class);


        return response.getToken();
    }




    @Test
    public void user_can_make_community() throws Exception {
        User user = register_user();
        String token = getUserToken();

        var communityDTO = CommunityDTO.builder()
                .username(authenticationRequest.getUsername())
                .communityType(CommunityType.PUBLIC)
                .description("a description")
                .title("communityTitle")
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/community/save_community")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(communityDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.communityType", equalTo( CommunityType.PUBLIC.toString() )  ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", equalTo( communityDTO.getTitle() )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", equalTo( communityDTO.getDescription() )))
                .andReturn();

        assertTrue(communityRepository.existsByTitle(communityDTO.getTitle()));
        Community community = communityRepository.findByTitle(communityDTO.getTitle()) .orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(communityDTO.getTitle()));
        assertEquals(community.getTitle(), communityDTO.getTitle());
        assertEquals(community.getCommunityType(), communityDTO.getCommunityType());
        assertEquals(community.getDescription(), communityDTO.getDescription());






    }


    // when deleting community, posts and comments are also deleted
    @Test
    public void when_deleting_community_the_posts_in_the_community_are_also_deleted() throws Exception {
        // user
        User user = register_user();
        String token = getUserToken();

        // community
        Set<User> members = new HashSet<>();
        members.add(user);

        var community = Community.builder()
                .title("title")
                .description("description")
                .administrators(members)
                .members(members)
                .communityType(CommunityType.PUBLIC)
                .build();
        Community savedCommunity = communityRepository.save(community);

        assertTrue(communityRepository.existsByTitle(community.getTitle()));
        assertTrue(community.getAdministrators().contains(user));
        assertTrue(community.getMembers().contains(user));


        // adding post in community
        var postDTO = PostDTO.builder()
                        .title("title")
                        .content("content")
                        .build();



        mockMvc.perform(MockMvcRequestBuilders.post("/post/save_post_to_community/" + community.getTitle())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(postDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();



        List<Post> posts = postRepository.findAllByOrderByCreatedAtAsc();
        assertTrue(posts.size() > 0);

        Post savedPost = posts.get(0);
        assertTrue(postRepository.find20LaterPostAfterPost(community.getTitle(), PageRequest.of(0, 10)).contains(savedPost));



        // deleting community
        mockMvc.perform(MockMvcRequestBuilders.post("/community/delete_community/" + community.getTitle())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertFalse(communityRepository.existsByTitle(community.getTitle()));
        assertFalse(postRepository.findAllByOrderByCreatedAtAsc().size() == 0);




    }






    // when deleting post, comments for that post are deleted
    @Test
    public void when_deleting_posts_the_comments_for_that_post_is_also_deleted() throws Exception {
        // user
        User user = register_user();
        String token = getUserToken();

        // creating community
        Set<User> members = new HashSet<>();
        members.add(user);

        var community = Community.builder()
                .title("title")
                .description("description")
                .administrators(members)
                .members(members)
                .communityType(CommunityType.PUBLIC)
                .build();
        Community savedCommunity = communityRepository.save(community);

        assertTrue(communityRepository.existsByTitle(community.getTitle()));
        assertTrue(community.getAdministrators().contains(user));
        assertTrue(community.getMembers().contains(user));


        // adding post in community
        var postDTO = PostDTO.builder()
                .title("title")
                .content("content")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/post/save_post_to_community/" + community.getTitle())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(postDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();



        List<Post> posts = postRepository.findAllByOrderByCreatedAtAsc();
        assertTrue(posts.size() > 0);

        Post savedPost = posts.get(0);
        assertTrue(postRepository.find20LaterPostAfterPost(community.getTitle(), PageRequest.of(0, 10)).contains(savedPost));




        // create some comments
        var comment1 = Comment.builder()
                .isPrimeComment(true)
                .createdAt(LocalDateTime.now())
                .description("description1")
                .post(savedPost)
                .title("title1")
                .user(user)
                .build();
        Comment savedComment1 = commentRepository.save(comment1);


        var comment2 = Comment.builder()
                .isPrimeComment(false)
                .parent(savedComment1)
                .createdAt(LocalDateTime.now())
                .user(user)
                .post(savedPost)
                .title("title2")
                .description("description2")
                .build();
        Comment savedComment2 = commentRepository.save(comment2);

        List<Comment> comments = commentRepository.getAllCommentsOfPostWithId(savedPost.getId());
        List<Long> commentIds = new ArrayList<>();
        for (Comment comment : comments) {
            commentIds.add(comment.getId());
            if (Objects.equals(comment.getTitle(), "title1"))
                assertTrue(commentRepository.getReplyIntervall(comment.getId(), PageRequest.of(0, 10)).contains(savedComment2));
        }






        // delete post api
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/delete_post_by_id/" + savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();



        // check if post is deleted
        assertFalse(postRepository.existsById(savedPost.getId()));


        // check if comments for the post is deleted
        for (Long id : commentIds)
            assertFalse(commentRepository.existsById(id));





    }





    // only the user itself can delete its own post
    public User registerOwnUser(String firstname, String lastname, String email, String password, String username) throws Exception {
        RegisterRequest registerRequestOtherUser = RegisterRequest.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(password)
                .username(username)
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestOtherUser)));

        User otherUser = userRepository.findByUsername(registerRequestOtherUser.getUsername()) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(registerRequestOtherUser.getUsername()));
        return otherUser;
    }

    public String getTokenOfUserWithUsername(String username, String password) throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username(username)
                .password(password)
                .build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(content, AuthenticationResponse.class);

        String token = response.getToken();

        return token;
    }


    @Test
    public void only_the_user_itself_can_delete_its_own_post() throws Exception {
        User user = register_user();
        String token = getUserToken();


        // register another user
        User otherUser = registerOwnUser("karl", "kristian", "karl@email.com", "password2", "username2");
        String otherToken = getTokenOfUserWithUsername("username2", "password2");

        // make community
        Set<User> members = new HashSet<>();
        members.add(user);
        members.add(otherUser);
        var community = Community.builder()
                .communityType(CommunityType.PUBLIC)
                .members(members)
                .description("description")
                .title("title")
                .build();
        Community savedCommunity = communityRepository.save(community);



        // have user have a post in community
        var post = Post.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .content("content")
                .title("title")
                .community(savedCommunity)
                .build();
        Post savedPost = postRepository.save(post);


        //make otherUser delete the post
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/delete_post_by_id/" + savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", otherToken) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        assertTrue(postRepository.existsByTitle(post.getTitle()));


        //make user delete the post
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/delete_post_by_id/" + savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertFalse(postRepository.existsByTitle(post.getTitle()));






    }

    private Community insertCommunity(String title, String description, Set<User> members, CommunityType communityType, Set<User> admins, Set<User> mods) {
        var community = Community.builder()
                .title(title)
                .description(description)
                .members(members)
                .administrators(admins)
                .moderators(mods)
                .communityType(CommunityType.PUBLIC)
                .build();
        return communityRepository.save(community);
    }

    private Post insertPost(String title, User user, String content, Community community) {
        var post = Post.builder()
                .title(title)
                .user(user)
                .content(content)
                .community(communityRepository.findByTitle(community.getTitle()) .orElseThrow(() -> ExceptionUtils.noCommunityWithThatName(community.getTitle())))
                .createdAt(LocalDateTime.now())
                .build();
        return postRepository.save(post);
    }

    private Comment insertComment(boolean isPrimeComment, User user, String description, String title, Post post, Comment parent) {
        Comment comment = Comment.builder()
                .isPrimeComment(isPrimeComment)
                .user(user)
                .createdAt(LocalDateTime.now())
                .description(description)
                .title(title)
                .post(post)
                .parent(parent)
                .build();
        return commentRepository.save(comment);
    }

    // only user itself can delete comment
    @Test
    public void only_user_itself_can_delete_its_own_comment() throws Exception {
        User user = register_user();
        String token = getUserToken();

        //make other user
        User otherUser = registerOwnUser("karl", "kristian", "karl@email.com", "password2", "username2");
        String otherToken = getTokenOfUserWithUsername(otherUser.getUsername(), "password2");


        //make community
        Set<User> members = new HashSet<>();
        members.add(user);
        members.add(otherUser);
        Community community = insertCommunity("title", "description", members, CommunityType.PUBLIC, null, null);
        assertTrue(communityRepository.existsByTitle("title"));

        // make post
        Post post = insertPost("title", user, "content", community );
        assertTrue(postRepository.existsById(post.getId()));

        // make comment belonging to user
        Comment comment = insertComment(true, user, "description", "title", post, null);
        assertTrue(commentRepository.existsById(comment.getId()));

        // have other user delete comment
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/delete_comment/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", otherToken) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();

        assertTrue(commentRepository.existsById(comment.getId()));


        // have the user itself delete comment
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/delete_comment/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertFalse(commentRepository.existsById(comment.getId()));
    }

    // when deleting a comment that has children, its set to anonymus
    @Test
    public void when_deleting_comment_that_has_children_its_set_to_anonymus() throws Exception {
        User user = register_user();
        String token = getUserToken();

        // creating community
        Set<User> members = new HashSet<>();
        members.add(user);
        Community community = insertCommunity("title", "description", members, CommunityType.PUBLIC, null, null);

        // creating post
        Post post = insertPost("title", user, "content", community);

        // creating comment
        Comment parentComment = insertComment(true, user, "description", "title", post, null);


        // adding child to comment
        Comment childComment = insertComment(true, user, "description", "title", post, parentComment);


        // deleting parent comment
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/delete_comment/" + parentComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // check its set to anonymus
        assertTrue(commentRepository.existsById(parentComment.getId()));
        Comment deletedParentComment = commentRepository.findById(parentComment.getId()) .orElseThrow(() -> new RuntimeException("No comment with that id"));
        assertEquals(deletedParentComment.getTitle(), "...");
        assertEquals(deletedParentComment.getDescription(), "This comment has been deleted");
        assertEquals(deletedParentComment.getUser(), null);

        assertTrue(commentRepository.existsById(childComment.getId()));

    }





    // when deleting a comment that has no children, its removed
    @Test
    public void when_deleting_comment_that_has_no_children_the_comment_is_removed() throws Exception {
        User user = register_user();
        String token = getUserToken();

        // creating community
        Set<User> members = new HashSet<>();
        members.add(user);
        Community community = insertCommunity("title", "description", members, CommunityType.PUBLIC, null, null);

        // creating post
        Post post = insertPost("title", user, "content", community);

        // creating comment
        Comment parentComment = insertComment(true, user, "description", "title", post, null);


        // adding child to comment
        Comment childComment = insertComment(true, user, "description", "title", post, parentComment);


        // deleting parent comment
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/delete_comment/" + childComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // check its set to anonymus
        assertTrue(commentRepository.existsById(parentComment.getId()));

        assertFalse(commentRepository.existsById(childComment.getId()));
    }

    // when making a reply to a post from uppmost comment, the user of the post gets a message
    @Test
    public void when_making_a_upmost_comment_to_post_the_user_of_the_post_gets_a_message() throws Exception {
        User user = register_user();
        String token = getUserToken();

        Set<User> members = new HashSet<>();
        members.add(user);
        Community community = insertCommunity("title", "description", members, CommunityType.PUBLIC, null, null);

        Post post = insertPost("title", user, "content", community);



        var commentDTO = CommentDTO.builder()
                .parentCommentId(null)
                .description("description")
                .title("title")
                .postId(post.getId())
                .isPrimeComment(true)
                .build();

        // user DONT get message for having made comment on ones own post
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/saveComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<Message> messages = messageRepository.findMessagesOrderByEventHappendAt(user.getId());
        assertTrue(messages.size() == 0);


        // user get message for having made comment on ones own post
        User otherUser = registerOwnUser("firstname", "lastname", "firstname@email.com", "password", "username2");
        String otherToken = getTokenOfUserWithUsername("username2", "password");

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/comment/saveComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", otherToken) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();



        String content = response.getResponse().getContentAsString();
        CommentDTO responseDTO = objectMapper.readValue(content, CommentDTO.class);

        messages = messageRepository.findMessagesOrderByEventHappendAt(user.getId());
        assertTrue(messages.size() > 0);
        Message message = messages.get(0);
        System.out.println("hersjadusja");
        System.out.println(message);
        assertEquals(message.getToUser(), user);
        assertEquals(message.getFromUser(), otherUser);
        assertEquals(message.getMessageTopic(), MessageTopic.NewReplyToPost);




    }



    // when comment is not uppmost, the user dosent get a reply
    @Test
    public void when_comment_is_not_upmost_the_user_dosent_get_a_reply() throws Exception {
        User user = register_user();
        String token = getUserToken();

        Set<User> members = new HashSet<>();
        members.add(user);
        Community community = insertCommunity("title", "description", members, CommunityType.PUBLIC, null, null);

        Post post = insertPost("title", user, "content", community);

        User otherUser = registerOwnUser("firstname", "lastname", "firstname@email.com", "password", "username2");
        String otherToken = getTokenOfUserWithUsername("username2", "password");



        var commentDTO = CommentDTO.builder()
                .parentCommentId(null)
                .description("description")
                .title("title")
                .postId(post.getId())
                .isPrimeComment(true)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/saveComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", otherToken) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Message> messages = messageRepository.findMessagesOrderByEventHappendAt(user.getId());
        assertTrue(messages.size() == 1);





        var commentDTOchild = CommentDTO.builder()
                .parentCommentId(messages.get(0).getId())
                .description("description")
                .title("title")
                .postId(post.getId())
                .isPrimeComment(true)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/saveComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", otherToken) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(commentDTOchild)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        messages = messageRepository.findMessagesOrderByEventHappendAt(user.getId());
        assertTrue(messages.size() == 1);


    }

    // when deleting a user, all it's posts, comments, messages, memberships are removed
    @Test
    public void when_deleting_a_user_all_its_posts_and_comments_and_messages_and_memberships_and_friends_are_removed_too() throws Exception {
        User user = register_user();
        String token = getUserToken();

        User otherUser = registerOwnUser("firstname", "lastname", "firstname@email.com", "password", "username2");
        String otherToken = getTokenOfUserWithUsername("username2", "password");

        Set<User> members = new HashSet<>();
        members.add(user);

        //skjekk med community laget av user
        Community communityOfUser = insertCommunity("title", "description", members, CommunityType.PUBLIC, members, null);
        Post post = insertPost("title", user, "content", communityOfUser);
        Comment comment = insertComment(true, user, "description", "title", post, null);




        // skjekk med community av annen user
        Community communityOfOtherUser = insertCommunity("title", "description", members, CommunityType.PUBLIC, members, null);
        Post postOnOtherCommunity = insertPost("title", user, "content", communityOfOtherUser);
        Comment commentOnOtherCommunity = insertComment(true, user, "description", "title", postOnOtherCommunity, null);

        // creating message to user
        Post postOnOtherCommunityByOtherUser = insertPost("title", otherUser, "content", communityOfOtherUser);
        Comment commentOnOtherCommunityOnOtherPost = insertComment(true, user, "description", "title", postOnOtherCommunityByOtherUser, null);

        // kall methode for å lage reply til commentOnOtherCommunityOnOtherPost




        // gjør brukere til venner
        mockMvc.perform(MockMvcRequestBuilders.post("/user/send_friend_request/" + otherUser.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/accept_friend_request/" + user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", otherUser) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        user = userRepository.findByUsername(user.getUsername()) .orElseThrow();
        assertTrue(user.getFriends().contains(otherUser));











        // slett bruker
        mockMvc.perform(MockMvcRequestBuilders.post("/user/delete_user/" + user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token) // Replace with a valid authorization token
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();






        // skjekk at bruker er slettet
        // skjekk at egen community er slettet
        // skjekk at post på community er slettet
        // skjekk at comment på community er slettet

        //skjekk at membersships ikke inkluderer slettet bruker
        // skjekk at post post på annen community er slettet
        // skjekk at comment på annen community er slettet
        //skjekk at det ikke er noen messages tilhørende slettet bruker

        //skjekk at vennskap har opphørt





    }









}
