package org.example.service.integrations.jpa.repositories;

import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.model.orders.DeliveryOrderRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@RepositoryDefinition(domainClass = DeliveryOrder.class, idClass = Long.class)
public interface JpaDeliveryOrderRepository extends DeliveryOrderRepository {

    @Override
    @Query(value = "SELECT * FROM orders o WHERE o.id = :id", nativeQuery = true)
    Optional<DeliveryOrder> findById(@Param("id") Long id);

    @Override
    @Query(value = "SELECT * FROM orders o WHERE o.urgency = 'ASAP' AND o.execute_at < CURRENT_DATE() LIMIT :count OFFSET :offset", nativeQuery = true)
    List<DeliveryOrder> findScheduledOrders(@Param("offset") long offset, @Param("count") int count);

    @Override
    @Query(value = "SELECT * FROM orders o LIMIT :count OFFSET :offset", nativeQuery = true)
    List<DeliveryOrder> findAllOrders(@Param("offset") long offset, @Param("count") int count);

    @Override
    void save(DeliveryOrder order);
}
