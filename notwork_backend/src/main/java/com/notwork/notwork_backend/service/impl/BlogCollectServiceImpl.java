package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.pojo.BlogCollect;
import com.notwork.notwork_backend.mapper.BlogCollectMapper;
import com.notwork.notwork_backend.mapper.BlogMapper;
import com.notwork.notwork_backend.service.IBlogCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogCollectServiceImpl extends ServiceImpl<BlogCollectMapper, BlogCollect> implements IBlogCollectService {

    private final BlogMapper blogMapper;

    @Override
    @Transactional
    public boolean toggleCollect(Long blogId, Long userId) {
        LambdaQueryWrapper<BlogCollect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogCollect::getBlogId, blogId).eq(BlogCollect::getUserId, userId);
        BlogCollect existing = getOne(wrapper);

        if (existing != null) {
            removeById(existing.getId());
            blogMapper.update(null, new LambdaUpdateWrapper<Blog>()
                    .eq(Blog::getId, blogId)
                    .setSql("collect_count = collect_count - 1"));
            return false;
        } else {
            BlogCollect blogCollect = new BlogCollect();
            blogCollect.setBlogId(blogId);
            blogCollect.setUserId(userId);
            save(blogCollect);
            blogMapper.update(null, new LambdaUpdateWrapper<Blog>()
                    .eq(Blog::getId, blogId)
                    .setSql("collect_count = collect_count + 1"));
            return true;
        }
    }

    @Override
    public boolean hasCollected(Long blogId, Long userId) {
        return count(new LambdaQueryWrapper<BlogCollect>()
                .eq(BlogCollect::getBlogId, blogId)
                .eq(BlogCollect::getUserId, userId)) > 0;
    }

    @Override
    public List<Long> getCollectedBlogIdsByUserId(Long userId) {
        return list(new LambdaQueryWrapper<BlogCollect>().eq(BlogCollect::getUserId, userId))
                .stream().map(BlogCollect::getBlogId).toList();
    }
}
