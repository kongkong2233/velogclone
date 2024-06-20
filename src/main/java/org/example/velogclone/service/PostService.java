package org.example.velogclone.service;

import lombok.RequiredArgsConstructor;
import org.example.velogclone.domain.Image;
import org.example.velogclone.domain.Post;
import org.example.velogclone.domain.User;
import org.example.velogclone.repository.ImageRepository;
import org.example.velogclone.repository.PostRepository;
import org.example.velogclone.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public Post createPost(String title, String content, String username, List<String> imageUrls) {
        //게시물 작성
        Optional<User> userOptional = userRepository.findByUserName(username);
        if (userOptional.isPresent()) {
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setCreatedAt(LocalDateTime.now());
            post.setUser(userOptional.get()); //작성자

            if (imageUrls != null) {
                for (String imageUrl : imageUrls) {
                    Image image = new Image();
                    image.setImageUrl(imageUrl);
                    image.setPost(post);
                    post.getImages().add(image);
                }
            }

            return postRepository.save(post);
        }
        throw new RuntimeException("User not found");
    }

    public List<Post> getAllPosts() { //게시물 전체보기
        List<Post> posts = postRepository.findAll();
        return posts;
    }

    public Optional<Post> getPostById(Long postId) { //게시물 검색
        return postRepository.findById(postId);
    }

    public Post updatePost(Long postId, String title, String content, List<String> imageUrls) {
        //게시물 수정
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setTitle(title);
            post.setContent(content);

            post.getImages().clear();
            if (imageUrls != null) {
                for (String imageUrl : imageUrls) {
                    Image image = new Image();
                    image.setImageUrl(imageUrl);
                    image.setPost(post);
                    post.getImages().add(image);
                }
            }

            return postRepository.save(post);
        }
        throw new RuntimeException("Post not found");
    }

    public void deletePostById(Long postId) { //게시물 삭제
        postRepository.deleteById(postId);
    }
}
