package com.dw.forest.service;

import com.dw.forest.model.ForumPost;
import com.dw.forest.repository.ForumPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumPostService {
    @Autowired
    ForumPostRepository forumPostRepository;

    public List<ForumPost> getAllForumPosts() {
        return forumPostRepository.findAll();
    }

}
