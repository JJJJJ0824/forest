package com.dw.forest.service;

import com.dw.forest.repository.ForumPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForumPostService {
    @Autowired
    ForumPostRepository forumPostRepository;

}
