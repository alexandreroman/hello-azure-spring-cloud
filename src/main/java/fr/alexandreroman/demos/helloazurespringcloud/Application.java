/*
 * Copyright (c) 2019 Pivotal Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.alexandreroman.demos.helloazurespringcloud;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@ConfigurationProperties("app")
@ConstructorBinding
@Getter
@RequiredArgsConstructor
class AppProperties {
    private final String message;
}

@RestController
@RequiredArgsConstructor
class HelloController {
    private final AppProperties props;

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    String hello() {
        return props.getMessage();
    }
}

@RestController
@RequiredArgsConstructor
class CounterController {
    private final RedisAtomicInteger counter;

    @GetMapping("/inc")
    Map<String, Object> incrementCounter() {
        final var value = counter.incrementAndGet();
        return Map.of("counter", value);
    }
}

@Configuration
class RedisConfig {
    @Bean
    RedisAtomicInteger counter(RedisConnectionFactory rcf) {
        return new RedisAtomicInteger("counter", rcf);
    }
}
