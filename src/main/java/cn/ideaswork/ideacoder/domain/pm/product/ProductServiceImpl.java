package cn.ideaswork.ideacoder.domain.pm.product;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public Product saveProduct(final Product product) {
        return productDao.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @Override
    public Product getProductById(final String id) {
        return productDao.findById(id).orElse(new Product());
    }

    @Override
    @Transactional
    public Product updateProductById(final Product product, final String id) {
        Product productDb = productDao.findById(id).orElse(new Product());
        BeanUtils.copyProperties(product, productDb);
        return productDao.save(productDb);
    }

    @Override
    @Transactional
    public void deleteProductById(final String id) {
        productDao.deleteById(id);
    }

    @Override
    public Boolean isProductExist(final String id) {
        return productDao.existsById(id);
    }

    @Override
    public Page<Product> getPageByCondition(final ProductDTO productDTO, final Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(productDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(productDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(productDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getUserName())) {
            Criteria criteria = Criteria.where("userName").is(productDTO.getUserName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getPersonaids())) {
            Criteria criteria = Criteria.where("personaids").is(productDTO.getPersonaids());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getPersonaName())) {
            Criteria criteria = Criteria.where("personaName").is(productDTO.getPersonaName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getProblemid())) {
            Criteria criteria = Criteria.where("problemid").is(productDTO.getProblemid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getProblemName())) {
            Criteria criteria = Criteria.where("problemName").is(productDTO.getProblemName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(productDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getIntro())) {
            Criteria criteria = Criteria.where("intro").regex(productDTO.getIntro());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getSolution())) {
            Criteria criteria = Criteria.where("solution").regex(productDTO.getSolution());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getPfunction())) {
            Criteria criteria = Criteria.where("pfunction").regex(productDTO.getPfunction());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getReplacement())) {
            Criteria criteria = Criteria.where("replacement").regex(productDTO.getReplacement());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getTargetuser())) {
            Criteria criteria = Criteria.where("targetuser").regex(productDTO.getTargetuser());
            query.addCriteria(criteria);
        }
        if (productDTO.getCreatetime() != null) {
            Criteria criteria = Criteria.where("createtime").gte(productDTO.getCreatetime());
            query.addCriteria(criteria);
        }
        // ----------------
        if (productDTO.getIsfree() != null) {
            Criteria criteria = Criteria.where("isfree").is(productDTO.getIsfree());
            query.addCriteria(criteria);
        }
        if (productDTO.getPrice() != null) {
            Criteria criteria = Criteria.where("price").is(productDTO.getPrice());
            query.addCriteria(criteria);
        }
        if (productDTO.getOriginalPrice() != null) {
            Criteria criteria = Criteria.where("originalPrice").is(productDTO.getOriginalPrice());
            query.addCriteria(criteria);
        }
        if (productDTO.getScore() != null) {
            Criteria criteria = Criteria.where("score").is(productDTO.getScore());
            query.addCriteria(criteria);
        }
        if (productDTO.getIsPopular() != null) {
            Criteria criteria = Criteria.where("isPopular").is(productDTO.getIsPopular());
            query.addCriteria(criteria);
        }
        if (productDTO.getIsPublished() != null) {
            Criteria criteria = Criteria.where("isPublished").is(productDTO.getIsPublished());
            query.addCriteria(criteria);
        }
        if (productDTO.getLearnerNum() != null) {
            Criteria criteria = Criteria.where("learnerNum").is(productDTO.getLearnerNum());
            query.addCriteria(criteria);
        }

        return this.listToPage(mongoTemplate.find(query, Product.class), pageable);
    }

    @Override
    public List<Product> getListByCondition(final ProductDTO productDTO) {
        Query query = new Query();
        if (!StringUtils.isEmpty(productDTO.getId())) {
            Criteria criteria = Criteria.where("id").is(productDTO.getId());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getUserid())) {
            Criteria criteria = Criteria.where("userid").is(productDTO.getUserid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getUserName())) {
            Criteria criteria = Criteria.where("userName").is(productDTO.getUserName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getPersonaids())) {
            Criteria criteria = Criteria.where("personaids").is(productDTO.getPersonaids());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getPersonaName())) {
            Criteria criteria = Criteria.where("personaName").is(productDTO.getPersonaName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getProblemid())) {
            Criteria criteria = Criteria.where("problemid").is(productDTO.getProblemid());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getProblemName())) {
            Criteria criteria = Criteria.where("problemName").is(productDTO.getProblemName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getName())) {
            Criteria criteria = Criteria.where("name").regex(productDTO.getName());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getIntro())) {
            Criteria criteria = Criteria.where("intro").regex(productDTO.getIntro());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getSolution())) {
            Criteria criteria = Criteria.where("solution").regex(productDTO.getSolution());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getPfunction())) {
            Criteria criteria = Criteria.where("pfunction").regex(productDTO.getPfunction());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getReplacement())) {
            Criteria criteria = Criteria.where("replacement").regex(productDTO.getReplacement());
            query.addCriteria(criteria);
        }
        if (!StringUtils.isEmpty(productDTO.getTargetuser())) {
            Criteria criteria = Criteria.where("targetuser").regex(productDTO.getTargetuser());
            query.addCriteria(criteria);
        }
        if (productDTO.getCreatetime() != null) {
            Criteria criteria = Criteria.where("createtime").gte(productDTO.getCreatetime());
            query.addCriteria(criteria);
        }

        //--------------
        if (productDTO.getIsfree() != null) {
            Criteria criteria = Criteria.where("isfree").is(productDTO.getIsfree());
            query.addCriteria(criteria);
        }
        if (productDTO.getPrice() != null) {
            Criteria criteria = Criteria.where("price").is(productDTO.getPrice());
            query.addCriteria(criteria);
        }
        if (productDTO.getOriginalPrice() != null) {
            Criteria criteria = Criteria.where("originalPrice").is(productDTO.getOriginalPrice());
            query.addCriteria(criteria);
        }
        if (productDTO.getScore() != null) {
            Criteria criteria = Criteria.where("score").is(productDTO.getScore());
            query.addCriteria(criteria);
        }
        if (productDTO.getIsPopular() != null) {
            Criteria criteria = Criteria.where("isPopular").is(productDTO.getIsPopular());
            query.addCriteria(criteria);
        }
        if (productDTO.getIsPublished() != null) {
            Criteria criteria = Criteria.where("isPublished").is(productDTO.getIsPublished());
            query.addCriteria(criteria);
        }
        if (productDTO.getLearnerNum() != null) {
            Criteria criteria = Criteria.where("learnerNum").is(productDTO.getLearnerNum());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query, Product.class);
    }

    public <T> Page<T> listToPage(final List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    public Product mapToEntity(final ProductDTO productDTO, final Product product) {
        BeanUtils.copyProperties(productDTO, product);
        return product;
    }

    public ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }
}