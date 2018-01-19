package com.system.db.repository.base.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.db.repository.search.criteria.SearchCriteria;
import com.system.db.repository.specification.builder.EntitySpecificationBuilder;
import com.system.logging.exception.util.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;

/**
 * The <class>BaseEntityRepositoryImpl</class> defines
 * our main repository for the system framework.
 * <p>
 * It also allows overriding or adding methods to all repositories for database access
 *
 * @author Andrew
 */
public class BaseEntityRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseEntityRepository<T, ID> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    //////////////////////////////////////////////////////////////////////

    public BaseEntityRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public BaseEntityRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                      //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Add search feature to all repositories.
     * This allows a url encoded with a filter in
     * json form.
     * <p>
     * Ex: filter:[{"operator":"like","value":"asd","property":"description"}
     *
     * @param searchFilter - what we would like to search for based on the operations specified in the search criteria array
     * @param pageable     - this enables sorting and paging
     * @return - the paged results sorted and filtered based on the search criteria
     */

    public Page<T> findAll(String searchFilter, Integer searchDepth, String[] extraSearchParams, Pageable pageable) {

        EntitySpecificationBuilder builder = new EntitySpecificationBuilder<T>(searchDepth);

        if (StringUtils.isNotEmpty(searchFilter)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<SearchCriteria> searchCriteriaList = null;
            try {
                searchCriteriaList = Arrays.asList(objectMapper.readValue(searchFilter, SearchCriteria[].class));
            } catch (IOException e) {
                ExceptionUtils.throwSystemException("Find All Failed", e);
            }

            iterate(searchCriteriaList, searchCriteria -> {

                if (searchCriteria.getValue() != null) {
                    builder.with(searchCriteria.getProperty(), searchCriteria.getOperator(), searchCriteria.getValue());
                    iterate(iterable(extraSearchParams), extraSearchParam -> builder.with(extraSearchParam, searchCriteria.getOperator(), searchCriteria.getValue()));
                }

            });
        }

        return findAll(builder.build(), pageable);
    }
}
