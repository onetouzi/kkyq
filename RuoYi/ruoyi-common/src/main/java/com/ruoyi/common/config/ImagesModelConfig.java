package com.ruoyi.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 图片模型配置类（读取application.yaml中的imagesModel配置）
 * @author azzzz
 * @date 2025-12-29
 */
@Component
@ConfigurationProperties(prefix = "imagepredicturl")
public class ImagesModelConfig {
    /** 上传接口路径 */
    private Upload upload = new Upload();

    // 内部类映射upload节点
    public static class Upload {
        /** 预测接口路径 */
        private String path;
        /** 预测接口端口 */
        private Integer port;
        /** 允许的文件扩展名 */
        private String allowExts;

        // getter/setter
        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getAllowExts() {
            return allowExts;
        }

        public void setAllowExts(String allowExts) {
            this.allowExts = allowExts;
        }
    }

    // getter/setter
    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }
}