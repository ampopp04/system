package com.system.db.repository.factory.metadata;

import com.system.db.entity.identity.EntityIdentity;
import com.system.util.clazz.ClassUtils;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

import static com.system.db.entity.identity.EntityIdentity.ID_TYPE_NAME_UPPERCASE;

/**
 * The <class>SystemRepositoryMetadata</class> will inspect generic types
 * of {@link Repository} to find out about domain and id class.
 * <p>
 * It is also smart enough to resolve ID class from within any id class that is assignable to {@link EntityIdentity}
 *
 * @author Andrew
 * @see DefaultRepositoryMetadata
 */
public class SystemRepositoryMetadata extends AbstractRepositoryMetadata {

    private static final String MUST_BE_A_REPOSITORY = String.format("Given type must be assignable to %s!",
            Repository.class);

    private final Class<? extends Serializable> idType;
    private final Class<?> domainType;

    /**
     * Creates a new {@link DefaultRepositoryMetadata} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public SystemRepositoryMetadata(Class<?> repositoryInterface) {

        super(repositoryInterface);
        Assert.isTrue(Repository.class.isAssignableFrom(repositoryInterface), MUST_BE_A_REPOSITORY);

        this.idType = resolveIdType(repositoryInterface);
        this.domainType = resolveDomainType(repositoryInterface);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.RepositoryMetadata#getDomainType()
     */
    @Override
    public Class<?> getDomainType() {
        return this.domainType;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.RepositoryMetadata#getIdType()
     */
    @Override
    public Class<? extends Serializable> getIdType() {
        return this.idType;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Serializable> resolveIdType(Class<?> repositoryInterface) {

        TypeInformation<?> information = ClassTypeInformation.from(repositoryInterface);
        List<TypeInformation<?>> arguments = information.getSuperTypeInformation(Repository.class).getTypeArguments();

        if (arguments.size() < 2 || arguments.get(1) == null) {
            throw new IllegalArgumentException(String.format("Could not resolve id type of %s!", repositoryInterface));
        }
        Class<? extends Serializable> typeClass = (Class<? extends Serializable>) arguments.get(1).getType();
        Class<? extends Serializable> idClass = (ClassUtils.isAssignable(typeClass, EntityIdentity.class)) ? (Class<? extends Serializable>) ClassUtils.getGenericTypeArgument(typeClass, ID_TYPE_NAME_UPPERCASE) : typeClass;

        return idClass;
    }

    private Class<?> resolveDomainType(Class<?> repositoryInterface) {

        TypeInformation<?> information = ClassTypeInformation.from(repositoryInterface);
        List<TypeInformation<?>> arguments = information.getSuperTypeInformation(Repository.class).getTypeArguments();

        if (arguments.isEmpty() || arguments.get(0) == null) {
            throw new IllegalArgumentException(String.format("Could not resolve domain type of %s!", repositoryInterface));
        }

        return arguments.get(0).getType();
    }
}
