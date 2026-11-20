package ru.itmo.wp.form;

import javax.validation.constraints.NotBlank;

public class CommentForm {
    @NotBlank(message = "Comment must not be empty")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}