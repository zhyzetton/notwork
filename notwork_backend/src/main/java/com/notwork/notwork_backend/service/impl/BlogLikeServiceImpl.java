package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.pojo.BlogLike;
import com.notwork.notwork_backend.mapper.BlogLikeMapper;
import com.notwork.notwork_backend.mapper.BlogMapper;
import com.notwork.notwork_backend.service.IBlogLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike> implements IBlogLikeService {

    private final BlogMapper blogMapper;

    @Override
    @Transactional
    public boolean toggleLike(Long blogId, Long userId) {
        LambdaQueryWrapper<BlogLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogLike::getBlogId, blogId).eq(BlogLike::getUserId, userId);
        BlogLike existing = getOne(wrapper);

        if (existing != null) {
            removeById(existing.getId());
            blogMapper.update(null, new LambdaUpdateWrapper<Blog>()
                    .eq(Blog::getId, blogId)
                    .setSql("like_count = like_count - 1"));
            return false;
        } else {
            BlogLike blogLike = new BlogLike();
            blogLike.setBlogId(blogId);
            blogLike.setUserId(userId);
            save(blogLike);
            blogMapper.update(null, new LambdaUpdateWrapper<Blog>()
                    .eq(Blog::getId, blogId)
                    .setSql("like_count = like_count + 1"));
            return true;
        }
    }

    @Override
    public boolean hasLiked(Long blogId, Long userId) {
        return count(new LambdaQueryWrapper<BlogLike>()
                .eq(BlogLike::getBlogId, blogId)
                .eq(BlogLike::getUserId, userId)) > 0;
    }
}
