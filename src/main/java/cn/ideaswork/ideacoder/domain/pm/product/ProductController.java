package cn.ideaswork.ideacoder.domain.pm.product;

import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

@RestController
@Api(tags = "产品 API")

@RequestMapping("/products")
@CrossOrigin()
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping
    @ApiOperation("添加产品")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product saveProduct(@RequestBody Product product) throws Exception {
        product.setCreatetime(new Date());
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new Exception("请登录后再操作");
        }
        product.setUserid(loginUser.getId())
                .setUserName(loginUser.getName());
        product.setId(UUID.randomUUID().toString());
        return productService.saveProduct(product);
    }

    @GetMapping
    @ApiOperation("获取 产品列表")
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 产品")
    public Product getProduct(@PathVariable("id") final String id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据主键更新产品")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateProduct(@RequestBody Product product, @PathVariable("id") final String id) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new Exception("请登录后再操作");
        }
        Product productById = productService.getProductById(id);
        if (productById.getUserid().equals(loginUser.getId())) {
            if(productById.getCreatetime()==null){
                product.setCreatetime(new Date());
            }
            return ResponseEntity.ok(productService.updateProductById(product, id));
        } else {
            return ResponseEntity.badRequest().body("只能修改自己创建的产品");
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据主键 id 删除产品")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteProductById(@PathVariable("id") final String id) throws Exception {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            throw new Exception("请登录后再操作");
        }
        Product productById = productService.getProductById(id);
        if (productById.getUserid().equals(loginUser.getId())) {
            productService.deleteProductById(id);
        } else {
            return ResponseEntity.badRequest().body("只能删除自己创建的产品");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看产品是否存在")
    public Boolean isExistProduct(@PathVariable("id") final String id) {
        return productService.isProductExist(id);
    }

    @GetMapping("/getPageList")
    @ApiOperation("分页条件查询")
    public Page<Product> getPageByCondition(ProductDTO productDTO,
                                            @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return productService.getPageByCondition(productDTO, pageable);
    }

    @GetMapping("/getList")
    @ApiOperation("分页条件查询")
    public List<Product> getListByCondition(ProductDTO productDTO) {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null) {
            productDTO.setIsPublished(true);
        }
        return productService.getListByCondition(productDTO);
    }
}