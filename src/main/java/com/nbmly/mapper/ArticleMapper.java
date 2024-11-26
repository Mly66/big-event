package com.nbmly.mapper;

import com.nbmly.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //新增
    @Insert("insert into article(title, content,state, cover_img, category_id, create_user, create_time, update_time) " +
            "values (#{title},#{content},#{state},#{coverImg},#{categoryId},#{createUser},#{createTime},#{updateTime})")
    void add(Article article);
    //分页查询

    List<Article> list(Integer userId, Integer categoryId, String state);
    //获取文章详情
    @Select("select * from article where id=#{id}")
    Article findById(Integer id);
    @Update("update article set title=#{title},content=#{content},cover_img=#{coverImg},state=#{state},category_id=#{categoryId} where id=#{id}")
    void update(Article article);
    //删除文章
    @Delete("delete from article where id=#{id}")
    void deleteById(Integer id);
}
