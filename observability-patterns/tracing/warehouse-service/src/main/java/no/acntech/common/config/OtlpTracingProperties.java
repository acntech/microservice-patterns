/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.acntech.common.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "management.otlp.tracing")
public class OtlpTracingProperties {

    @NotNull
    private Boolean enabled = true;
    @Valid
    @NotNull
    private ExportProperties export = new ExportProperties();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public ExportProperties getExport() {
        return export;
    }

    public void setExport(ExportProperties export) {
        this.export = export;
    }

    public static class ExportProperties {
        @Valid
        @NotNull
        private Endpoint grpc = new Endpoint("http://localhost:4317", Duration.ofSeconds(10));
        @Valid
        @NotNull
        private Endpoint rest = new Endpoint("http://localhost:4318/v1/traces", Duration.ofSeconds(10));

        public Endpoint getGrpc() {
            return grpc;
        }

        public void setGrpc(Endpoint grpc) {
            this.grpc = grpc;
        }

        public Endpoint getRest() {
            return rest;
        }

        public void setRest(Endpoint rest) {
            this.rest = rest;
        }

        public static class Endpoint {

            @NotBlank
            private String url;
            @NotNull
            private Duration timeout;

            public Endpoint() {
            }

            public Endpoint(String url, Duration timeout) {
                this.url = url;
                this.timeout = timeout;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public Duration getTimeout() {
                return timeout;
            }

            public void setTimeout(Duration timeout) {
                this.timeout = timeout;
            }
        }
    }
}
