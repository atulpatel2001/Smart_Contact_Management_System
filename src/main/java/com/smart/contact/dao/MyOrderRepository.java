package com.smart.contact.dao;

import com.smart.contact.model.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyOrderRepository extends JpaRepository<MyOrder,Long> {

public MyOrder findByOrderId(String orderId);





}
