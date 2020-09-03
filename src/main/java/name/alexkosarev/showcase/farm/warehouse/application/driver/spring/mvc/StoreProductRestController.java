package name.alexkosarev.showcase.farm.warehouse.application.driver.spring.mvc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.alexkosarev.showcase.farm.warehouse.application.port.input.StoreProductInputPort;
import name.alexkosarev.showcase.farm.warehouse.domain.command.StoreProduct;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

@Slf4j
@RequestMapping("/warehouse/api/stored-products")
@RequiredArgsConstructor
public class StoreProductRestController {

    @NonNull
    private final StoreProductInputPort storeProduct;

    @NonNull
    private final TransactionOperations transactionOperations;

    @PostMapping
    public ResponseEntity<?> handleStoreProduct(@NonNull @RequestBody StoreProductRequest request) {
        ResponseEntity<?> responseEntity;
        try {
            this.transactionOperations.executeWithoutResult(status -> this.storeProduct
                    .storeProduct(StoreProduct.of(Instant.now(), request.getProductId(), request.getAmount())));
            responseEntity = ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            responseEntity = ResponseEntity.badRequest().build();
            log.error(e.getMessage(), e);
        }

        return responseEntity;
    }
}
