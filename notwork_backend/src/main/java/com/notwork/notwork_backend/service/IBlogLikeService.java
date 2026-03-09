package com.notwork.notwork_backend.service;

import com.notwork.notwork_backend.entity.pojo.BlogLike;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IBlogLikeService extends IService<BlogLike> {

    boolean toggleLike(Long blogId, Long userId);

    boolean hasLiked(Long blogId, Long userId);
}
