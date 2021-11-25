package com.jonasermert.springlasagne.repositories;

import com.jonasermert.springlasagne.domain.Order;

public interface OrderRepository {

    Order save(Order order);
}
