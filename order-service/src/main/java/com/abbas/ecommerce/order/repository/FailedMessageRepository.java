package com.abbas.ecommerce.order.repository;

import com.abbas.ecommerce.order.model.FailedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedMessageRepository extends JpaRepository<FailedMessage,Long> {
}
