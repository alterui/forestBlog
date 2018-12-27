package com.lr.blog.service.impl;

import com.lr.blog.entity.Notice;
import com.lr.blog.mapper.NoticeMapper;
import com.lr.blog.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ALTERUI
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired(required = false)
    private NoticeMapper noticeMapper;

    @Override
    public List<Notice> listNotice(Integer status)  {
        return noticeMapper.listNotice(status);
    }

    @Override
    @CachePut(value = "default", key = "'notice:'+#notice.noticeId")
    public void insertNotice(Notice notice)  {
        noticeMapper.insert(notice);
    }

    @Override
    @CacheEvict(value = "default", key = "'notice:'+#id")
    public void deleteNotice(Integer id)  {
        noticeMapper.deleteById(id);
    }

    @Override
    @CacheEvict(value = "default", key = "'notice:'+#notice.noticeId")
    public void updateNotice(Notice notice)  {
        noticeMapper.update(notice);
    }

    @Override
    @Cacheable(value = "default", key = "'notice:'+#id")
    public Notice getNoticeById(Integer id)  {
        return noticeMapper.getNoticeById(id);
    }

}
