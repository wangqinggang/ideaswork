package cn.ideaswork.ideacoder.domain.sale.order;

import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.infrastructure.tools.SysTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.Boolean;
import java.lang.String;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
@Api(
        tags = "订单 API"
)
@CrossOrigin
@RequestMapping("/orderss")
public class OrdersController {
    @Autowired
    OrdersService ordersService;


    @PostMapping("/createUnderLineOrder")
    @ApiOperation("创建线下订单")
    public ResponseEntity<?> createUnderLineOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null || StringUtils.isBlank(loginUser.getId())) {
            return ResponseEntity.badRequest().body("请登录后再进行操作");
        }
        return ordersService.createUnderLineOrder(createOrderDTO, loginUser);
    }

    /**
     * 创建课程订单
     * @param createOrderDTO
     * @return
     */
    @PostMapping("/createOrder")
    @ApiOperation("创建订单")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null || StringUtils.isBlank(loginUser.getId())) {
            return ResponseEntity.badRequest().body("请登录后再进行操作");
        }
        return ordersService.studentCreateOrder(createOrderDTO, loginUser);
    }

    /**
     * 创建销售产品订单
     * @param createOrderDTO
     * @return
     */
    @PostMapping("/createSaleProductOrder")
    @ApiOperation("创建销售产品订单")
    public ResponseEntity<?> createSaleProductOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null || StringUtils.isBlank(loginUser.getId())) {
            return ResponseEntity.badRequest().body("请登录后再进行操作");
        }
        return ordersService.CreateSaleProductOrder(createOrderDTO, loginUser);
    }

    @GetMapping("/getOrderStatus/{orderId}")
    @ApiOperation("查询用户当前订单是否支付成功")
    public ResponseEntity<?> getOrderStatus(@PathVariable("orderId") String orderId) {
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null || StringUtils.isBlank(loginUser.getId())) {
            return ResponseEntity.badRequest().body("请登录后再进行操作");
        }
        return ordersService.getOrderStatus(orderId,loginUser);

    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("添加订单")
    public Orders saveOrders(@RequestBody Orders orders) {
        orders.setId(UUID.randomUUID().toString());
        return ordersService.saveOrders(orders);
    }

    @GetMapping
    @ApiOperation("获取 订单列表")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Orders> getOrderss() {
        return ordersService.getAllOrderss();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据主键获取一条 订单")
    public Orders getOrders(@PathVariable("id") final String id) {
        return ordersService.getOrdersById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键更新订单")
    public Orders updateOrders(@RequestBody Orders orders, @PathVariable("id") final String id) {
        return ordersService.updateOrdersById(orders, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("根据主键 id 删除订单")
    public void deleteOrdersById(@PathVariable("id") final String id) {
        ordersService.deleteOrdersById(id);
    }

    @GetMapping("/isExist/{id}")
    @ApiOperation("根据主键 id 查看订单是否存在")
    public Boolean isExistOrders(@PathVariable("id") final String id) {
        return ordersService.isOrdersExist(id);
    }

    @GetMapping("/getPageList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation("分页条件查询")
    public Page<Orders> getPageByCondition(OrdersDTO ordersDTO,
                                           @PageableDefault(value = 1, size = 20) Pageable pageable) {
        return ordersService.getPageByCondition(ordersDTO, pageable);
    }

    /**
     * 查看自己的订单列表
     */
    @GetMapping("/getMyOrders")
    @ApiOperation("根据平台类型查看本年度自己的订单列表")
    public List<Orders> getMyOrders() {
        String code = Orders.OrderType.getCode("vms");
        if(code.equals("-1")){
            throw new RuntimeException("订单类型错误");
        }
        User loginUser = SysTools.getLoginUser();
        if (loginUser == null || StringUtils.isBlank(loginUser.getId())) {
            throw new RuntimeException("请登录后再进行操作");
        }
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setStatus(Orders.OrderStatus.PAYED.getCode());
        ordersDTO.setUserid(loginUser.getId());
        ordersDTO.setType(code);
        // 只显示当前年度的订单
        ordersDTO.setYear(SysTools.getYear());
        return ordersService.getListByCondition(ordersDTO);
    }
}