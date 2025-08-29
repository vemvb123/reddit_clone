package com.example.Reddit.clone.Services;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.CommunityType;
import com.example.Reddit.clone.Entity.Post;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.CommentRepository;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.PostRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;


    public PostDTO getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        return new PostDTO().mapObjectToDTO(post);
    }



    @Transactional
    public PostDTO savePost(PostDTO postDTO, String authorization, String communityName) {

        System.out.println("1");
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> new RuntimeException("Community not found"));
        System.out.println("2");

        String username = jwtService.extractUsername(authorization);
        System.out.println("3");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("4");

        //save post
        //etabler forholdet mellom post og community
        //etabler forholdet mellom user og post

        Post post = new Post();
        if (postDTO.getId() != null) {
            post.setId(postDTO.getId());
            post.setPathToPostImage(postDTO.getPathToPostImage());
            post.setLastUpdated(LocalDateTime.now());
            post.setCreatedAt(postDTO.getCreatedAt());
        }
        else
            post.setCreatedAt(LocalDateTime.now());

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());

        post.setCommunity(community);
        post.setUser(user);
        System.out.println("5");

        Post savedPost = postRepository.save(post);
        System.out.println("6");




        return new PostDTO().mapObjectToDTO(savedPost);







    }


    public List<PostDTO> get20LatestPosts(String communityName, int page) {
        Community community = communityRepository.findByTitle(communityName).orElseThrow(() -> new RuntimeException("Community not found"));

        List<Post> posts = postRepository.find20LaterPostAfterPost(community.getTitle(), PageRequest.of(page, 10));

        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts)
            postDTOs.add( new PostDTO().mapObjectToDTO(post) );

        return postDTOs;
    }


    public List<PostDTO> get20PostsFromCommunitiesMemberOf(String authorization, Integer page) {
        String username = jwtService.extractUsername(authorization);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Community not found"));

        Set<Community> communities = userRepository.findCommunitiesByUsername(username);

        List<Post> posts = communityRepository.getPostsOfCommunities(communities, PageRequest.of(page, 20));

        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts)
            postDTOs.add( new PostDTO().mapObjectToDTO(post) );


        return postDTOs;

    }

    public void setImage(MultipartFile file, Long postId) {


        Post post = postRepository.getById(postId);
        if (post.getPathToPostImage() != null)
            post.setLastUpdated(LocalDateTime.now());

        if (!file.isEmpty()) {
            try {
                if (!new File(FileService.pathToSaveImages).exists()) {
                    new File(FileService.pathToSaveImages).mkdir();
                }

                String orgName = file.getOriginalFilename();
                String filePath = FileService.pathToSaveImages + orgName;
                filePath = FileService.makeOriginalFileName(filePath);
                File dest = new File(filePath);
                file.transferTo(dest);



                if (new File(filePath).exists()) {
                    post.setPathToPostImage(file.getOriginalFilename());
                    postRepository.save(post);
                }


            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }


    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new NoSuchElementException("Entity not found with id: " + postId);
        }



        postRepository.deleteById(postId);
    }

    public List<PostDTO> get20LatestPostsOfUser(String username, Integer page, String authorization) {
        User userLooking = userRepository.findByUsername(jwtService.extractUsername(authorization)).orElseThrow(() -> ExceptionUtils.noUserWithThatName(jwtService.extractUsername(authorization)));
        User userProfile = userRepository.findByUsername(username ) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username ));

        List<Post> posts = postRepository.getPostsOfUserOrderByCreatedAt(userProfile.getUsername(), PageRequest.of(page, 20));


        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            if (!(post.getCommunity().getCommunityType() == CommunityType.PUBLIC))
                if (!userRepository.findCommunitiesUserIsMemberOf(userLooking.getUsername()).contains(post.getCommunity()))
                    continue;

            postDTOs.add( new PostDTO().mapObjectToDTO(post) );
        }

        return postDTOs;

    }

    public List<PostDTO> get20LatestPostsOfUser(String username, Integer page) {
        User userProfile = userRepository.findByUsername(username ) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username ));

        List<Post> posts = postRepository.getPostsOfUserOrderByCreatedAt(userProfile.getUsername(), PageRequest.of(page, 20));


        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            if (!(post.getCommunity().getCommunityType() == CommunityType.PUBLIC))
                continue;

            postDTOs.add( new PostDTO().mapObjectToDTO(post) );
        }

        return postDTOs;

    }
    public List<PostDTO> getLatestPostsOfCommunitiesPublic(Integer page) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        List<Post> posts = postRepository.getLatestsPostsOfPublicCommunities(PageRequest.of(page, 20));


        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            postDTOs.add( new PostDTO().mapObjectToDTO(post) );
        }

        return postDTOs;
    }
}