package org.example.velogclone.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.velogclone.domain.Post;
import org.example.velogclone.service.PostService;
import org.example.velogclone.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/{postId}")
    public String getPostById(@PathVariable("postId") Long postId, Model model) {
        //게시물 상세 페이지
        Post post = postService.getPostById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        model.addAttribute("post", post);
        return "post";
    }

    @GetMapping("/create")
    public String showCreatePost(Model model) {
        model.addAttribute("post", new Post());
        return "createform";
    }

    @PostMapping("/create")
    public String createPost(@RequestParam("title") String title, @RequestParam("content") String content,
                             @RequestParam("username") String username, @RequestParam(value = "imageUrls", required = false) List<String> imageUrls) {
        //게시물 등록
        postService.createPost(title, content, username, imageUrls);
        return String.format("redirect:/@%s/posts", username); //동적 경로 설정
    }

    @GetMapping("/{postId}/edit")
    public String showEditPost(@PathVariable("postId") Long postId, Model model, HttpServletRequest request) {
        //게시글 수정 폼
        Optional<Post> optionalPost = postService.getPostById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);

            String currentUsername =
                    request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;

            if (currentUsername != null && currentUsername.equals(post.getUser().getUserName())) {
                model.addAttribute("username", currentUsername);
            }
            return "post";
        } else {
            return String.format("redirect:/posts/%d", postId);
        }
    }

    @PostMapping("/{postId}/edit")
    public String editPost(@PathVariable("postId") Long postId, @RequestParam("title") String title,
                           @RequestParam("content") String content, @RequestParam(value = "imageUrls", required = false) List<String> imageUrls) {
        postService.updatePost(postId, title, content, imageUrls);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable("postId") Long postId) {
        //게시물 삭제
        postService.deletePostById(postId);
        return "redirect:/posts";
    }
}
