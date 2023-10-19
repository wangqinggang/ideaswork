package cn.ideaswork.ideacoder.domain.sale.saleproduct;

import cn.ideaswork.ideacoder.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SaleProductService {
  SaleProduct saveSaleProduct(SaleProduct saleProduct);

  List<SaleProduct> getAllSaleProducts();

  SaleProduct getSaleProductById(final String id);

  SaleProduct updateSaleProductById(SaleProduct saleProduct, final String id);

  void deleteSaleProductById(final String id);

  Boolean isSaleProductExist(final String id);

  Page<SaleProduct> getPageByCondition(SaleProductDTO saleProductDTO,Pageable pageable);

  List<SaleProduct> getListByCondition(SaleProductDTO saleProductDTO);
  Boolean hasSaleProduct(String id, User loginUser);
}
