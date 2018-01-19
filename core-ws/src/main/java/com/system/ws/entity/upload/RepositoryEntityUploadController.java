package com.system.ws.entity.upload;

import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.repository.event.types.SystemRepositoryEventTypes;
import com.system.db.util.repository.RepositoryUtils;
import com.system.logging.exception.util.ExceptionUtils;
import com.system.util.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.*;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.system.ws.entity.upload.util.FileValidationUtils.validateFileUpload;
import static com.system.ws.entity.upload.util.LoadExternalDataUtils.loadExternalData;

/**
 * The <class>RepositoryEntityUploadController</class> defines
 * the ability to upload entity files to be updated, inserted, or replaced
 * within the database.
 * <p>
 * This makes it easy for outside individuals to upload excel, csv, hsv files etc
 * to modify in batch various entities in the system.
 *
 * @author Andrew
 */
@RepositoryRestController
public class RepositoryEntityUploadController {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private static final String BASE_MAPPING = "/{repository}/upload";
    private static final String ACCEPT_HEADER = "Accept";

    private static final EmbeddedWrappers WRAPPERS = new EmbeddedWrappers(false);

    private final RepositoryEntityLinks entityLinks;
    private final RepositoryRestConfiguration config;
    private final PagedResourcesAssembler<Object> pagedResourcesAssembler;
    private final Repositories repositories;
    private final HttpHeadersPreparer headersPreparer;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private ApplicationEventPublisher publisher;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                    Constructor                                                      //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    public RepositoryEntityUploadController(Repositories repositories, RepositoryRestConfiguration config,
                                            RepositoryEntityLinks entityLinks, PagedResourcesAssembler<Object> assembler,
                                            HttpHeadersPreparer headersPreparer) {

        this.repositories = repositories;
        this.pagedResourcesAssembler = assembler;
        this.entityLinks = entityLinks;
        this.config = config;
        this.headersPreparer = headersPreparer;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                          Methods                                                      //////////
    //////////////////////////////////////////////////////////////////////


    @ResponseBody
    @RequestMapping(value = BASE_MAPPING, method = RequestMethod.POST)
    public ResponseEntity<ResourceSupport> postCollectionResource(RootResourceInformation resourceInformation, @RequestParam("file") MultipartFile uploadfile, @RequestParam("uploadType") String uploadType, PersistentEntityResourceAssembler assembler, @RequestHeader(value = ACCEPT_HEADER, required = false) String acceptHeader) throws HttpRequestMethodNotSupportedException, IOException {
        SystemRepositoryEventTypes uploadEventType = SystemRepositoryEventTypes.fromName(uploadType);
        String contentType = StringUtils.substringAfterLastDot(uploadfile.getOriginalFilename()).equals("tsv") ? "text/tab-separated-values" : "text/csv";

        validateFileUpload(uploadfile, contentType);

        SystemRepository systemRepository = (SystemRepository) beanFactory.getBean(RepositoryUtils.getRepositoryName(resourceInformation.getDomainType()));

        try {
            loadExternalData(uploadfile.getInputStream(), systemRepository, (Class<? extends BaseEntity>) resourceInformation.getDomainType(), uploadEventType, contentType, resourceInformation, repositories, publisher);
        } catch (Exception e) {
            ExceptionUtils.throwSystemException("Error loading data", e);
        }
        
        return ControllerUtils.toEmptyResponse(HttpStatus.OK);
    }

}