package com.perfree.controller.auth.note.vo;

import com.perfree.commons.common.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "笔记分页ReqVO")
@Data
@EqualsAndHashCode(callSuper = true)
public class NotePageReqVO extends PageParam {

    @Schema(description = "笔记标题")
    private String title;

    @Schema(description = "分类ID")
    private Integer categoryId;

    @Schema(description = "状态0:已发布,1:草稿")
    private Integer status;

    @Schema(description = "是否可见")
    private Integer visibility;
}
