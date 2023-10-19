package cn.ideaswork.ideacoder.domain.sale.order;

import java.lang.String;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersDao extends MongoRepository<Orders, String> {
    List<Orders> findAllByUserid(String id);
}
