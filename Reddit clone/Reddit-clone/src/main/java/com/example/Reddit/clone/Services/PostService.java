package com.example.Reddit.clone.Services;


import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.DTO.PostDTO;
import com.example.Reddit.clone.DTO.ResponseText;
import com.example.Reddit.clone.Entity.Community;
import com.example.Reddit.clone.Entity.CommunityType;
import com.example.Reddit.clone.Entity.Post;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Exception.NotFound;
import com.example.Reddit.clone.Exception.NotFoundException;
import com.example.Reddit.clone.Mapper.PostMapper;
import com.example.Reddit.clone.Repository.CommunityRepository;
import com.example.Reddit.clone.Repository.PostRepository;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class PostService {

    @Value("${images.path}")
    public String pathToSaveImages;

    private CommunityRepository communityRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    private JwtService jwtService;

    private PostMapper postMapper;


    public PostDTO getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(NotFound.POST));
        return postMapper.entityToDto(post);
    }


    @Transactional
    public PostDTO savePost(PostDTO postDTO, String communityName) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        Post post = new Post();
        if (postDTO.id() != null) {
            post.setId(postDTO.id());
            post.setPathToPostImage(postDTO.pathToPostImage());
            post.setLastUpdated(LocalDateTime.now());
            post.setCreatedAt(postDTO.createdAt());
        }
        else
            post.setCreatedAt(LocalDateTime.now());

        post.setTitle(postDTO.title());
        post.setContent(postDTO.content());

        post.setCommunity(community);
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        return postMapper.entityToDto(savedPost);
    }


    public List<PostDTO> get20LatestPosts(String communityName, int page) {
        Community community = communityRepository.findByTitle(communityName)
                .orElseThrow(() -> new NotFoundException(NotFound.COMMUNITY));

        List<Post> posts = postRepository.find20LaterPostAfterPost(community.getTitle(), PageRequest.of(page, 10));
        return posts.stream()
                .map(post -> postMapper.entityToDto(post))
                .toList();
    }


    public List<PostDTO> get20PostsFromCommunitiesMemberOf(Integer page) {
        Set<Community> communities = userRepository.findCommunitiesByUsername( SecurityUtils.getUsername() );
        List<Post> posts = communityRepository.getPostsOfCommunities(communities, PageRequest.of(page, 20));
        return posts.stream()
                .map(post -> postMapper.entityToDto(post))
                .toList();
    }


    public ResponseText setImage(MultipartFile file, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(NotFound.POST));

        if (post.getPathToPostImage() != null)
            post.setLastUpdated(LocalDateTime.now());

        if (!file.isEmpty()) {
            try {
                if (!new File(pathToSaveImages).exists()) {
                    new File(pathToSaveImages).mkdir();
                }

                String orgName = file.getOriginalFilename();
                String filePath = pathToSaveImages + orgName;
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
        return new ResponseText("File saved successfully");
    }


    public ResponseText deletePost(Long postId) {
        if (!postRepository.existsById(postId))
            throw new NotFoundException(NotFound.POST);

        postRepository.deleteById(postId);
        return new ResponseText("Post deleted successfully");
    }


    public List<PostDTO> get20LatestPostsOfUserLoggedIn(String username, Integer page) {
        User userLooking = userRepository.findByUsername( SecurityUtils.getUsername() )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));
        User userProfile = userRepository.findByUsername( username )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        Set<Community> userCommunities = userRepository.findCommunitiesUserIsMemberOf(userLooking.getUsername());
        List<Post> posts = postRepository.getPostsOfUserOrderByCreatedAt(userProfile.getUsername(), PageRequest.of(page, 20));
        return posts.stream()
            .filter(post ->
                post.getCommunity().getCommunityType() == CommunityType.PUBLIC
                || userCommunities.contains(post.getCommunity())
            )
            .map(postMapper::entityToDto)
            .toList();
    }


    public List<PostDTO> get20LatestPostsOfUserNotLoggedIn(String username, Integer page) {
        User userProfile = userRepository.findByUsername( username )
                .orElseThrow(() -> new NotFoundException(NotFound.USER));

        List<Post> posts = postRepository.getPostsOfUserOrderByCreatedAt(userProfile.getUsername(), PageRequest.of(page, 20));

        return posts.stream()
                .filter(post -> post.getCommunity().getCommunityType() == CommunityType.PUBLIC)
                .map(post -> postMapper.entityToDto(post))
                .toList();
    }


    public List<PostDTO> getLatestPostsOfCommunitiesPublic(Integer page) {
        List<Post> posts = postRepository.getLatestsPostsOfPublicCommunities(PageRequest.of(page, 20));
        return posts.stream()
                .map(post -> postMapper.entityToDto(post))
                .toList();
    }

}