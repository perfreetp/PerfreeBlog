package com.perfree.controller.auth.note.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "笔记添加ReqVO")
@Data
public class NoteAddReqVO {

    @Schema(description = "笔记标题")
    @NotBlank(message = "笔记标题不能为空")
    private String title;

    @Schema(description = "笔记内容")
    private String content;

    @Schema(description = "笔记内容类型:html/markdown")
    private String contentModel;

    @Schema(description = "解析后的笔记内容")
    private String parseContent;

    @Schema(description = "分类id集合")
    private List<Integer> categoryIds;

    @Schema(description = "标签id集合")
    private List<Integer> tagIds;

    @Schema(description = "新增的标签集合")
    private List<String> addTags;

    @Schema(description = "状态0:已发布,1:草稿")
    @NotNull(message = "笔记状态不能为空")
    private Integer status;

    @Schema(description = "是否可见, 0是, 1否")
    private Integer visibility;

    @Schema(description = "笔记摘要")
    private String summary;
}
