package com.jonasermert.springlasagne.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonasermert.springlasagne.domain.Lasagne;
import com.jonasermert.springlasagne.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface JdbcOrderRepository implements OrderRepository {

    private SimpleJdbcInsert orderInserter;
    private SimpleJdbcInsert orderLasagneInserter;
    private ObjectMapper objectMapper;

    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbc) {
        this.orderInserter = new SimpleJdbcInsert(jdbc).withTableName("Lasagne_Order").usingGeneratedKeyColumns("id");
        this.orderLasagneInserter = new SimpleJdbcInsert(jdbc).withTableName("Lasagne_Order_Lasagnes");
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        order.setId(orderId);
        List<Lasagne> lasagnes = order.getLasagnes();
        for (Lasagne lasagne : lasagnes) {
            saveTacoToOrder(lasagne, orderId);
        }
        return order;
    }

    private long saveOrderDetails(Order order) {
        @SuppressWarnings("unchecked")
        Map<String, Object> values = objectMapper.convertValue(order, Map.class);
        values.put("placedAt", order.getPlacedAt());
        long orderId = orderInserter.executeAndReturnKey(values).longValue();
        return orderId;
    }

    private void saveTacoToOrder(Lasagne lasagne, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("lasagneOrder", orderId);
        values.put("lasagne", lasagne.getId());
        orderLasagneInserter.execute(values);
    }



}
