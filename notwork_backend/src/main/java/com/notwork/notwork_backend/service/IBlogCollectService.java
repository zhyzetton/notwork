package com.notwork.notwork_backend.service;

import com.notwork.notwork_backend.entity.pojo.BlogCollect;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IBlogCollectService extends IService<BlogCollect> {

    boolean toggleCollect(Long blogId, Long userId);

    boolean hasCollected(Long blogId, Long userId);

    List<Long> getCollectedBlogIdsByUserId(Long userId);
}
