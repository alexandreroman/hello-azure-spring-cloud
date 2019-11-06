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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testIndex() {
        final var message = restTemplate.getForObject("/", String.class);
        assertThat(message).isEqualTo("Hello Azure Spring Cloud!");
    }

    @Test
    void testCounter() {
        final var values = restTemplate.getForObject("/inc", Map.class);
        assertThat((int) values.get("counter")).isGreaterThanOrEqualTo(1);
    }
}

@Component
class EmbeddedRedis {
    private RedisServer redisServer;

    @PostConstruct
    public void init() {
        redisServer = new RedisServer();
        redisServer.start();
    }

    @PreDestroy
    public void destroy() {
        redisServer.stop();
        redisServer = null;
    }
}
