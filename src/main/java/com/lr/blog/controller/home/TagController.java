package com.lr.blog.controller.home;

import com.github.pagehelper.PageInfo;
import com.lr.blog.entity.Article;
import com.lr.blog.entity.Tag;
import com.lr.blog.enums.ArticleStatus;


import com.lr.blog.service.ArticleService;
import com.lr.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lr
 */
@Controller
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;

    /**
     * 根据标签查询文章
     *
     * @param tagId 标签ID
     * @return 模板
     */
    @RequestMapping("/tag/{tagId}")
    public String getArticleListByTag(@PathVariable("tagId") Integer tagId,
                                      @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                      Model model) {
        //该标签信息
        Tag tag = tagService.getTagById(tagId);
        if (tag == null) {
            return "redirect:/404";
        }
        model.addAttribute("tag", tag);

        //文章列表
        HashMap<String, Object> criteria = new HashMap<>(2);
        criteria.put("tagId", tagId);
        criteria.put("status", ArticleStatus.PUBLISH.getValue());
        PageInfo<Article> articlePageInfo = articleService.pageArticle(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", articlePageInfo);

        //侧边栏
        //标签列表显示
        List<Tag> allTagList = tagService.listTag();
        model.addAttribute("allTagList", allTagList);
        //获得随机文章
        List<Article> randomArticleList = articleService.listRandomArticle(8);
        model.addAttribute("randomArticleList", randomArticleList);
        //获得热评文章
        List<Article> mostCommentArticleList = articleService.listArticleByCommentCount(8);
        model.addAttribute("mostCommentArticleList", mostCommentArticleList);
        model.addAttribute("pageUrlPrefix", "/tag?pageIndex");
        return "Home/Page/articleListByTag";
    }

}
