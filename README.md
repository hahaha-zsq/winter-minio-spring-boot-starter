<div align="center">

# 🌨️ Winter MinIO Spring Boot Starter

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Java support](https://img.shields.io/badge/Java-1.8+-green.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6+-blue.svg)](https://spring.io/projects/spring-boot)
[![AWS SDK S3](https://img.shields.io/badge/AWS%20SDK%20S3-1.12.709-FF9900.svg)](https://aws.amazon.com/sdk-for-java/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.hahaha-zsq/winter-minio-spring-boot-starter.svg)](https://search.maven.org/artifact/io.github.hahaha-zsq/winter-minio-spring-boot-starter)
[![GitHub stars](https://img.shields.io/github/stars/hahaha-zsq/winter-minio-spring-boot-starter.svg?style=social&label=Stars)](https://github.com/hahaha-zsq/winter-minio-spring-boot-starter)

**企业级 S3 兼容对象存储 Spring Boot Starter**

*基于 AWS S3 SDK 构建，完美兼容 MinIO、阿里云 OSS、腾讯云 COS 等 S3 协议存储服务*

 演示视频地址：[aws-s3分片、断点，秒传](https://www.bilibili.com/video/BV1qkY3zFEzt/?share_source=copy_web&vd_source=c447f1819318b0fe977ae468afb3faf6)
</div>

---

## 🚀 项目简介

Winter MinIO Spring Boot Starter 是一个企业级的 S3 兼容对象存储解决方案，提供开箱即用的 `AmazonS3Template` 工具类，封装了存储桶管理、对象上传/下载、预签名 URL、分片上传、断点续传、进度监控、元数据管理等企业级功能。

### 🎯 为什么选择 Winter MinIO Starter？

- **🔧 开箱即用** - 零配置启动，自动装配 S3 客户端和模板类
- **🌐 广泛兼容** - 支持 MinIO、AWS S3、阿里云 OSS、腾讯云 COS 等 S3 协议存储
- **⚡ 性能优化** - 内置连接池、超时配置、重试机制，支持大文件分块上传
- **🔒 安全可靠** - 预签名 URL、访问策略管理、SSL/TLS 支持
- **📊 功能完整** - 涵盖存储桶管理、对象操作、元数据、生命周期、版本控制等全场景

## ✨ 功能特性

### 核心功能

| 功能模块 | 描述 | 支持状态 |
|---------|------|----------|
| 🗂️ **存储桶管理** | 创建、删除、检查存在性、列举所有桶 | ✅ |
| 📄 **对象操作** | 上传、下载、删除、复制、移动、重命名 | ✅ |
| 🔗 **预签名 URL** | 生成临时访问链接，支持 GET/PUT 操作 | ✅ |
| 🧩 **分片上传** | 大文件自动分片，支持并发和断点续传 | ✅ |
| 🏷️ **元数据管理** | 完整的对象元数据设置和获取 | ✅ |
| 🔄 **批量操作** | 批量删除、复制、移动对象 | ✅ |
| 🛡️ **访问策略** | 内置只读、只写、读写策略，支持自定义 | ✅ |
| 📋 **生命周期** | 自动化对象存储策略配置 | ✅ |
| 🔢 **版本控制** | 启用/禁用对象版本管理 | ✅ |

### 企业级特性

- **智能分片上传** - 自动处理大文件分片，支持并发上传和断点续传
- **实时进度监控** - 上传/下载进度实时回调，支持进度条显示
- **批量操作支持** - 批量删除、复制、移动对象操作
- **元数据管理** - 完整的对象元数据设置和获取能力
- **生命周期管理** - 自动化对象存储策略配置
- **安全访问控制** - 预签名 URL、访问策略、SSL/TLS 支持

## 📦 快速开始

### 1. 添加依赖

**Maven**
```xml
<dependency>
    <groupId>io.github.hahaha-zsq</groupId>
    <artifactId>winter-minio-spring-boot-starter</artifactId>
    <version>0.0.3</version>
</dependency>
```

**Gradle**
```gradle
implementation 'io.github.hahaha-zsq:winter-minio-spring-boot-starter:0.0.3'
```

### 2. 配置文件

**application.yml**
```yaml
winter-aws:
  # 必填配置
  access-key: minioadmin              # 访问密钥
  secret-key: minioadmin              # 秘密密钥
  endpoint: http://localhost:9000     # 服务端点
  bucket: default-bucket              # 默认存储桶
  
  # 可选配置
  region: us-east-1                   # 区域设置
  protocol: http                      # 连接协议（http/https，不区分大小写）
  path-style-access: true             # 路径风格访问（MinIO推荐true）
  max-connections: 500                # 最大连接数
  socket-timeout: 20000               # Socket超时（毫秒）
  connection-timeout: 10000           # 连接超时（毫秒）
  max-error-retry: 2                  # 失败重试次数
  custom-domain: https://cdn.example.com  # 自定义域名
  enabled: true                       # 是否启用（默认true）
```

**application.properties**
```properties
# 必填配置
winter-aws.access-key=minioadmin
winter-aws.secret-key=minioadmin
winter-aws.endpoint=http://localhost:9000
winter-aws.bucket=default-bucket

# 可选配置
winter-aws.region=us-east-1
winter-aws.protocol=http
winter-aws.path-style-access=true
winter-aws.max-connections=500
winter-aws.socket-timeout=20000
winter-aws.connection-timeout=10000
winter-aws.max-error-retry=2
winter-aws.custom-domain=https://cdn.example.com
winter-aws.enabled=true
```

### 3. 基本使用

```java
@RestController
@RequiredArgsConstructor
public class FileController {
    
    private final AmazonS3Template s3Template;
    
    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String objectKey = "uploads/" + System.currentTimeMillis() + "/" + file.getOriginalFilename();
            s3Template.putObject(objectKey, file, null);
            String fileUrl = s3Template.getGatewayUrl(objectKey);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成下载链接
     */
    @GetMapping("/download/{objectKey}")
    public ResponseEntity<String> getDownloadUrl(@PathVariable String objectKey) {
        // 生成15分钟有效期的下载链接
        String downloadUrl = s3Template.getObjectUrl(objectKey, 15);
        return ResponseEntity.ok(downloadUrl);
    }
    
    /**
     * 检查文件是否存在
     */
    @GetMapping("/exists/{objectKey}")
    public ResponseEntity<Boolean> checkFileExists(@PathVariable String objectKey) {
        boolean exists = s3Template.doesObjectExist(objectKey);
        return ResponseEntity.ok(exists);
    }
}
```
## ⚙️ 配置说明

### 完整配置参数

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `access-key` | String | ✅ | - | S3 访问密钥 ID |
| `secret-key` | String | ✅ | - | S3 秘密访问密钥 |
| `endpoint` | String | ✅ | - | S3 服务端点 URL |
| `bucket` | String | ✅ | - | 默认存储桶名称 |
| `region` | String | ❌ | `ap-east-1` | AWS 区域代码 |
| `path-style-access` | Boolean | ❌ | `true` | 是否使用路径风格访问。true 时使用 path-style 模式（如 http://endpoint/bucketname/object-key），适用于 nginx 反向代理和 S3 默认配置；false 时使用 virtual-hosted-style 模式（如 http://bucketname.endpoint/object-key），适用于阿里云等服务 |
| `protocol` | String | ❌ | `http` | 连接协议（http/https），不区分大小写，配置 https 时自动选用 Protocol.HTTPS |
| `max-connections` | Integer | ❌ | `500` | 客户端最大连接数 |
| `socket-timeout` | Integer | ❌ | `20000` | Socket 超时时间（毫秒） |
| `connection-timeout` | Integer | ❌ | `10000` | 连接超时时间（毫秒） |
| `max-error-retry` | Integer | ❌ | `2` | 失败请求重试次数 |
| `custom-domain` | String | ❌ | - | 自定义访问域名 |
| `enabled` | Boolean | ❌ | `true` | 是否启用自动配置 |

### 不同环境配置示例

<details>
<summary>点击展开环境配置示例</summary>

**开发环境 (application-dev.yml)**
```yaml
winter-aws:
  access-key: minioadmin
  secret-key: minioadmin
  endpoint: http://localhost:9000
  bucket: dev-bucket
  path-style-access: true
```

**生产环境 (application-prod.yml)**
```yaml
winter-aws:
  access-key: ${AWS_ACCESS_KEY}
  secret-key: ${AWS_SECRET_KEY}
  endpoint: https://s3.amazonaws.com
  bucket: prod-bucket
  region: us-east-1
  path-style-access: false
  custom-domain: https://cdn.yourdomain.com
  max-connections: 100
  connection-timeout: 60000
```

</details>

## 📖 API 文档

### 核心 API 概览

#### 🗂️ 存储桶管理

| 方法 | 描述 | 返回值 |
|------|------|--------|
| `createBucket(String bucketName)` | 创建存储桶 | `Bucket` |
| `createBucket(String bucketName, PolicyType policyType)` | 创建存储桶并设置内置策略 | `Bucket` |
| `removeBucket(String bucketName)` | 删除存储桶 | `void` |
| `existBucket(String bucketName)` | 检查存储桶是否存在 | `boolean` |
| `getAllBuckets()` | 列举所有存储桶 | `List<Bucket>` |
| `setBucketPolicy(String bucketName, PolicyType policyType)` | 设置存储桶策略 | `void` |

#### 📄 对象操作

| 方法 | 描述 | 返回值 |
|------|------|--------|
| `putObject(String objectName, InputStream stream)` | 上传文件（InputStream） | `PutObjectResult` |
| `putObject(String objectName, MultipartFile file, Map<String, String> metadata)` | 上传文件（MultipartFile） | `PutObjectResult` |
| `putObject(String bucketName, String objectName, byte[] bytes, String contentType)` | 上传文件（byte[]） | `PutObjectResult` |
| `getObject(String key)` | 获取对象 | `S3Object` |
| `getObjectInputStream(String key)` | 获取对象内容流 | `InputStream` |
| `getObjectInputStream(String key, long start, long end)` | 范围读取（断点续传） | `InputStream` |
| `downloadObject(String key, File file)` | 下载文件到本地 | `void` |
| `removeObject(String objectName)` | 删除对象 | `void` |
| `removeObjects(String bucketName, List<String> objectNames)` | 批量删除对象 | `void` |
| `copyObject(String sourceKey, String destKey)` | 复制对象 | `void` |
| `renameObject(String oldKey, String newKey)` | 重命名对象 | `void` |
| `doesObjectExist(String objectName)` | 检查对象是否存在 | `boolean` |
| `getObjectSize(String key)` | 获取对象大小 | `long` |
| `getObjectLastModified(String key)` | 获取最后修改时间 | `Date` |
| `getAllObjectsByPrefix(String prefix, String delimiter, Integer maxKeys)` | 前缀列举 | `ObjectListing` |

#### 🔗 预签名 URL

| 方法 | 描述 | 返回值 |
|------|------|--------|
| `getGatewayUrl(String objectName)` | 获取公开访问 URL | `String` |
| `getObjectUrl(String objectName)` | 生成预签名下载 URL（默认过期时间） | `String` |
| `getObjectUrl(String objectName, long expireTime, TimeUnit timeUnit)` | 生成预签名下载 URL（自定义过期时间） | `String` |
| `getObjectUrl(String bucket, String objectName, long expireTime, TimeUnit timeUnit, HttpMethod method)` | 生成自定义方法预签名 URL | `String` |
| `getPresignedObjectPutUrl(String objectName)` | 生成预签名上传 URL | `String` |
| `getPresignedObjectPutUrl(String bucket, String objectName, int expireTime, TimeUnit timeUnit)` | 生成预签名上传 URL（自定义过期时间） | `String` |

#### 🧩 分片上传

| 方法 | 描述 | 返回值 |
|------|------|--------|
| `initiateMultipartUpload(String objectName, String contentType)` | 初始化分片上传 | `String` (UploadId) |
| `uploadPart(String bucketName, String uploadId, String key, InputStream partStream, int partSize, int partNumber)` | 上传分片 | `UploadPartResult` |
| `listParts(String bucketName, String key, String uploadId)` | 列出已上传分片 | `ListPartsResult` |
| `completeMultipartUpload(String key, String uploadId, List<PartETag> partETags)` | 完成分片上传 | `void` |
| `abortMultipartUpload(String key, String uploadId)` | 中止分片上传 | `void` |
| `listMultipartUploads()` | 列出进行中的分片任务 | `ListMultipartUploadsResult` |

## ❓ 常见问题

<details>
<summary><strong>Q: 如何解决 "Connection refused" 错误？</strong></summary>

**A**: 检查 MinIO 服务是否正常启动，确认 endpoint 配置正确。

```bash
# 检查 MinIO 服务状态
docker ps | grep minio

# 测试连接
curl http://localhost:9000/minio/health/live
```
</details>

<details>
<summary><strong>Q: 上传大文件时内存占用过高？</strong></summary>

**A**: 使用分片上传功能，避免一次性加载大文件到内存。

```java
// 使用 InputStream 分片上传
String uploadId = s3Template.initiateMultipartUpload(objectKey, "application/octet-stream");
// 切分文件并逐个 uploadPart...
// 完成后调用 s3Template.completeMultipartUpload(objectKey, uploadId, parts);
```
</details>

<details>
<summary><strong>Q: 如何设置自定义域名？</strong></summary>

**A**: 在配置中设置 `custom-domain` 参数，并确保 DNS 解析正确。

```yaml
winter-aws:
  custom-domain: https://cdn.yourdomain.com
  # 其他配置...
```
</details>

<details>
<summary><strong>Q: 支持哪些 S3 兼容服务？</strong></summary>

**A**: 支持所有兼容 S3 API 的对象存储服务：
- ✅ MinIO
- ✅ AWS S3
- ✅ 阿里云 OSS
- ✅ 腾讯云 COS
- ✅ 华为云 OBS
- ✅ 七牛云 Kodo
</details>

## 🤝 贡献

我们欢迎所有形式的贡献！请查看 [贡献指南](CONTRIBUTING.md) 了解如何参与项目开发。

### 快速贡献

1. 🍴 Fork 本仓库
2. 🔧 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 📝 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 📤 推送到分支 (`git push origin feature/AmazingFeature`)
5. 🔄 开启 Pull Request

### 问题反馈

- 🐛 [提交 Bug 报告](../../issues/new?template=bug_report.md)
- 💡 [提交功能建议](../../issues/new?template=feature_request.md)
- 📖 [文档改进建议](../../issues/new?template=documentation.md)

## 📄 许可证

本项目采用 [Apache License 2.0](LICENSE) 开源协议。

## 🙏 致谢

感谢以下开源项目和贡献者：

- [AWS SDK for Java](https://github.com/aws/aws-sdk-java) - 提供 S3 客户端支持
- [Spring Boot](https://github.com/spring-projects/spring-boot) - 自动配置框架
- [MinIO](https://github.com/minio/minio) - 高性能对象存储服务
- 所有为这个项目做出贡献的开发者 ❤️

---

<div align="center">

**如果这个项目对您有帮助，请给我们一个 ⭐️**

[⬆ 回到顶部](#-winter-minio-spring-boot-starter)

</div>

