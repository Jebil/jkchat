package com.jkchat.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * @author Jebil Kuruvila
 */
public class SpringMvcInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {
    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.support.
     * AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses
     * ()
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.support.
     * AbstractAnnotationConfigDispatcherServletInitializer#
     * getServletConfigClasses()
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.support.
     * AbstractDispatcherServletInitializer#getServletMappings()
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
