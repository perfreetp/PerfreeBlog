package com.perfree.controller.auth.ai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 生成摘要请求VO
 */
@Data
@Schema(description = "生成摘要请求VO")
public class AIGenerateSummaryReqVO {

    @Schema(description = "文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容不能为空")
    private String content;
}
