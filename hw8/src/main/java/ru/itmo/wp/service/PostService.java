package ru.itmo.wp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.itmo.wp.domain.Post;
import ru.itmo.wp.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreationTimeDesc();
    }

    public Post findById(long id) {
        return postRepository.findById(id).orElse(null);
    }
}
