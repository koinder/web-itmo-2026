package ru.itmo.wp.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Role;
import ru.itmo.wp.form.PostForm;
import ru.itmo.wp.security.AnyRole;
import ru.itmo.wp.service.UserService;

@Controller
public class WritePostPage extends Page {
    private final UserService userService;

    public WritePostPage(UserService userService) {
        this.userService = userService;
    }

    @AnyRole({Role.Name.WRITER, Role.Name.ADMIN})
    @GetMapping("/writePost")
    public String writePostGet(Model model) {
        model.addAttribute("post", new PostForm());
        return "WritePostPage";
    }

    @AnyRole({Role.Name.WRITER, Role.Name.ADMIN})
    @PostMapping("/writePost")
    public String writePostPost(@Valid @ModelAttribute("post") PostForm form,
                                BindingResult bindingResult,
                                HttpSession httpSession) {
        Set<String> tagNames = new HashSet<>();
        String rawTags = form.getTags();
        if (rawTags != null && !rawTags.trim().isEmpty()) {
            for (String tag : rawTags.trim().split("\\s+")) {
                if (!tag.matches("[a-zA-Z]{1,255}")) {
                    bindingResult.rejectValue("tags", "tags.invalid", "Each tag must contain 1 to 255 Latin letters");
                    break;
                }
                tagNames.add(tag);
            }
        }
        if (bindingResult.hasErrors()) {
            return "WritePostPage";
        }
        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setText(form.getText());
        userService.writePost(getUser(httpSession), post, tagNames);
        putMessage(httpSession, "You published new post");
        return "redirect:/myPosts";
    }
}
