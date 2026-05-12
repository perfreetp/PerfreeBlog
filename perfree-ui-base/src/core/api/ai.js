import request from '@/core/api/axios'

/**
 * AI续写
 * @param {Object} data
 * @param {string} data.content - 原文内容
 */
export function continueWritingApi(data) {
  return request({
    url: '/api/auth/ai/continueWriting',
    method: 'post',
    data
  })
}

/**
 * 生成文章摘要
 * @param {Object} data
 * @param {string} data.content - 文章内容
 */
export function generateSummaryApi(data) {
  return request({
    url: '/api/auth/ai/generateSummary',
    method: 'post',
    data
  })
}

/**
 * SEO标题优化
 * @param {Object} data
 * @param {string} data.title - 原标题
 * @param {string} data.content - 文章内容
 */
export function optimizeSeoTitleApi(data) {
  return request({
    url: '/api/auth/ai/optimizeSeoTitle',
    method: 'post',
    data
  })
}

/**
 * 生成SEO关键词
 * @param {Object} data
 * @param {string} data.content - 文章内容
 */
export function generateSeoKeywordsApi(data) {
  return request({
    url: '/api/auth/ai/generateSeoKeywords',
    method: 'post',
    data
  })
}

/**
 * 生成SEO描述
 * @param {Object} data
 * @param {string} data.content - 文章内容
 */
export function generateSeoDescriptionApi(data) {
  return request({
    url: '/api/auth/ai/generateSeoDescription',
    method: 'post',
    data
  })
}
