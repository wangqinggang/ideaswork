package cn.ideaswork.ideacoder.domain.sale.saleproduct;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleProductDao extends MongoRepository<SaleProduct, String> {
}
