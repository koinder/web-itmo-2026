package ru.itmo.wp.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NoticeForm {
    @NotBlank(message = "Content is required")
    @Size(max = 10000, message = "Content is too long")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}