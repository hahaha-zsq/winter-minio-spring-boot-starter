package com.zsq.winter.minio.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.zsq.winter.minio.service.AmazonS3Template;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({AmazonS3Properties.class})
/*
@EnableConfigurationProperties(A.class)的作用就是如果 A 这个类上使用了 @ConfigurationProperties 注解,那么 A 这个类会与 xxx.properties/xxx.yml 进行动态绑定,并且会将 A 这个类加入 IOC 容器中,并交由 IOC 容器进行管理
如果添加了@EnableConfigurationProperties注解  在使用@ConfigurationProperties 注解的时候    实体类就不需要加上 @Component 注解了
*/
//只有当配置中明确设置了"winter-aws.enabled=true"时或者完全没定义"winter-aws.enabled"时，被此注解标记的配置或Bean才会被Spring容器初始化
@ConditionalOnProperty(
        prefix = "winter-aws",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = true
)
@Configuration
public class AmazonS3AutoConfiguration {
    //@Resource
    //private MinioProperties minioProperties;  可以直接注入该依赖，或者直接在方法参数中引入该类型的参数


    //@ConditionalOnMissingBean(AmazonS3.class) 希望在容器中不存在名为 `AmazonS3` 的 Bean 时，才创建一个新的 `AmazonS3` 实例
    @ConditionalOnMissingBean({AmazonS3.class})
    @Bean
    public AmazonS3 amazon(AmazonS3Properties minioProperties) {
        // 禁用AWS SDK 1.x弃用警告
        // 禁用 AWS SDK 1.x 弃用警告（SDK v1 在 2024 年进入维护期，不再有新功能更新）
        System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");

        // 创建客户端配置对象，所有连接参数统一通过该对象传递
        ClientConfiguration config = new ClientConfiguration();
        // 设置连接池最大连接数，避免高并发下连接数不足；默认 500
        config.setMaxConnections(minioProperties.getMaxConnections());
        // 设置 Socket 超时时间（毫秒），数据传输最大等待时间；默认 20000ms
        config.setSocketTimeout(minioProperties.getSocketTimeout());
        // 设置失败请求重试次数，网络抖动时自动重试；默认 2 次
        config.setMaxErrorRetry(minioProperties.getMaxErrorRetry());
        // 设置连接超时时间（毫秒），建立 TCP 连接的最大等待时间；默认 10000ms
        config.setConnectionTimeout(minioProperties.getConnectionTimeout());
        // 根据配置的协议字符串（http/https）选择对应枚举，不区分大小写；默认 HTTP
        Protocol protocol = minioProperties.getProtocol().equalsIgnoreCase("https") ? Protocol.HTTPS : Protocol.HTTP;
        config.setProtocol(protocol);
        // 启用 Expect: 100-continue 握手，上传大文件前先等待服务端确认，避免无效数据传输
        config.setUseExpectContinue(true);

        // 使用 accessKey 和 secretKey 创建 AWS 凭证对象
        AWSCredentials credentials = new BasicAWSCredentials(minioProperties.getAccessKey(), minioProperties.getSecretKey());
        // 封装 S3 服务端点（Endpoint）和区域（Region），Endpoint 格式如 http://localhost:9000
        AwsClientBuilder.EndpointConfiguration endPoint = new AwsClientBuilder.EndpointConfiguration(
                minioProperties.getEndpoint(), minioProperties.getRegion());
        // 通过标准建造器构建 AmazonS3 客户端，链式注入所有配置
        return AmazonS3ClientBuilder.standard()
                .withClientConfiguration(config)          // 注入连接参数
                .withCredentials(new AWSStaticCredentialsProvider(credentials)) // 注入认证信息
                .withEndpointConfiguration(endPoint)      // 注入服务端点和区域
                .withPathStyleAccessEnabled(minioProperties.getPathStyleAccess()) // true=路径风格(false=虚拟主机风格)
                .build();
       /* withPathStyleAccessEnabled 是Amazon S3客户端配置中的一个选项，它用于指定是否启用路径样式访问（Path-Style Access）。
        在Amazon S3中，有两种不同的URL访问样式：
        虚拟主机访问样式（Virtual Hosted-Style Access）： 默认情况下，Amazon S3的访问样式是虚拟主机访问样式。在虚拟主机访问样式中，访问一个桶中的对象的URL的格式为
        http://bucket-name.s3.amazonaws.com/object-key。这种方式更符合RESTful风格，并且通常更简洁。
        路径样式访问（Path-Style Access）： 在路径样式访问中，访问一个桶中的对象的URL的格式为
        http://s3.amazonaws.com/bucket-name/object-key。这种方式将桶名作为URL的一部分，更类似传统的URL路径结构。
        withPathStyleAccessEnabled 的意义在于，当启用路径样式访问时，你可以通过类似
        http://s3.amazonaws.com/bucket-name/object-key 的URL 访问对象，而无需使用特定的桶名前缀
        （例如bucket-name.s3.amazonaws.com）。这在某些特殊情况下很有用，例如在使用代理服务器或者某些第三方库时，这些情况下虚拟主机访问样式可能会遇到问题。*/
    }

    @Bean
    @ConditionalOnMissingBean({AmazonS3Template.class})
    @ConditionalOnBean({AmazonS3.class})
    //用于在 Spring 容器中存在AmazonS3 时且不存在AmazonS3Template才会生效
    public AmazonS3Template amazonS3Template(AmazonS3Properties amazonS3Properties,AmazonS3 amazonS3){
        return new AmazonS3Template(amazonS3Properties,amazonS3);
    }
}
