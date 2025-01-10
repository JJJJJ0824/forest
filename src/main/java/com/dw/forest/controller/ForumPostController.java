package com.dw.forest.controller;

import com.dw.forest.model.ForumPost;
import com.dw.forest.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ForumPostController {
    @Autowired
    ForumPostService forumPostService;

    @GetMapping("/forum-posts/all")
    public List<ForumPost> getAllForumPosts() {
        return forumPostService.getAllForumPosts();
    }
}
