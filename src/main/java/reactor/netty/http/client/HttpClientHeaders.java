/*
 * Copyright (c) 2011-Present Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.netty.http.client;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import io.netty.bootstrap.Bootstrap;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import reactor.netty.tcp.TcpClient;

/**
 * @author Stephane Maldini
 */
final class HttpClientHeaders extends HttpClientOperator
		implements Function<Bootstrap, Bootstrap> {

	final Consumer<? super HttpHeaders> headers;

	HttpClientHeaders(HttpClient client, Consumer<? super HttpHeaders> headers) {
		super(client);
		this.headers = Objects.requireNonNull(headers, "headers");
	}

	@Override
	protected TcpClient tcpConfiguration() {
		return source.tcpConfiguration().bootstrap(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Bootstrap apply(Bootstrap bootstrap) {
		HttpHeaders h = HttpClientConfiguration.headers(bootstrap);
		if (h == null) {
			h = new DefaultHttpHeaders();
		}

		headers.accept(h);
		if (!h.isEmpty()) {
			HttpClientConfiguration.headers(bootstrap, h);
		}
		return bootstrap;
	}
}
