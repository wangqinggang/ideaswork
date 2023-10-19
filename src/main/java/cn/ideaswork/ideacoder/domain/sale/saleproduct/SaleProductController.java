package cn.ideaswork.ideacoder.domain.sale.saleproduct;

import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Api(
    tags = "销售产品 API"
)
@CrossOrigin
@RequestMapping("/saleProduct")
public class SaleProductController {
  @Autowired
  SaleProductService saleProductService;

  @PostMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("添加产品")
  public SaleProduct saveSaleProduct(@RequestBody SaleProduct SaleProduct) {
    SaleProduct.setId(UUID.randomUUID().toString());
    SaleProduct.setCzsj(new Date());
    return saleProductService.saveSaleProduct(SaleProduct);
  }

  @GetMapping
  @ApiOperation("获取 产品列表")
  public List<SaleProduct> getSaleProducts() {
    return saleProductService.getAllSaleProducts();
  }

  @GetMapping("/{id}")
  @ApiOperation("根据主键获取一条 产品")
  public SaleProduct getSaleProduct(@PathVariable("id") final String id) {
    return saleProductService.getSaleProductById(id);
  }

  @GetMapping("/hasSaleProduct/{id}")
  @ApiOperation("获取当前用户是否拥有当前产品-(课程用)")
  public Boolean getIsHave(@PathVariable("id") final String id) {
    User loginUser = SysTools.getLoginUser();
    if (loginUser == null || StringUtils.isBlank(loginUser.getId())) {
      return false;
    }
    return saleProductService.hasSaleProduct(id,loginUser);
  }


  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("根据主键更新产品")
  public SaleProduct updateSaleProduct(@RequestBody SaleProduct SaleProduct, @PathVariable("id") final String id) {
    return saleProductService.updateSaleProductById(SaleProduct,id);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("根据主键 id 删除产品")
  public void deleteSaleProductById(@PathVariable("id") final String id) {
    SaleProduct SaleProductById = saleProductService.getSaleProductById(id);
    SaleProductById.setIsPublished(false);
    saleProductService.updateSaleProductById(SaleProductById,SaleProductById.getId());
//    SaleProductService.deleteSaleProductById(id);
  }

  @GetMapping("/isExist/{id}")
  @ApiOperation("根据主键 id 查看产品是否存在")
  public Boolean isExistSaleProduct(@PathVariable("id") final String id) {
    return saleProductService.isSaleProductExist(id);
  }

  @GetMapping("/getPageList")
  @ApiOperation("分页条件查询")
  public Page<SaleProduct> getPageByCondition(SaleProductDTO SaleProductDTO,
                                         @PageableDefault(value = 1, size = 20) Pageable pageable) {
    SaleProductDTO.setIsPublished(true);
    return saleProductService.getPageByCondition(SaleProductDTO ,pageable);
  }

  @GetMapping("/adminGetPageList")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation("分页条件查询")
  public Page<SaleProduct> adminGetPageList(SaleProductDTO SaleProductDTO,
                                       @PageableDefault(value = 1, size = 20) Pageable pageable) {
    return saleProductService.getPageByCondition(SaleProductDTO ,pageable);
  }
}