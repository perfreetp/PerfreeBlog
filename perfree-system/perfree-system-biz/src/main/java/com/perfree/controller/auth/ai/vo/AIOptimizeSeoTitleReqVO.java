package com.perfree.controller.auth.ai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * SEO标题优化请求VO
 */
@Data
@Schema(description = "SEO标题优化请求VO")
public class AIOptimizeSeoTitleReqVO {

    @Schema(description = "原标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容不能为空")
    private String content;
}
