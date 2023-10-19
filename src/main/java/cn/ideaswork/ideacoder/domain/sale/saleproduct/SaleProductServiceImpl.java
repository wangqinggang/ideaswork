package cn.ideaswork.ideacoder.domain.sale.saleproduct;

import cn.ideaswork.ideacoder.domain.sale.order.OrdersService;
import cn.ideaswork.ideacoder.domain.user.User;
import cn.ideaswork.ideacoder.domain.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class SaleProductServiceImpl implements SaleProductService {
    @Autowired
    private SaleProductDao saleProductDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OrdersService orderService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public SaleProduct saveSaleProduct(final SaleProduct SaleProduct) {
        return saleProductDao.save(SaleProduct);
    }

    @Override
    public List<SaleProduct> getAllSaleProducts() {
        return saleProductDao.findAll();
    }

    @Override
    public SaleProduct getSaleProductById(final String id) {
        return saleProductDao.findById(id).orElse(new SaleProduct());
    }

    @Override
    @Transactional
    public SaleProduct updateSaleProductById(final SaleProduct SaleProduct, final String id) {
        SaleProduct SaleProductDb = saleProductDao.findById(id).orElse(new SaleProduct());
        BeanUtils.copyProperties(SaleProduct, SaleProductDb);
        return saleProductDao.save(SaleProductDb);
    }

    @Override
    @Transactional
    public void deleteSaleProductById(final String id) {
        saleProductDao.deleteById(id);
    }

    @Override
    public Boolean isSaleProductExist(final String id) {
        return saleProductDao.existsById(id);
    }

    @Override
    public Page<SaleProduct> getPageByCondition(final SaleProductDTO SaleProductDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(SaleProductDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(SaleProductDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(SaleProductDTO.getName())) {
            Pattern pattern = Pattern.compile("");// TODO
            Criteria criteria = Criteria.where("name").regex(SaleProductDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(SaleProductDTO.getImgurl())) {
            Criteria criteria = Criteria.where("imgurl").is(SaleProductDTO.getImgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(SaleProductDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").is(SaleProductDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(SaleProductDTO.getClassification())) {
            Criteria criteria = Criteria.where("classification").regex(SaleProductDTO.getClassification());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getIsfree() != null) {
            Criteria criteria = Criteria.where("isfree").is(SaleProductDTO.getIsfree());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getPrice() != null) {
            Criteria criteria = Criteria.where("price").is(SaleProductDTO.getPrice());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getOriginalPrice() != null) {
            Criteria criteria = Criteria.where("originalPrice").is(SaleProductDTO.getOriginalPrice());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getIsPopular() != null) {
            Criteria criteria = Criteria.where("isPopular").is(SaleProductDTO.getIsPopular());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getScore() != null) {
            Criteria criteria = Criteria.where("score").is(SaleProductDTO.getScore());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getBuyNum() != null) {
            Criteria criteria = Criteria.where("buyNum").is(SaleProductDTO.getBuyNum());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getCzsj() != null) {
            Criteria criteria = Criteria.where("czsj").is(SaleProductDTO.getCzsj());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getIsPublished() != null) {
            Criteria criteria = Criteria.where("isPublished").is(SaleProductDTO.getIsPublished());
            query.addCriteria(criteria);
        }
        return this.listToPage(mongoTemplate.find(query, SaleProduct.class), pageable);
    }

    @Override
    public List<SaleProduct> getListByCondition(final SaleProductDTO SaleProductDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(SaleProductDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(SaleProductDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(SaleProductDTO.getName())) {
            Criteria criteria = Criteria.where("name").is(SaleProductDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(SaleProductDTO.getImgurl())) {
            Criteria criteria = Criteria.where("imgurl").is(SaleProductDTO.getImgurl());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(SaleProductDTO.getInfo())) {
            Criteria criteria = Criteria.where("info").is(SaleProductDTO.getInfo());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(SaleProductDTO.getClassification())) {
            Criteria criteria = Criteria.where("classification").regex(SaleProductDTO.getClassification());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getIsfree() != null) {
            Criteria criteria = Criteria.where("isfree").is(SaleProductDTO.getIsfree());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getPrice() != null) {
            Criteria criteria = Criteria.where("price").is(SaleProductDTO.getPrice());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getOriginalPrice() != null) {
            Criteria criteria = Criteria.where("originalPrice").is(SaleProductDTO.getOriginalPrice());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getIsPopular() != null) {
            Criteria criteria = Criteria.where("isPopular").is(SaleProductDTO.getIsPopular());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getScore() != null) {
            Criteria criteria = Criteria.where("score").is(SaleProductDTO.getScore());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getBuyNum() != null) {
            Criteria criteria = Criteria.where("buyNum").is(SaleProductDTO.getBuyNum());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getCzsj() != null) {
            Criteria criteria = Criteria.where("czsj").is(SaleProductDTO.getCzsj());
            query.addCriteria(criteria);
        }
        if (SaleProductDTO.getIsPublished() != null) {
            Criteria criteria = Criteria.where("isPublished").is(SaleProductDTO.getIsPublished());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, SaleProduct.class);
    }

    @Override
    public Boolean hasSaleProduct(String SaleProductId, User loginUser) {
        User userById = userService.getUserById(loginUser.getId());
        List<String> mySaleProducts = userById.getMyCourses();

        if(mySaleProducts!=null && mySaleProducts.size()>0){
            boolean contains = mySaleProducts.contains(SaleProductId);
           if(contains){
               return true;
           }else{
              return syncSaleProduct(SaleProductId, loginUser, userById, mySaleProducts);
           }
        }else{
            // 确认当前用户的课程订单支付状态
            return syncSaleProduct(SaleProductId, loginUser, userById, mySaleProducts);
        }
    }

    /**
     * 同步当前课程订单并同步到当前用户信息
     * @param SaleProductId
     * @param loginUser
     * @param userById
     * @param mySaleProducts
     */
    private Boolean syncSaleProduct(String SaleProductId, User loginUser, User userById, List<String> mySaleProducts) {
        // 确认当前用户的课程订单支付状态,若支付成功则同步我的课程 TODO
            Set<String> mySaleProductsSet = new HashSet<>(mySaleProducts);
            mySaleProductsSet.add(SaleProductId);
            userById.setMyCourses(new ArrayList<>(mySaleProductsSet));
            userService.updateUserById(userById, userById.getId());
            return true;
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public SaleProduct mapToEntity(final SaleProductDTO SaleProductDTO, final SaleProduct SaleProduct) {
        BeanUtils.copyProperties(SaleProductDTO, SaleProduct);
        return SaleProduct;
    }

    public SaleProductDTO mapToDTO(final SaleProduct SaleProduct, final SaleProductDTO SaleProductDTO) {
        BeanUtils.copyProperties(SaleProduct, SaleProductDTO);
        return SaleProductDTO;
    }
}