package be.intecbrussel.jpaonetomanydemo.controller;

import be.intecbrussel.jpaonetomanydemo.model.Comment;
import be.intecbrussel.jpaonetomanydemo.model.Post;
import be.intecbrussel.jpaonetomanydemo.service.CommentServiceImpl;
import be.intecbrussel.jpaonetomanydemo.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class CommentController {
    private  CommentServiceImpl commentService;
    private  PostServiceImpl postService;


    @Autowired
    public CommentController(CommentServiceImpl commentService, PostServiceImpl postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    // Handles GET requests for displaying all comments for a specific post
    @GetMapping("/posts/{postId}/comments")
    public String getAllCommentsByPostId(@PathVariable(value = "postId") Long postId,
                                         @RequestParam(name = "page", defaultValue = "1") int pageNo,
                                         Model model) {
        int pageSize = 5;
        Page<Comment> page = commentService.findCommentPaginated(postId, pageNo, pageSize);
        List<Comment> commentList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("commentList", commentList);

        // Fetches the post to which comments belong and adds to the model
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        model.addAttribute("comment", new Comment());
        return "comments";
    }

    // Handles POST requests for creating a new comment
    @PostMapping("/posts/{postId}/comments")
    public String createComment(@PathVariable(value = "postId") Long postId,
                                @ModelAttribute("comment") Comment comment) {
        Post post = postService.getPostById(postId);
        // Associates the comment with the post
        comment.setPost(post);
        commentService.saveComment(comment);
        return "redirect:/posts/" + postId + "/comments";
    }

    // Handles GET requests for displaying the edit form for a specific comment
    @GetMapping("/posts/{postId}/comments/{commentId}/edit")
    public String showEditCommentForm(@PathVariable(value = "postId") Long postId,
                                      @PathVariable(value = "commentId") Long commentId, Model model) {
        Comment comment = commentService.getCommentById(commentId);
        model.addAttribute("comment", comment);
        model.addAttribute("postId", postId);
        return "edit_comment";
    }

    // Handles POST requests for updating an existing comment
    @PostMapping("/posts/{postId}/comments/{commentId}/edit")
    public String updateComment(@PathVariable(value = "postId") Long postId,
                                @PathVariable(value = "commentId") Long commentId,
                                @ModelAttribute("comment") Comment comment) {
        Comment existingComment = commentService.getCommentById(commentId);
        existingComment.setText(comment.getText());
        commentService.saveComment(existingComment);
        return "redirect:/posts/" + postId + "/comments";
    }


    // Handles GET requests for deleting a specific comment
    @GetMapping("/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable(value = "postId") Long postId,
                                @PathVariable(value = "commentId") Long commentId) {
        commentService.deleteCommentById(commentId);
        return "redirect:/posts/" + postId + "/comments";
    }


};


