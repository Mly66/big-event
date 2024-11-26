package com.nbmly.pojo;


import com.nbmly.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
@Data
public class Article {
    @NotNull(groups = Update.class)
    private Integer id;//主键ID
    @NotEmpty(groups = Update.class)
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;//文章标题
    @NotEmpty(groups = Update.class)
    private String content;//文章内容
    @NotEmpty(groups = Update.class)
    @URL
    private String coverImg;//封面图像
    @NotEmpty(groups = Update.class)
    @State
    private String state;//发布状态 已发布|草稿
    @NotNull(groups = Update.class)
    private Integer categoryId;//文章分类id
    private Integer createUser;//创建人ID
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间


    public interface Update extends Default {

}
}
