package ru.itmo.wp.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import ru.itmo.wp.domain.Post;
import ru.itmo.wp.form.CommentForm;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.CommentService;
import ru.itmo.wp.service.PostService;

@Controller
public class PostPage extends Page {
    private final PostService postService;
    private final CommentService commentService;

    public PostPage(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @Guest
    @GetMapping("/post/{id}")
    public String post(@PathVariable("id") long id, Model model) {
        Post post = postService.findById(id);

        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("post", post);
        model.addAttribute("commentForm", new CommentForm());
        return "PostPage";
    }

    @PostMapping("/post/{id}")
    public String addComment(
            @PathVariable("id") long id,
            @Valid @ModelAttribute("commentForm") CommentForm commentForm,
            BindingResult bindingResult,
            Model model,
            HttpSession httpSession) {

        Post post = postService.findById(id);

        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "PostPage";
        }

        commentService.add(getUser(httpSession), post, commentForm.getText());
        return "redirect:/post/" + id;
    }
}