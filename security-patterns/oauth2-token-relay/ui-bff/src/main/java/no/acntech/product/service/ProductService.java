package no.acntech.product.service;

import no.acntech.config.ServiceProperties;
import no.acntech.product.model.ProductDto;
import no.acntech.product.model.ProductQuery;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Service
public class ProductService {

    private final ServiceProperties properties;
    private final WebClient webClient;

    public ProductService(final ServiceProperties properties,
                          final WebClient webClient) {
        this.properties = properties;
        this.webClient = webClient;
    }

    public List<ProductDto> findProducts(@NotNull ProductQuery productQuery, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getProducts().getUrl();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
        if (productQuery.getName() != null) {
            uriComponentsBuilder.queryParam("name", productQuery.getName());
        }
        URI uri = uriComponentsBuilder.build()
                .toUri();
        return webClient.get()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .collectList()
                .block();
    }

    public ProductDto getProduct(@NotNull UUID productId, OAuth2AuthorizedClient authorizedClient) {
        String url = properties.getProducts().getUrl();
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/{productId}")
                .buildAndExpand(productId)
                .toUri();
        return webClient.get()
                .uri(uri)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }
}
