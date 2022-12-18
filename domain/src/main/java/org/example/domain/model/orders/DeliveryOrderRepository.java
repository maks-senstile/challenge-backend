package org.example.domain.model.orders;

import java.util.List;
import java.util.Optional;

public interface DeliveryOrderRepository {

    Optional<DeliveryOrder> findById(Long id);

    // IDK which way to implement pagination is more suitable in this case, but the lack of pagination is obviously baddest. So I used usual offset pagination
    List<DeliveryOrder> findScheduledOrders(long offset, int count);

    List<DeliveryOrder> findAllOrders(long offset, int count);

    void save(DeliveryOrder order);
}
