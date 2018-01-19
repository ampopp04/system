package com.system.bean.service;

import com.system.bean.SystemBean;
import com.system.inversion.component.InversionComponent;
import com.system.util.clazz.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * The <class>SystemBeanService</class> defines a service
 * that performs executions of system beans.
 * <p>
 * This essentially means that it will accept
 * a SystemBean and parameters and properly process
 * it as if it were a real java object.
 *
 * @author Andrew
 */
@InversionComponent
public class SystemBeanService {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private AutowireCapableBeanFactory beanFactory;


    ///////////////////////////////////////////////////////////////////////
    ////////                                         Bean Creation Methods                                         //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * This method creates an instance of a Java object represented by this system bean
     *
     * @param systemBean
     * @return
     */
    public Object getSystemBeanInstance(SystemBean systemBean) {
        Class<?> beanClass = ClassUtils.forNameOrNull(systemBean.getSystemBeanType().getClassName());

        try {
            Object beanInstance = beanClass.newInstance();
            getBeanFactory().autowireBean(beanInstance);
            return beanInstance;
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating an object of type: " + beanClass, e);
        }
    }

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
