package com.perfree.controller.auth.ai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * AI续写请求VO
 */
@Data
@Schema(description = "AI续写请求VO")
public class AIContinueWritingReqVO {

    @Schema(description = "原文内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容不能为空")
    private String content;
}
