package cn.ideaswork.ideacoder.domain.pm.product;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
  Product saveProduct(Product product);

  List<Product> getAllProducts();

  Product getProductById(final String id);

  Product updateProductById(Product product, final String id);

  void deleteProductById(final String id);

  Boolean isProductExist(final String id);

  Page<Product> getPageByCondition(ProductDTO productDTO, Pageable pageable);

  List<Product> getListByCondition(ProductDTO productDTO);
}
